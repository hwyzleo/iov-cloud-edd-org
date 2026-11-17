package net.hwyz.iov.cloud.edd.org.service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.DealershipMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.mapper.DealershipStaffMapper;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipPo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipStaffPo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import net.hwyz.iov.cloud.framework.common.util.ParamHelper;
import net.hwyz.iov.cloud.framework.common.util.StrUtil;
import net.hwyz.iov.cloud.framework.web.domain.TreeSelect;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 销售门店员工应用服务类
 *
 * @author hwyz_leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DealershipStaffAppService {

    private final DealershipMapper dealershipMapper;
    private final DealershipStaffMapper dealershipStaffMapper;

    /**
     * 查询门店员工信息
     *
     * @param regionCode     大区代码
     * @param areaCode       小区代码
     * @param dealershipCode 门店代码
     * @param dealershipName 门店名称
     * @param employeeCode   员工工号
     * @param employeeName   员工姓名
     * @param phone          员工手机号
     * @param beginTime      开始时间
     * @param endTime        结束时间
     * @return 门店员工列表
     */
    public List<DealershipStaffPo> search(String regionCode, String areaCode, String dealershipCode, String dealershipName,
                                          String employeeCode, String employeeName, String phone, Date beginTime, Date endTime) {
        Map<String, Object> map = new HashMap<>();
        if (StrUtil.isNotBlank(regionCode)) {
            List<DealershipPo> regionDealershipList = dealershipMapper.selectPoByExample(DealershipPo.builder().regionCode(regionCode).build());
            List<String> regionDealershipCodeList = new java.util.ArrayList<>(regionDealershipList.stream().map(DealershipPo::getCode).toList());
            if (regionDealershipCodeList.isEmpty()) {
                regionDealershipCodeList.add("");
            }
            map.put("dealershipList", regionDealershipCodeList);
        }
        if (StrUtil.isNotBlank(areaCode)) {
            List<DealershipPo> areaDealershipList = dealershipMapper.selectPoByExample(DealershipPo.builder().areaCode(areaCode).build());
            List<String> areaDealershipCodeList = new java.util.ArrayList<>(areaDealershipList.stream().map(DealershipPo::getCode).toList());
            if (areaDealershipCodeList.isEmpty()) {
                areaDealershipCodeList.add("");
            }
            map.put("dealershipList", areaDealershipCodeList);
        }
        map.put("dealershipCode", dealershipCode);
        map.put("dealershipName", ParamHelper.fuzzyQueryParam(dealershipName));
        map.put("employeeCode", ParamHelper.fuzzyQueryParam(employeeCode));
        map.put("employeeName", ParamHelper.fuzzyQueryParam(employeeName));
        map.put("phone", ParamHelper.fuzzyQueryParam(phone));
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return dealershipStaffMapper.selectPoByMap(map);
    }

    /**
     * 根据关键字查询候选员工（来源为 ORG 员工主数据）
     *
     * @param key 关键字（工号/姓名/手机号）
     * @return 员工下拉树列表
     */
    public List<TreeSelect> searchEmployee(String key) {
        List<EmployeePo> employees = dealershipStaffMapper.selectEmployeeByKey(ParamHelper.fuzzyQueryParam(key));
        return employees.stream()
                .map(employee -> {
                    TreeSelect treeSelect = new TreeSelect();
                    treeSelect.setId(employee.getId().toString());
                    treeSelect.setLabel(employee.getCode() + ", " + employee.getName() + ", " + employee.getPhone());
                    treeSelect.setType(employee.getName());
                    return treeSelect;
                })
                .collect(Collectors.toList());
    }

    /**
     * 检查同一门店下员工是否重复关联
     *
     * @param dealershipStaffId 门店员工ID
     * @param dealershipCode    门店代码
     * @param employeeId        员工ID
     * @return 结果
     */
    public Boolean checkEmployeeUnique(Long dealershipStaffId, String dealershipCode, Long employeeId) {
        if (StrUtil.isBlank(dealershipCode) || employeeId == null) {
            return true;
        }
        List<DealershipStaffPo> existing = dealershipStaffMapper.selectPoByExample(DealershipStaffPo.builder()
                .dealershipCode(dealershipCode)
                .employeeId(employeeId)
                .build());
        if (existing == null || existing.isEmpty()) {
            return true;
        }
        if (dealershipStaffId == null) {
            return false;
        }
        return existing.stream().anyMatch(po -> dealershipStaffId.equals(po.getId()));
    }

    /**
     * 根据主键ID获取门店员工
     *
     * @param id 主键ID
     * @return 门店员工
     */
    public DealershipStaffPo getDealershipStaffById(Long id) {
        return dealershipStaffMapper.selectPoById(id);
    }

    /**
     * 根据员工ID获取门店员工关联（一个员工可隶属多个门店）
     *
     * @param employeeId 员工ID
     * @return 门店员工关联列表
     */
    public List<DealershipStaffPo> getDealershipStaffByEmployeeId(Long employeeId) {
        return dealershipStaffMapper.selectPoByEmployeeId(employeeId);
    }

    /**
     * 新增门店员工
     *
     * @param dealershipStaff 门店员工
     * @return 结果
     */
    public int createDealershipStaff(DealershipStaffPo dealershipStaff) {
        return dealershipStaffMapper.insertPo(dealershipStaff);
    }

    /**
     * 修改门店员工
     *
     * @param dealershipStaff 门店员工
     * @return 结果
     */
    public int modifyDealershipStaff(DealershipStaffPo dealershipStaff) {
        return dealershipStaffMapper.updatePo(dealershipStaff);
    }

    /**
     * 批量删除门店员工
     *
     * @param ids 门店员工ID数组
     * @return 结果
     */
    public int deleteDealershipStaffByIds(Long[] ids) {
        return dealershipStaffMapper.batchPhysicalDeletePo(ids);
    }

    /**
     * 按员工ID批量删除门店员工关联（员工删除时联动清理）
     *
     * @param employeeIds 员工ID集合
     * @return 结果
     */
    public int deleteDealershipStaffByEmployeeIds(List<Long> employeeIds) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return 0;
        }
        return dealershipStaffMapper.deletePoByEmployeeIds(employeeIds);
    }

}
