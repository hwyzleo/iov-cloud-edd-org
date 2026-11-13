package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hwyz.iov.cloud.edd.org.api.enums.DealershipServiceType;
import net.hwyz.iov.cloud.edd.org.api.service.OrgDealershipService;
import net.hwyz.iov.cloud.edd.org.api.vo.Dealership;
import net.hwyz.iov.cloud.edd.org.service.application.service.DealershipAppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * EDD-ORG-DSN-CR-002 / US-001：通用内部门店列表接口控制器测试。
 * <p>
 * 使用 standaloneSetup 装配，避免依赖 Nacos/MySQL 与容器过滤器，保证本地与 CI 结果一致。
 * 覆盖：精确路由不抢占既有 {@code /{dealershipCode}}、S/D/A 委托、6 参数完整透传、
 * 空列表、距离空值语义、缺失/非法 serviceType 拒绝且不退化为全量查询、Feign 契约一致性。
 *
 * @author hwyz_leo
 * @since 2026-11-13
 */
class ServiceDealershipControllerTest {

    private static final String LIST_PATH = "/service/dealership/list";
    private static final String BY_CODE_PATH = "/service/dealership/{dealershipCode}";
    private static final String[] PARAM_NAMES =
            {"serviceType", "provinceCode", "cityCode", "countyCode", "requestLon", "requestLat"};

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DealershipAppService dealershipAppService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        dealershipAppService = mock(DealershipAppService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ServiceDealershipController(dealershipAppService)).build();
    }

    @Test
    @DisplayName("S/D/A 三种服务类型均委托 AppService 并完整透传区域与经纬度参数")
    void list_delegates_for_each_service_type_with_all_params() throws Exception {
        for (DealershipServiceType serviceType : DealershipServiceType.values()) {
            when(dealershipAppService.getDealershipList(serviceType, "31", "3101", "310105", "121.473962",
                    "31.234567")).thenReturn(List.of());

            mockMvc.perform(get(LIST_PATH)
                            .param("serviceType", serviceType.name())
                            .param("provinceCode", "31")
                            .param("cityCode", "3101")
                            .param("countyCode", "310105")
                            .param("requestLon", "121.473962")
                            .param("requestLat", "31.234567"))
                    .andExpect(status().isOk());

            verify(dealershipAppService).getDealershipList(serviceType, "31", "3101", "310105", "121.473962",
                    "31.234567");
        }
    }

    @Test
    @DisplayName("仅提供服务类型时其余参数以 null 透传，不隐式补全条件")
    void list_passes_null_for_absent_optional_params() throws Exception {
        when(dealershipAppService.getDealershipList(any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get(LIST_PATH).param("serviceType", "S")).andExpect(status().isOk());

        ArgumentCaptor<DealershipServiceType> typeCaptor = ArgumentCaptor.forClass(DealershipServiceType.class);
        ArgumentCaptor<String> provinceCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> cityCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> countyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> lonCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> latCaptor = ArgumentCaptor.forClass(String.class);
        verify(dealershipAppService).getDealershipList(typeCaptor.capture(), provinceCaptor.capture(),
                cityCaptor.capture(), countyCaptor.capture(), lonCaptor.capture(), latCaptor.capture());

        assertThat(typeCaptor.getValue()).isEqualTo(DealershipServiceType.S);
        assertThat(provinceCaptor.getValue()).isNull();
        assertThat(cityCaptor.getValue()).isNull();
        assertThat(countyCaptor.getValue()).isNull();
        assertThat(lonCaptor.getValue()).isNull();
        assertThat(latCaptor.getValue()).isNull();
    }

    @Test
    @DisplayName("响应为裸门店数组，包含 code/name/address/distance 四个字段")
    void list_returns_bare_dealership_array() throws Exception {
        when(dealershipAppService.getDealershipList(any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(Dealership.builder().code("SHSA01").name("上海销售门店")
                        .address("上海市浦东新区某路1号").distance(12.34).build()));

        String body = mockMvc.perform(get(LIST_PATH).param("serviceType", "S"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode node = objectMapper.readTree(body);
        assertThat(node.isArray()).isTrue();
        assertThat(node).hasSize(1);
        JsonNode item = node.get(0);
        assertThat(item.get("code").asText()).isEqualTo("SHSA01");
        assertThat(item.get("name").asText()).isEqualTo("上海销售门店");
        assertThat(item.get("address").asText()).isEqualTo("上海市浦东新区某路1号");
        assertThat(item.get("distance").asDouble()).isEqualTo(12.34);
        assertThat(item.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("无结果返回空数组而不是 null")
    void list_returns_empty_array_when_no_match() throws Exception {
        when(dealershipAppService.getDealershipList(any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get(LIST_PATH).param("serviceType", "A"))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8))
                        .isEqualTo("[]"));
    }

    @Test
    @DisplayName("无法计算距离时 distance 如实输出 null，不虚构距离")
    void list_keeps_null_distance() throws Exception {
        when(dealershipAppService.getDealershipList(any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(Dealership.builder().code("NJSA01").name("南京销售门店")
                        .address("南京市某路2号").distance(null).build()));

        String body = mockMvc.perform(get(LIST_PATH).param("serviceType", "S"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode distance = objectMapper.readTree(body).get(0).get("distance");
        assertThat(distance.isNull()).isTrue();
    }

    @Test
    @DisplayName("缺失 serviceType 时拒绝请求，且不调用 AppService（不退化为全量查询）")
    void list_rejects_missing_service_type() throws Exception {
        mockMvc.perform(get(LIST_PATH)).andExpect(status().isBadRequest());

        verifyNoInteractions(dealershipAppService);
    }

    @Test
    @DisplayName("非法 serviceType 时拒绝请求，且不调用 AppService（不退化为全量查询）")
    void list_rejects_invalid_service_type() throws Exception {
        mockMvc.perform(get(LIST_PATH).param("serviceType", "X"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(dealershipAppService);
    }

    @Test
    @DisplayName("接入框架全局异常处理后，缺失/非法 serviceType 仍被拒绝且不返回门店列表")
    void list_rejects_invalid_service_type_under_framework_global_handler() throws Exception {
        MockMvc frameworkMvc = MockMvcBuilders
                .standaloneSetup(new ServiceDealershipController(dealershipAppService))
                .setControllerAdvice(new net.hwyz.iov.cloud.framework.web.handler.GlobalExceptionHandler())
                .build();

        for (String url : List.of(LIST_PATH, LIST_PATH + "?serviceType=X")) {
            org.springframework.test.web.servlet.MvcResult result = frameworkMvc.perform(get(url)).andReturn();
            String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
            int status = result.getResponse().getStatus();

            // 参数错误响应沿用框架既有行为（当前全局异常处理器统一转为 5xx 错误响应），
            // 本 CR 只要求“拒绝且不退化为全量查询”，不在此锁定具体状态码。
            assertThat(status).as("请求 %s 应被拒绝", url).isGreaterThanOrEqualTo(400);
            assertThat(body).as("请求 %s 不得返回门店数组", url).doesNotStartWith("[").isNotEqualTo("[]");
        }

        verifyNoInteractions(dealershipAppService);
    }

    @Test
    @DisplayName("/list 精确路由不被 /{dealershipCode} 抢占，按编码查询行为不变")
    void list_route_is_not_shadowed_by_code_route() throws Exception {
        when(dealershipAppService.getDealershipList(any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(Dealership.builder().code("SHDE01").build()));
        when(dealershipAppService.getDealershipByCode("SHDE01")).thenReturn(null);

        mockMvc.perform(get(LIST_PATH).param("serviceType", "D")).andExpect(status().isOk());
        verify(dealershipAppService).getDealershipList(DealershipServiceType.D, null, null, null, null, null);
        verify(dealershipAppService, never()).getDealershipByCode("list");

        mockMvc.perform(get(BY_CODE_PATH, "SHDE01")).andExpect(status().isOk());
        verify(dealershipAppService).getDealershipByCode("SHDE01");
    }

    @Test
    @DisplayName("Feign 契约与控制器契约一致：路径 /list、serviceType 必填、其余 5 项可选")
    void feign_contract_matches_controller_contract() throws Exception {
        assertListContract(OrgDealershipService.class, "getDealershipList");
        assertListContract(ServiceDealershipController.class, "getDealershipList");

        assertThat(OrgDealershipService.class.getAnnotation(
                org.springframework.cloud.openfeign.FeignClient.class).path())
                .isEqualTo(requestMappingPath(ServiceDealershipController.class));
    }

    private void assertListContract(Class<?> type, String methodName) {
        Method method = findMethod(type, methodName, PARAM_NAMES.length);
        GetMapping mapping = method.getAnnotation(GetMapping.class);
        assertThat(mapping).as("%s#%s 缺少 @GetMapping", type.getSimpleName(), methodName).isNotNull();
        assertThat(mapping.value()).containsExactly("/list");

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < PARAM_NAMES.length; i++) {
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            assertThat(requestParam)
                    .as("%s#%s 参数 %s 缺少 @RequestParam", type.getSimpleName(), methodName, PARAM_NAMES[i])
                    .isNotNull();
            assertThat(requestParam.required())
                    .as("%s#%s 参数 %s 的 required 与契约不一致", type.getSimpleName(), methodName, PARAM_NAMES[i])
                    .isEqualTo(i == 0);
            // 未显式指定 value 时由编译期 -parameters 提供参数名，行为由请求绑定测试保证
            assertThat(requestParam.value()).as("%s#%s 参数 %s 显式指定了非法名称", type.getSimpleName(),
                    methodName, PARAM_NAMES[i]).isIn("", PARAM_NAMES[i]);
        }
    }

    private Method findMethod(Class<?> type, String name, int parameterCount) {
        for (Method method : type.getDeclaredMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == parameterCount) {
                return method;
            }
        }
        throw new AssertionError("未找到方法: " + type.getSimpleName() + "#" + name);
    }

    private String requestMappingPath(Class<?> type) {
        RequestMapping mapping = type.getAnnotation(RequestMapping.class);
        assertThat(mapping).as("%s 缺少 @RequestMapping", type.getSimpleName()).isNotNull();
        return String.join("", mapping.value());
    }

}
