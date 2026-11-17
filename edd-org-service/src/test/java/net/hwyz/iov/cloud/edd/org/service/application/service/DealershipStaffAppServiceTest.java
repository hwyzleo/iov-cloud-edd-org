package net.hwyz.iov.cloud.edd.org.service.application.service;

import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.DealershipMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.DealershipStaffMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipPo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipStaffPo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import net.hwyz.iov.cloud.framework.web.domain.TreeSelect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * EDD-ORG-DSN-CR-003 / US-001、US-002：门店员工应用服务测试。
 * <p>
 * 覆盖：查询条件映射到员工主数据字段、区域过滤复用门店查询、候选人检索来源为员工主数据、
 * 门店内员工唯一性校验（同门店去重 / 跨门店允许）、员工删除时联动清理关联。
 *
 * @author hwyz_leo
 * @since 2026-11-17
 */
class DealershipStaffAppServiceTest {

    private DealershipMapper dealershipMapper;
    private DealershipStaffMapper dealershipStaffMapper;
    private DealershipStaffAppService appService;

    @BeforeEach
    void setUp() {
        dealershipMapper = mock(DealershipMapper.class);
        dealershipStaffMapper = mock(DealershipStaffMapper.class);
        appService = new DealershipStaffAppService(dealershipMapper, dealershipStaffMapper);
    }

    @Test
    @DisplayName("查询条件映射为员工主数据字段：工号/姓名/手机号模糊匹配")
    @SuppressWarnings("unchecked")
    void searchMapsEmployeeConditions() {
        when(dealershipStaffMapper.selectPoByMap(any())).thenReturn(List.of(staff(1L, "SHDE01", 1001L)));

        List<DealershipStaffPo> result = appService.search(null, null, "SHDE01", "上海",
                "E1001", "张三", "138", null, null);

        assertThat(result).hasSize(1);
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(dealershipStaffMapper).selectPoByMap(captor.capture());
        Map<String, Object> params = captor.getValue();
        assertThat(params.get("dealershipCode")).isEqualTo("SHDE01");
        assertThat(params.get("dealershipName")).isEqualTo("%上海%");
        assertThat(params.get("employeeCode")).isEqualTo("%E1001%");
        assertThat(params.get("employeeName")).isEqualTo("%张三%");
        assertThat(params.get("phone")).isEqualTo("%138%");
        assertThat(params).doesNotContainKeys("userName", "phonenumber");
    }

    @Test
    @DisplayName("区域过滤复用门店查询，返回门店代码集合")
    @SuppressWarnings("unchecked")
    void searchMapsRegionToDealershipList() {
        when(dealershipMapper.selectPoByExample(any(DealershipPo.class)))
                .thenReturn(List.of(DealershipPo.builder().code("SHDE01").build(),
                        DealershipPo.builder().code("SHSA01").build()));
        when(dealershipStaffMapper.selectPoByMap(any())).thenReturn(Collections.emptyList());

        appService.search("R01", null, null, null, null, null, null, null, null);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(dealershipStaffMapper).selectPoByMap(captor.capture());
        assertThat((List<String>) captor.getValue().get("dealershipList"))
                .containsExactly("SHDE01", "SHSA01");
    }

    @Test
    @DisplayName("区域无匹配门店时用空串占位，避免生成全量条件")
    @SuppressWarnings("unchecked")
    void searchUsesEmptyPlaceholderWhenRegionHasNoDealership() {
        when(dealershipMapper.selectPoByExample(any(DealershipPo.class))).thenReturn(Collections.emptyList());
        when(dealershipStaffMapper.selectPoByMap(any())).thenReturn(Collections.emptyList());

        appService.search("R99", null, null, null, null, null, null, null, null);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(dealershipStaffMapper).selectPoByMap(captor.capture());
        assertThat((List<String>) captor.getValue().get("dealershipList")).containsExactly("");
    }

