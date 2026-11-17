package net.hwyz.iov.cloud.edd.org.service;

import com.baomidou.mybatisplus.annotation.TableField;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipStaffExService;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipStaffMpt;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.mpt.MptDealershipStaffController;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.DealershipStaffMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipStaffPo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EDD-ORG-DSN-CR-003 / G-001、G-003：门店员工关联契约与跨库依赖门禁测试。
 * <p>
 * 断言：对外/管理后台契约只暴露 ORG 员工事实（不含 MPT 账号字段）、持久化对象只绑定员工ID、
 * Mapper 不再出现 {@code db_mpt}/{@code ry.sys_user} 引用、Flyway V4 结构重构落地、
 * 候选人检索接口由 {@code searchUser} 收敛为 {@code searchEmployee}。
 *
 * @author hwyz_leo
 * @since 2026-11-17
 */
class DealershipStaffContractTest {

    private static final List<String> EMPLOYEE_FIELDS =
            List.of("employeeId", "employeeCode", "employeeName", "phone");
    private static final List<String> FORBIDDEN_MPT_FIELDS =
            List.of("userId", "userName", "nickName", "phonenumber");

    @Test
    @DisplayName("对外契约 DealershipStaffExService 只含员工事实，不含 MPT 账号字段")
    void exServiceContractExposesEmployeeFactsOnly() {
        List<String> fields = declaredFieldNames(DealershipStaffExService.class);

        assertThat(fields).containsAll(EMPLOYEE_FIELDS);
        assertThat(fields).doesNotContainAnyElementsOf(FORBIDDEN_MPT_FIELDS);
    }

    @Test
    @DisplayName("管理后台契约 DealershipStaffMpt 只含员工事实，不含 MPT 账号字段")
    void mptContractExposesEmployeeFactsOnly() {
        List<String> fields = declaredFieldNames(DealershipStaffMpt.class);

        assertThat(fields).containsAll(EMPLOYEE_FIELDS);
        assertThat(fields).doesNotContainAnyElementsOf(FORBIDDEN_MPT_FIELDS);
    }

    @Test
    @DisplayName("持久化对象绑定 employee_id，展示字段不参与持久化")
    void persistenceObjectBindsEmployeeId() throws NoSuchFieldException {
        List<String> fields = declaredFieldNames(DealershipStaffPo.class);
        assertThat(fields).doesNotContainAnyElementsOf(FORBIDDEN_MPT_FIELDS);

        Field employeeId = DealershipStaffPo.class.getDeclaredField("employeeId");
        TableField employeeIdMapping = employeeId.getAnnotation(TableField.class);
        assertThat(employeeIdMapping).isNotNull();
        assertThat(employeeIdMapping.value()).isEqualTo("employee_id");

        for (String displayField : List.of("employeeCode", "employeeName", "phone", "dealershipName")) {
            TableField mapping = DealershipStaffPo.class.getDeclaredField(displayField).getAnnotation(TableField.class);
            assertThat(mapping).as(displayField).isNotNull();
            assertThat(mapping.exist()).as(displayField).isFalse();
        }
    }

    @Test
    @DisplayName("Mapper XML 不存在 MPT 跨库引用，关联查询限定在 tb_employee")
    void mapperXmlHasNoCrossDatabaseReference() throws IOException {
        String xml = readClasspath("/mappers/DealershipStaffMapper.xml");

        assertThat(xml).doesNotContain("db_mpt");
        assertThat(xml).doesNotContain("sys_user");
        assertThat(xml).doesNotContain("login_name");
        assertThat(xml).doesNotContain("nick_name");
        assertThat(xml).contains("INNER JOIN tb_employee");
        assertThat(xml).contains("selectEmployeeByKey");
        assertThat(xml).contains("deletePoByEmployeeIds");
    }

    @Test
    @DisplayName("Mapper 接口新增员工维度方法，移除 MPT 用户查询方法")
    void mapperInterfaceExposesEmployeeOperations() {
        List<String> methodNames = Arrays.stream(DealershipStaffMapper.class.getDeclaredMethods())
                .map(Method::getName)
                .collect(Collectors.toList());

        assertThat(methodNames).contains("selectPoByEmployeeId", "selectEmployeeByKey", "deletePoByEmployeeIds");
        assertThat(methodNames).doesNotContain("selectSysUserByKey", "selectPoByUserId");
    }

    @Test
    @DisplayName("Flyway V4 移除 user_id、employee_id 非空并建立门店内唯一键")
    void flywayMigrationRestructuresDealershipStaff() throws IOException {
        String sql = readClasspath("/db/migration/V4__dealership_staff_link_employee.sql");

        assertThat(sql).contains("DROP COLUMN `user_id`");
        assertThat(sql).contains("MODIFY COLUMN `employee_id` BIGINT NOT NULL");
        assertThat(sql).contains("ADD UNIQUE KEY `uk_dealership_employee` (`dealership_code`, `employee_id`)");
    }

    @Test
    @DisplayName("候选人检索路由由 searchUser 收敛为 searchEmployee")
    void candidateSearchRouteRenamed() {
        List<String> routes = Arrays.stream(MptDealershipStaffController.class.getDeclaredMethods())
                .map(method -> method.getAnnotation(GetMapping.class))
                .filter(mapping -> mapping != null && mapping.value().length > 0)
                .map(mapping -> mapping.value()[0])
                .collect(Collectors.toList());

        assertThat(routes).contains("/searchEmployee");
        assertThat(routes).doesNotContain("/searchUser");
    }

    private List<String> declaredFieldNames(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .filter(field -> !field.isSynthetic() && !java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    private String readClasspath(String path) throws IOException {
        try (InputStream inputStream = DealershipStaffContractTest.class.getResourceAsStream(path)) {
            assertThat(inputStream).as(path).isNotNull();
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
