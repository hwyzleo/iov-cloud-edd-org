package net.hwyz.iov.cloud.edd.org.service;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.repository.EmployeeRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EDD-ORG-DSN-CR-004 / US-024、US-025：员工增量同步与删除逻辑化门禁测试。
 * <p>
 * 断言：增量查询不按 row_valid 过滤（墓碑可同步）、按 modify_time+id 稳定排序、
 * 在职检索仅返回启用员工、Flyway V5 建立水位索引、员工删除改为逻辑删除。
 *
 * @author hwyz_leo
 * @since 2026-11-17
 */
class EmployeeSyncContractTest {

    @Test
    @DisplayName("增量查询包含逻辑删除墓碑，并按 modify_time、id 稳定排序")
    void changesQueryIncludesTombstones() throws IOException {
        String xml = readClasspath("/mappers/EmployeeMapper.xml");

        assertThat(xml).contains("id=\"selectChanges\"");
        assertThat(xml).contains("ORDER BY t.modify_time ASC, t.id ASC");
        assertThat(xml).contains("LIMIT #{offset}, #{limit}");
        // 增量查询不得带 row_valid = 1（否则墓碑不可见）
        String changesBlock = xml.substring(xml.indexOf("id=\"selectChanges\""), xml.indexOf("id=\"countChanges\""));
        assertThat(changesBlock).doesNotContain("row_valid = 1");
        assertThat(changesBlock).contains("t.modify_time &gt; #{since}");
    }

    @Test
    @DisplayName("在职员工检索仅返回启用且未删除的员工")
    void activeQueryFiltersEnabledEmployees() throws IOException {
        String xml = readClasspath("/mappers/EmployeeMapper.xml");
        String activeBlock = xml.substring(xml.indexOf("id=\"selectActiveByKey\""));
        assertThat(activeBlock).contains("t.row_valid = 1");
        assertThat(activeBlock).contains("t.enable = 1");
        assertThat(activeBlock).contains("LIMIT #{limit}");
    }

    @Test
    @DisplayName("Flyway V5 建立 modify_time 水位索引")
    void flywayCreatesWatermarkIndex() throws IOException {
        String sql = readClasspath("/db/migration/V5__employee_sync_index.sql");
        assertThat(sql).contains("CREATE INDEX `idx_modify_time` ON `tb_employee` (`modify_time`, `id`)");
    }

    @Test
    @DisplayName("员工删除改为逻辑删除，保留墓碑供同步")
    void employeeDeleteIsLogical() {
        String source = readSource("src/main/java/net/hwyz/iov/cloud/edd/org/service/infrastructure/persistence/repository/EmployeeRepositoryImpl.java");
        assertThat(source).contains("employeeMapper.logicalDeletePo(id)");
        assertThat(source).doesNotContain("employeeMapper.physicalDeletePo(id)");
        assertThat(EmployeeRepositoryImpl.class).isNotNull();
    }

    private String readClasspath(String path) throws IOException {
        try (InputStream inputStream = EmployeeSyncContractTest.class.getResourceAsStream(path)) {
            assertThat(inputStream).as(path).isNotNull();
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String readSource(String relativePath) {
        try {
            return java.nio.file.Files.readString(java.nio.file.Path.of(relativePath));
        } catch (IOException e) {
            throw new IllegalStateException("读取源文件失败：" + relativePath, e);
        }
    }
}