    @Test
    @DisplayName("候选人检索来源为员工主数据：id=员工ID，label=工号, 姓名, 手机")
    void searchEmployeeMapsTreeSelect() {
        when(dealershipStaffMapper.selectEmployeeByKey("%E1%")).thenReturn(List.of(
                EmployeePo.builder().id(1001L).code("E1001").name("张三").phone("13800000000").build()));

        List<TreeSelect> options = appService.searchEmployee("E1");

        assertThat(options).hasSize(1);
        TreeSelect option = options.get(0);
        assertThat(option.getId()).isEqualTo("1001");
        assertThat(option.getLabel()).isEqualTo("E1001, 张三, 13800000000");
        assertThat(option.getType()).isEqualTo("张三");
    }

    @Test
    @DisplayName("候选人检索无关键字时不生成模糊条件（关键字归一为 null）")
    void searchEmployeePassesBlankKey() {
        when(dealershipStaffMapper.selectEmployeeByKey(any())).thenReturn(Collections.emptyList());

        assertThat(appService.searchEmployee("")).isEmpty();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(dealershipStaffMapper).selectEmployeeByKey(captor.capture());
        assertThat(captor.getValue()).isNull();
    }

    @Test
    @DisplayName("同一门店下员工重复关联应拒绝，跨门店允许")
    void checkEmployeeUniqueWithinDealership() {
        when(dealershipStaffMapper.selectPoByExample(any(DealershipStaffPo.class)))
                .thenReturn(List.of(staff(7L, "SHDE01", 1001L)));

        assertThat(appService.checkEmployeeUnique(null, "SHDE01", 1001L)).isFalse();
        assertThat(appService.checkEmployeeUnique(7L, "SHDE01", 1001L)).isTrue();
        assertThat(appService.checkEmployeeUnique(9L, "SHDE01", 1001L)).isFalse();

        // 跨门店：查询条件带门店代码，不同门店不存在该员工
        when(dealershipStaffMapper.selectPoByExample(any(DealershipStaffPo.class)))
                .thenReturn(Collections.emptyList());
        assertThat(appService.checkEmployeeUnique(null, "SHSA01", 1001L)).isTrue();
    }

    @Test
    @DisplayName("缺少门店代码或员工ID时不做重复校验")
    void checkEmployeeUniqueSkipsInvalidInput() {
        assertThat(appService.checkEmployeeUnique(null, null, 1001L)).isTrue();
        assertThat(appService.checkEmployeeUnique(null, "SHDE01", null)).isTrue();
        verify(dealershipStaffMapper, never()).selectPoByExample(any(DealershipStaffPo.class));
    }

    @Test
    @DisplayName("按员工ID查询门店员工关联，支持一个员工隶属多个门店")
    void getDealershipStaffByEmployeeIdReturnsMultiple() {
        when(dealershipStaffMapper.selectPoByEmployeeId(1001L))
                .thenReturn(List.of(staff(1L, "SHDE01", 1001L), staff(2L, "SHSA01", 1001L)));

        List<DealershipStaffPo> result = appService.getDealershipStaffByEmployeeId(1001L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(DealershipStaffPo::getDealershipCode)
                .containsExactly("SHDE01", "SHSA01");
    }

    @Test
    @DisplayName("员工删除联动：按员工ID批量清理门店员工关联")
    void deleteDealershipStaffByEmployeeIds() {
        when(dealershipStaffMapper.deletePoByEmployeeIds(Arrays.asList(1001L, 1002L))).thenReturn(3);

        assertThat(appService.deleteDealershipStaffByEmployeeIds(Arrays.asList(1001L, 1002L))).isEqualTo(3);
        verify(dealershipStaffMapper).deletePoByEmployeeIds(Arrays.asList(1001L, 1002L));
    }

    @Test
    @DisplayName("员工删除联动：无员工ID时直接返回0，不访问数据库")
    void deleteDealershipStaffByEmptyEmployeeIds() {
        assertThat(appService.deleteDealershipStaffByEmployeeIds(null)).isZero();
        assertThat(appService.deleteDealershipStaffByEmployeeIds(Collections.emptyList())).isZero();
        verify(dealershipStaffMapper, never()).deletePoByEmployeeIds(any());
    }

    private DealershipStaffPo staff(Long id, String dealershipCode, Long employeeId) {
        return DealershipStaffPo.builder()
                .id(id)
                .dealershipCode(dealershipCode)
                .employeeId(employeeId)
                .build();
    }
}
