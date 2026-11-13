package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hwyz.iov.cloud.edd.org.service.application.service.DealershipAppService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.DealershipMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipPo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * EDD-ORG-DSN-CR-002 / US-001：通用内部门店列表接口集成测试。
 * <p>
 * 链路：HTTP 请求 → ServiceDealershipController（真实）→ DealershipAppService（真实，含距离与排序）
 * → DealershipPoConverter（MapStruct 生成实现）→ Jackson 输出。
 * 仅持久层 mapper 使用桩替代，避免依赖环境数据库；真实 VSO → ORG 联调不在此断言范围。
 *
 * @author hwyz_leo
 * @since 2026-11-13
 */
class ServiceDealershipListIntegrationTest {

    private static final String LIST_PATH = "/service/dealership/list";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DealershipMapper dealershipMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        dealershipMapper = mock(DealershipMapper.class);
        ServiceDealershipController controller =
                new ServiceDealershipController(new DealershipAppService(dealershipMapper));
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("真实链路返回门店列表并按距离升序，映射 code/name/address 且附带距离")
    void returns_dealerships_sorted_by_distance_end_to_end() throws Exception {
        when(dealershipMapper.selectPoByMap(anyMap())).thenReturn(List.of(
                po("SZSA01", "深圳销售门店", "深圳市南山区某路3号", "31.565600", "120.121200"),
                po("NJSA01", "南京销售门店", "南京市某路2号", null, null),
                po("SHSA01", "上海销售门店", "上海市浦东新区某路1号", "31.656565", "121.545454")));

        String body = mockMvc.perform(get(LIST_PATH)
                        .param("serviceType", "S")
                        .param("provinceCode", "31")
                        .param("requestLon", "121.473962")
                        .param("requestLat", "31.234567"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        verify(dealershipMapper).selectPoByMap(org.mockito.ArgumentMatchers.<Map<String, Object>>argThat(map ->
                "S".equals(map.get("serviceType")) && "31".equals(map.get("provinceCode"))));

        JsonNode node = objectMapper.readTree(body);
        assertThat(node.isArray()).isTrue();
        assertThat(node).hasSize(3);
        assertThat(node.get(0).get("code").asText()).isEqualTo("SHSA01");
        assertThat(node.get(0).get("name").asText()).isEqualTo("上海销售门店");
        assertThat(node.get(0).get("address").asText()).isEqualTo("上海市浦东新区某路1号");
        assertThat(node.get(0).get("distance").isNumber()).isTrue();

        List<Double> distances = new ArrayList<>();
        for (JsonNode item : node) {
            if (item.get("distance").isNumber()) {
                distances.add(item.get("distance").asDouble());
            }
        }
        assertThat(distances).hasSize(2).isSorted();
        assertThat(findByCode(node, "NJSA01").get("distance").isNull()).isTrue();
    }

    @Test
    @DisplayName("无匹配门店时真实链路返回空数组")
    void returns_empty_array_end_to_end() throws Exception {
        when(dealershipMapper.selectPoByMap(anyMap())).thenReturn(List.of());

        String body = mockMvc.perform(get(LIST_PATH).param("serviceType", "A"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(body).isEqualTo("[]");
        verify(dealershipMapper).selectPoByMap(anyMap());
    }

    private JsonNode findByCode(JsonNode node, String code) {
        for (JsonNode item : node) {
            if (code.equals(item.get("code").asText())) {
                return item;
            }
        }
        throw new AssertionError("未返回门店: " + code);
    }

    private DealershipPo po(String code, String name, String address, String lat, String lon) {
        DealershipPo dealershipPo = new DealershipPo();
        dealershipPo.setCode(code);
        dealershipPo.setName(name);
        dealershipPo.setAddress(address);
        dealershipPo.setLat(lat);
        dealershipPo.setLon(lon);
        return dealershipPo;
    }

}
