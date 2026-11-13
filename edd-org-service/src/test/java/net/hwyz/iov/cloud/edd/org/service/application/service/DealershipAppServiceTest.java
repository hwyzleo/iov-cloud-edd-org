package net.hwyz.iov.cloud.edd.org.service.application.service;

import net.hwyz.iov.cloud.edd.org.api.enums.DealershipServiceType;
import net.hwyz.iov.cloud.edd.org.api.vo.Dealership;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.DealershipMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipPo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * EDD-ORG-DSN-CR-002 / US-001：门店列表筛选、距离与排序语义测试。
 * <p>
 * 该测试锁定 {@link DealershipAppService#getDealershipList} 的既有语义（Mobile 与内部接口共用同一实现），
 * 确保新增内部接口不引入第二套筛选规则。
 *
 * @author hwyz_leo
 * @since 2026-11-13
 */
@ExtendWith(MockitoExtension.class)
class DealershipAppServiceTest {

    @Mock
    private DealershipMapper dealershipMapper;

    @InjectMocks
    private DealershipAppService dealershipAppService;

    @Test
    @DisplayName("服务类型与省市区条件原样进入查询，经纬度不参与 SQL 过滤")
    void passes_service_type_and_region_filters_to_mapper() {
        when(dealershipMapper.selectPoByMap(anyMap())).thenReturn(List.of());

        dealershipAppService.getDealershipList(DealershipServiceType.D, "31", "3101", "310105",
                "121.473962", "31.234567");

        Map<String, Object> query = capturedQuery();
        assertThat(query).containsEntry("serviceType", "D")
                .containsEntry("provinceCode", "31")
                .containsEntry("cityCode", "3101")
                .containsEntry("countyCode", "310105")
                .containsEntry("requestLon", "121.473962")
                .containsEntry("requestLat", "31.234567");
    }

    @Test
    @DisplayName("S/D/A 三种服务类型均以枚举名作为查询条件")
    void encodes_each_service_type_by_enum_name() {
        when(dealershipMapper.selectPoByMap(anyMap())).thenReturn(List.of());

        for (DealershipServiceType serviceType : DealershipServiceType.values()) {
            dealershipAppService.getDealershipList(serviceType, null, null, null, null, null);
            assertThat(capturedQuery()).containsEntry("serviceType", serviceType.name());
        }
    }

    @Test
    @DisplayName("请求与门店坐标齐备时按千米计算距离，最多两位小数")
    void computes_distance_when_both_coordinates_available() {
        DealershipPo samePoint = po("SHDE01", "31.234567", "121.473962");
        DealershipPo farPoint = po("SZSA01", "31.5656", "120.1212");
        when(dealershipMapper.selectPoByMap(anyMap())).thenReturn(List.of(farPoint, samePoint));

        List<Dealership> result = dealershipAppService.getDealershipList(DealershipServiceType.S, null, null, null,
                "121.473962", "31.234567");

        assertThat(result).hasSize(2);
        assertThat(result).allSatisfy(dealership -> {
            assertThat(dealership.getDistance()).isNotNull().isGreaterThanOrEqualTo(0.0);
            assertThat(Math.round(dealership.getDistance() * 100) / 100.0)
                    .isEqualTo(dealership.getDistance());
        });
        assertThat(distanceOf(result, "SHDE01")).isEqualTo(0.0);
        assertThat(distanceOf(result, "SZSA01")).isGreaterThan(1.0);
    }

    @Test
    @DisplayName("请求坐标或门店坐标缺失时 distance 为空，门店结果仍保留")
    void keeps_null_distance_when_coordinates_incomplete() {
        DealershipPo noCoordinate = po("NJSA01", null, null);
        DealershipPo blankCoordinate = po("WHDE01", " ", " ");
        DealershipPo withCoordinate = po("SHSA01", "31.656565", "121.545454");
        when(dealershipMapper.selectPoByMap(anyMap()))
                .thenReturn(List.of(noCoordinate, blankCoordinate, withCoordinate));

        List<Dealership> noRequestCoordinate = dealershipAppService.getDealershipList(DealershipServiceType.S,
                null, null, null, null, null);
        assertThat(noRequestCoordinate).hasSize(3);
        assertThat(noRequestCoordinate).allSatisfy(dealership -> assertThat(dealership.getDistance()).isNull());

        List<Dealership> withRequestCoordinate = dealershipAppService.getDealershipList(DealershipServiceType.S,
                null, null, null, "121.473962", "31.234567");
        assertThat(distanceOf(withRequestCoordinate, "NJSA01")).isNull();
        assertThat(distanceOf(withRequestCoordinate, "WHDE01")).isNull();
        assertThat(distanceOf(withRequestCoordinate, "SHSA01")).isNotNull();
    }

    @Test
    @DisplayName("有距离的门店按距离升序排列（空距离位置沿用既有行为，本测试不作约定）")
    void sorts_stores_by_distance_ascending() {
        when(dealershipMapper.selectPoByMap(anyMap())).thenReturn(List.of(
                po("FAR01", "31.656565", "121.545454"),
                po("NONE01", null, null),
                po("NEAR01", "31.234567", "121.473962"),
                po("MID01", "31.300000", "121.500000")));

        List<Dealership> result = dealershipAppService.getDealershipList(DealershipServiceType.S, null, null, null,
                "121.473962", "31.234567");

        List<Double> distances = result.stream().map(Dealership::getDistance).filter(distance -> distance != null)
                .toList();
        assertThat(distances).hasSize(3).isSorted();
        assertThat(result.get(0).getDistance()).isNotNull();
        assertThat(distanceOf(result, "NEAR01")).isEqualTo(0.0);
    }

    @Test
    @DisplayName("无匹配门店返回空列表")
    void returns_empty_list_when_no_dealership_matched() {
        when(dealershipMapper.selectPoByMap(anyMap())).thenReturn(List.of());

        assertThat(dealershipAppService.getDealershipList(DealershipServiceType.A, "99", null, null, null, null))
                .isEmpty();
    }

    @Test
    @DisplayName("列表查询为只读操作，仅调用 selectPoByMap")
    void list_query_is_read_only() {
        when(dealershipMapper.selectPoByMap(anyMap())).thenReturn(List.of(po("SHSA01", null, null)));

        dealershipAppService.getDealershipList(DealershipServiceType.S, null, null, null, null, null);

        verify(dealershipMapper).selectPoByMap(anyMap());
        verifyNoMoreInteractions(dealershipMapper);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> capturedQuery() {
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(dealershipMapper, org.mockito.Mockito.atLeastOnce()).selectPoByMap(captor.capture());
        return captor.getValue();
    }

    private Double distanceOf(List<Dealership> dealerships, String code) {
        return dealerships.stream().filter(dealership -> code.equals(dealership.getCode())).findFirst()
                .orElseThrow(() -> new AssertionError("未返回门店: " + code)).getDistance();
    }

    private DealershipPo po(String code, String lat, String lon) {
        DealershipPo dealershipPo = new DealershipPo();
        dealershipPo.setCode(code);
        dealershipPo.setName(code + "-门店全称");
        dealershipPo.setAddress(code + "-门店地址");
        dealershipPo.setLat(lat);
        dealershipPo.setLon(lon);
        return dealershipPo;
    }

}
