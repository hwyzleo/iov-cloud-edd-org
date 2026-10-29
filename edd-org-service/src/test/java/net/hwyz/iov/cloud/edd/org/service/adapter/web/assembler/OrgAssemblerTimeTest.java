package net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.hwyz.iov.cloud.edd.org.api.vo.DepartmentMpt;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeMpt;
import net.hwyz.iov.cloud.edd.org.api.vo.OrgMpt;
import net.hwyz.iov.cloud.edd.org.api.vo.PositionMpt;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.DepartmentDto;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.EmployeeDto;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.OrganizationDto;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.PositionDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EDD-ORG-DSN-CR-001：assembler 时间转换语义契约测试。
 * <p>
 * application DTO 的 java.time（Instant createTime / LocalDate birthday 等）经
 * MapStruct 转换到 api/vo 的 java.util.Date（JSON 出口唯一时间字段 createTime）。
 * 断言：绝对时刻一致（Date.from(Instant)），且与 JVM 默认时区无关（UTC 与 Asia/Shanghai
 * 下结果相同，无八小时漂移）；modifyTime/LocalDate 字段不暴露到 VO（api/vo 无对应字段，
 * 序列化 JSON 不含这些 key）。
 *
 * @author hwyz_leo
 * @since 2026-10-29
 */
class OrgAssemblerTimeTest {

    private static final Instant CREATE_TIME = Instant.parse("2026-10-28T12:43:32Z");
    private static final Instant MODIFY_TIME = Instant.parse("2026-10-28T13:00:00Z");

    private final ObjectMapper mapper = new ObjectMapper();

    private TimeZone originalTimeZone;

    @BeforeEach
    void setUp() {
        originalTimeZone = TimeZone.getDefault();
    }

    @AfterEach
    void tearDown() {
        TimeZone.setDefault(originalTimeZone);
    }

    @Test
    void orgDto_toOrgMpt_createTime保持绝对时刻() throws Exception {
        OrganizationDto dto = OrganizationDto.builder()
                .id(1L)
                .code("ORG001")
                .name("组织1")
                .orgType("COMPANY")
                .parentId(0L)
                .enable(true)
                .sort(1)
                .createTime(CREATE_TIME)
                .modifyTime(MODIFY_TIME)
                .build();

        OrgMpt vo = OrgMptAssembler.INSTANCE.fromDto(dto);

        assertThat(vo.getId()).isEqualTo(1L);
        assertThat(vo.getCode()).isEqualTo("ORG001");
        assertThat(vo.getCreateTime()).isEqualTo(Date.from(CREATE_TIME));
        // api/vo 不暴露 modifyTime（无对应字段）
        assertThat(mapper.writeValueAsString(vo)).doesNotContain("modifyTime");
    }

    @Test
    void employeeDto_toEmployeeMpt_createTime保持绝对时刻且LocalDate不暴露() throws Exception {
        EmployeeDto dto = EmployeeDto.builder()
                .id(2L)
                .code("E001")
                .name("张三")
                .gender(1)
                .birthday(LocalDate.of(1990, 1, 1))
                .hireDate(LocalDate.of(2020, 6, 1))
                .leaveDate(LocalDate.of(2025, 12, 31))
                .enable(true)
                .sort(1)
                .createTime(CREATE_TIME)
                .modifyTime(MODIFY_TIME)
                .build();

        EmployeeMpt vo = EmployeeMptAssembler.INSTANCE.fromDto(dto);

        assertThat(vo.getCreateTime()).isEqualTo(Date.from(CREATE_TIME));
        assertThat(vo.getId()).isEqualTo(2L);
        assertThat(vo.getCode()).isEqualTo("E001");
        assertThat(vo.getName()).isEqualTo("张三");
        // EmployeeMpt 无 birthday/hireDate/leaveDate/modifyTime 字段 → 不暴露
        String json = mapper.writeValueAsString(vo);
        assertThat(json).doesNotContain("birthday");
        assertThat(json).doesNotContain("hireDate");
        assertThat(json).doesNotContain("leaveDate");
        assertThat(json).doesNotContain("modifyTime");
    }

    @Test
    void positionDto_toPositionMpt_createTime保持绝对时刻() throws Exception {
        PositionDto dto = PositionDto.builder()
                .id(3L)
                .code("P001")
                .name("岗位1")
                .enable(true)
                .sort(1)
                .createTime(CREATE_TIME)
                .modifyTime(MODIFY_TIME)
                .build();

        PositionMpt vo = PositionMptAssembler.INSTANCE.fromDto(dto);

        assertThat(vo.getCreateTime()).isEqualTo(Date.from(CREATE_TIME));
        assertThat(mapper.writeValueAsString(vo)).doesNotContain("modifyTime");
    }

    @Test
    void departmentDto_toDepartmentMpt_createTime保持绝对时刻() throws Exception {
        DepartmentDto dto = DepartmentDto.builder()
                .id(4L)
                .parentId(1L)
                .name("部门1")
                .code("D001")
                .leader("李四")
                .enable(true)
                .sort(1)
                .createTime(CREATE_TIME)
                .modifyTime(MODIFY_TIME)
                .build();

        DepartmentMpt vo = DepartmentMptAssembler.INSTANCE.fromDto(dto);

        assertThat(vo.getCreateTime()).isEqualTo(Date.from(CREATE_TIME));
        assertThat(mapper.writeValueAsString(vo)).doesNotContain("modifyTime");
    }

    @Test
    void 时间转换与JVM默认时区无关_UTC与上海结果一致() {
        OrganizationDto dto = OrganizationDto.builder()
                .id(1L)
                .code("ORG001")
                .name("组织1")
                .orgType("COMPANY")
                .parentId(0L)
                .enable(true)
                .sort(1)
                .createTime(CREATE_TIME)
                .modifyTime(MODIFY_TIME)
                .build();

        // UTC JVM
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        OrgMpt utcVo = OrgMptAssembler.INSTANCE.fromDto(dto);

        // Asia/Shanghai JVM
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        OrgMpt shVo = OrgMptAssembler.INSTANCE.fromDto(dto);

        assertThat(utcVo.getCreateTime()).isEqualTo(Date.from(CREATE_TIME));
        assertThat(shVo.getCreateTime()).isEqualTo(Date.from(CREATE_TIME));
        assertThat(shVo.getCreateTime()).isEqualTo(utcVo.getCreateTime());
        assertThat(shVo.getCreateTime().getTime()).isEqualTo(CREATE_TIME.toEpochMilli());
    }

    @Test
    void null时间保持null不转换为epoch0() {
        OrganizationDto dto = OrganizationDto.builder()
                .id(1L)
                .code("ORG001")
                .name("组织1")
                .orgType("COMPANY")
                .enable(true)
                .sort(1)
                .build();

        OrgMpt vo = OrgMptAssembler.INSTANCE.fromDto(dto);

        assertThat(vo.getCreateTime()).isNull();
    }
}
