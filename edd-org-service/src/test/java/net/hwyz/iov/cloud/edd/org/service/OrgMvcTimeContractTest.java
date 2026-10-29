package net.hwyz.iov.cloud.edd.org.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.DepartmentDto;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.EmployeeDto;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.PositionDto;
import net.hwyz.iov.cloud.edd.org.service.application.service.DepartmentAppService;
import net.hwyz.iov.cloud.edd.org.service.application.service.EmployeeAppService;
import net.hwyz.iov.cloud.edd.org.service.application.service.PositionAppService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * EDD-ORG-DSN-CR-001：真实 MVC 链路 JSON 输出契约测试。
 * <p>
 * 覆盖 /service/* Feign 入站接口的真实 MVC mapper 链（HeaderInterceptor → Controller →
 * HttpMessageConverter → 生产 ObjectMapper），断言 api/vo 唯一时间字段
 * java.util.Date createTime 输出 13 位 epoch 毫秒数字（升级前后不变），
 * 且排除范围字段（Long id / String / Integer / Boolean）不变。
 *
 * @author hwyz_leo
 * @since 2026-10-29
 */
@SpringBootTest
@AutoConfigureMockMvc
class OrgMvcTimeContractTest {

    private static final Instant CREATE_TIME = Instant.parse("2026-10-28T12:43:32Z");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PositionAppService positionAppService;

    @MockBean
    EmployeeAppService employeeAppService;

    @MockBean
    DepartmentAppService departmentAppService;

    @Test
    void servicePosition_getById_createTime输出epoch毫秒数字且排除字段不变() throws Exception {
        when(positionAppService.getPositionById(1L)).thenReturn(PositionDto.builder()
                .id(1L)
                .code("P001")
                .name("岗位一")
                .enable(true)
                .sort(7)
                .createTime(CREATE_TIME)
                .build());

        String body = mockMvc.perform(get("/service/position/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(body);
        assertThat(node.get("id").isIntegralNumber()).isTrue();
        assertThat(node.get("id").asLong()).isEqualTo(1L);
        assertThat(node.get("code").asText()).isEqualTo("P001");
        assertThat(node.get("name").asText()).isEqualTo("岗位一");
        assertThat(node.get("enable").asBoolean()).isTrue();
        assertThat(node.get("sort").asInt()).isEqualTo(7);
        assertThat(node.get("createTime").isIntegralNumber()).isTrue();
        assertThat(node.get("createTime").asLong()).isEqualTo(CREATE_TIME.toEpochMilli());
    }

    @Test
    void serviceEmployee_getById_createTime输出epoch毫秒数字() throws Exception {
        when(employeeAppService.getEmployeeById(2L)).thenReturn(EmployeeDto.builder()
                .id(2L)
                .code("E001")
                .name("张三")
                .gender(1)
                .phone("13800000000")
                .enable(true)
                .createTime(CREATE_TIME)
                .build());

        String body = mockMvc.perform(get("/service/employee/2"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(body);
        assertThat(node.get("id").asLong()).isEqualTo(2L);
        assertThat(node.get("code").asText()).isEqualTo("E001");
        assertThat(node.get("createTime").asLong()).isEqualTo(CREATE_TIME.toEpochMilli());
    }

    @Test
    void serviceDepartment_getById_createTime输出epoch毫秒数字() throws Exception {
        when(departmentAppService.getDepartmentById(3L)).thenReturn(DepartmentDto.builder()
                .id(3L)
                .parentId(1L)
                .name("部门一")
                .code("D001")
                .leader("李四")
                .enable(true)
                .sort(3)
                .createTime(CREATE_TIME)
                .build());

        String body = mockMvc.perform(get("/service/department/3"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(body);
        assertThat(node.get("id").asLong()).isEqualTo(3L);
        assertThat(node.get("code").asText()).isEqualTo("D001");
        assertThat(node.get("createTime").asLong()).isEqualTo(CREATE_TIME.toEpochMilli());
    }
}
