package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeChangeExService;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeOptionExService;
import net.hwyz.iov.cloud.edd.org.service.application.service.EmployeeAppService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * EDD-ORG-DSN-CR-004 / US-024：员工增量同步与在职员工检索接口测试。
 * <p>
 * 覆盖：默认分页与上限收敛、水位（epoch 毫秒 / ISO-8601）解析、墓碑字段透出、
 * 在职员工检索委托与上限、非法水位被拒绝。
 *
 * @author hwyz_leo
 * @since 2026-11-17
 */
class ServiceEmployeeChangesTest {

    private static final String CHANGES_PATH = "/service/employee/changes";
    private static final String ACTIVE_PATH = "/service/employee/active";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private EmployeeAppService employeeAppService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        employeeAppService = mock(EmployeeAppService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ServiceEmployeeController(employeeAppService)).build();
    }

    @Test
    @DisplayName("默认分页为 page=1/size=200，并按 offset/limit 查询")
    void changesUsesDefaultPaging() throws Exception {
        when(employeeAppService.countEmployeeChanges(isNull())).thenReturn(1L);
        when(employeeAppService.findEmployeeChanges(isNull(), eq(0), eq(200))).thenReturn(List.of(employee()));

        String body = mockMvc.perform(get(CHANGES_PATH))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(java.nio.charset.StandardCharsets.UTF_8);

        JsonNode json = objectMapper.readTree(body);
        assertThat(json.get("total").asLong()).isEqualTo(1L);
        JsonNode item = json.get("items").get(0);
        assertThat(item.get("employeeId").asLong()).isEqualTo(1001L);
        assertThat(item.get("code").asText()).isEqualTo("E1001");
        assertThat(item.get("enable").asBoolean()).isTrue();
        assertThat(item.get("rowValid").asBoolean()).isTrue();
        assertThat(item.has("userName")).isFalse();
        assertThat(item.has("password")).isFalse();
        verify(employeeAppService).findEmployeeChanges(isNull(), eq(0), eq(200));
    }

    @Test
    @DisplayName("size 超过 500 时按 500 收敛")
    void changesClampsPageSize() throws Exception {
        when(employeeAppService.countEmployeeChanges(any())).thenReturn(0L);
        when(employeeAppService.findEmployeeChanges(any(), anyInt(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get(CHANGES_PATH).param("page", "3").param("size", "9999"))
                .andExpect(status().isOk());

        verify(employeeAppService).findEmployeeChanges(isNull(), eq(1000), eq(500));
    }

    @Test
    @DisplayName("since 支持 epoch 毫秒与 ISO-8601，两者解析为同一瞬时")
    void changesParsesWatermarkBothFormats() throws Exception {
        long epochMilli = 1763000000123L;
        String iso = OffsetDateTime.ofInstant(new Date(epochMilli).toInstant(), java.time.ZoneOffset.UTC).toString();
        when(employeeAppService.countEmployeeChanges(any())).thenReturn(0L);
        when(employeeAppService.findEmployeeChanges(any(), anyInt(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get(CHANGES_PATH).param("since", String.valueOf(epochMilli))).andExpect(status().isOk());
        ArgumentCaptor<Date> epochCaptor = ArgumentCaptor.forClass(Date.class);
        verify(employeeAppService).countEmployeeChanges(epochCaptor.capture());
        assertThat(epochCaptor.getValue().toInstant().toEpochMilli()).isEqualTo(epochMilli);

        when(employeeAppService.countEmployeeChanges(any())).thenReturn(0L);
        mockMvc.perform(get(CHANGES_PATH).param("since", iso)).andExpect(status().isOk());
        ArgumentCaptor<Date> isoCaptor = ArgumentCaptor.forClass(Date.class);
        verify(employeeAppService, org.mockito.Mockito.times(2)).countEmployeeChanges(isoCaptor.capture());
        assertThat(isoCaptor.getValue().toInstant().toEpochMilli()).isEqualTo(epochMilli);
    }

    @Test
    @DisplayName("since 格式非法时拒绝请求")
    void changesRejectsInvalidWatermark() {
        // standalone MockMvc 不会包装未映射异常，直接抛出 ServletException
        org.junit.jupiter.api.Assertions.assertThrows(jakarta.servlet.ServletException.class,
                () -> mockMvc.perform(get(CHANGES_PATH).param("since", "not-a-date")));
        verify(employeeAppService, org.mockito.Mockito.never()).findEmployeeChanges(any(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("在职员工检索：委托关键词并限制 20 条，只返回员工字段")
    void activeReturnsEmployeeOptions() throws Exception {
        when(employeeAppService.searchActiveEmployees(eq("E1"), eq(20))).thenReturn(List.of(employee()));

        String body = mockMvc.perform(get(ACTIVE_PATH).param("key", "E1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(java.nio.charset.StandardCharsets.UTF_8);

        JsonNode json = objectMapper.readTree(body);
        assertThat(json.isArray()).isTrue();
        assertThat(json.get(0).get("employeeId").asLong()).isEqualTo(1001L);
        assertThat(json.get(0).get("employeeCode").asText()).isEqualTo("E1001");
        assertThat(json.get(0).get("employeeName").asText()).isEqualTo("张三");
        assertThat(json.get(0).has("userName")).isFalse();
        verify(employeeAppService).searchActiveEmployees("E1", 20);
    }

    private EmployeePo employee() {
        return EmployeePo.builder()
                .id(1001L)
                .code("E1001")
                .name("张三")
                .phone("13900000000")
                .email("zhangsan@example.com")
                .enable(true)
                .leaveDate(LocalDate.of(2026, 12, 31))
                .rowValid(true)
                .modifyTime(new Date(1763000000123L))
                .build();
    }

    @Test
    @DisplayName("契约 VO 不包含账号/权限字段")
    void contractVosExposeEmployeeFactsOnly() {
        assertThat(EmployeeChangeExService.class.getDeclaredFields())
                .extracting(java.lang.reflect.Field::getName)
                .contains("employeeId", "code", "name", "enable", "leaveDate", "rowValid", "modifyTime")
                .doesNotContain("userName", "password", "roles", "permissions", "eiamUserId");
        assertThat(EmployeeOptionExService.class.getDeclaredFields())
                .extracting(java.lang.reflect.Field::getName)
                .containsExactlyInAnyOrder("employeeId", "employeeCode", "employeeName", "phone");
    }
}
