package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.mpt;

import net.hwyz.iov.cloud.edd.org.api.vo.DealershipStaffMpt;
import net.hwyz.iov.cloud.edd.org.service.application.service.DealershipStaffAppService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipStaffPo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * EDD-ORG-DSN-CR-003 验收缺陷修复：门店员工导出的契约测试。
 * <p>
 * 导出与列表同口径筛选，表头必须为「门店代码/门店名称/工号/姓名/手机号码/备注/创建时间」，
 * 不得出现 MPT 账号字段；备注列必须真实写入（历史上备注未持久化）。
 *
 * @author hwyz_leo
 * @since 2026-11-17
 */
class MptDealershipStaffExportTest {

    private static final List<String> EXPECTED_HEADERS = List.of(
            "门店代码", "门店名称", "工号", "姓名", "手机号码", "备注", "创建时间");

    private DealershipStaffAppService dealershipStaffAppService;
    private MptDealershipStaffController controller;

    @BeforeEach
    void setUp() {
        dealershipStaffAppService = mock(DealershipStaffAppService.class);
        controller = new MptDealershipStaffController(null, null, dealershipStaffAppService);
    }

    @Test
    @DisplayName("导出门店员工：表头为员工事实字段，且带出备注")
    void exportWritesEmployeeHeaders() throws Exception {
        DealershipStaffPo po = DealershipStaffPo.builder()
                .id(1L)
                .dealershipCode("SHDE01")
                .employeeId(1001L)
                .employeeCode("E1001")
                .employeeName("张三")
                .phone("13900000000")
                .description("交付骨干")
                .build();
        po.setCreateTime(new Date(1794575931000L));
        when(dealershipStaffAppService.search(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(po));

        MockHttpServletResponse response = new MockHttpServletResponse();
        controller.export(response, DealershipStaffMpt.builder().dealershipCode("SHDE01").build());

        byte[] content = response.getContentAsByteArray();
        assertTrue(content.length > 0, "导出内容不能为空");

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(content))) {
            Sheet sheet = workbook.getSheetAt(0);
            List<String> headers = new ArrayList<>();
            Row headerRow = sheet.getRow(0);
            headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));
            assertEquals(EXPECTED_HEADERS, headers, "表头必须为员工事实字段");
            assertTrue(!headers.contains("用户账号"), "不得导出 MPT 账号字段");

            Row dataRow = sheet.getRow(1);
            assertEquals("SHDE01", dataRow.getCell(0).getStringCellValue());
            assertEquals("E1001", dataRow.getCell(2).getStringCellValue());
            assertEquals("张三", dataRow.getCell(3).getStringCellValue());
            assertEquals("13900000000", dataRow.getCell(4).getStringCellValue());
            assertEquals("交付骨干", dataRow.getCell(5).getStringCellValue(), "备注必须写入导出文件");
        }

        verify(dealershipStaffAppService).search(null, null, "SHDE01", null, null, null, null, null, null);
    }

    @Test
    @DisplayName("导出门店员工：无数据时仅输出表头")
    void exportWithEmptyResult() throws Exception {
        when(dealershipStaffAppService.search(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        MockHttpServletResponse response = new MockHttpServletResponse();
        controller.export(response, new DealershipStaffMpt());

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(response.getContentAsByteArray()))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(1, sheet.getPhysicalNumberOfRows(), "无数据时只应有表头行");
        }
    }
}
