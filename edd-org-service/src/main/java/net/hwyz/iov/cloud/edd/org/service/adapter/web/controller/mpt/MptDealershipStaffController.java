package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.mpt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipStaffMpt;
import net.hwyz.iov.cloud.edd.org.api.vo.OrgMpt;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler.DealershipStaffMptAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.service.DealershipAppService;
import net.hwyz.iov.cloud.edd.org.service.application.service.DealershipStaffAppService;
import net.hwyz.iov.cloud.edd.org.service.application.service.OrgAppService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipStaffPo;
import net.hwyz.iov.cloud.framework.audit.annotation.Log;
import net.hwyz.iov.cloud.framework.audit.enums.BusinessType;
import net.hwyz.iov.cloud.framework.common.bean.ApiResponse;
import net.hwyz.iov.cloud.framework.common.bean.PageResult;
import net.hwyz.iov.cloud.framework.security.annotation.RequiresPermissions;
import net.hwyz.iov.cloud.framework.security.util.SecurityUtils;
import net.hwyz.iov.cloud.framework.web.controller.BaseController;
import net.hwyz.iov.cloud.framework.web.domain.TreeSelect;
import net.hwyz.iov.cloud.framework.web.util.PageUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 门店员工相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpt/dealershipStaff")
public class MptDealershipStaffController extends BaseController {

    private final OrgAppService orgAppService;
    private final DealershipAppService dealershipAppService;
    private final DealershipStaffAppService dealershipStaffAppService;

    /**
     * 查询门店员工
     *
     * @param dealershipStaff 门店员工
     * @return 门店员工列表
     */
    @RequiresPermissions("org:dealership:staff:list")
    @GetMapping(value = "/list")
    public ApiResponse<PageResult<DealershipStaffMpt>> list(DealershipStaffMpt dealershipStaff) {
        log.info("管理后台用户[{}]查询门店员工", SecurityUtils.getUsername());
        List<DealershipStaffPo> dealershipStaffPoList = dealershipStaffAppService.search(dealershipStaff.getRegionCode(),
                dealershipStaff.getAreaCode(), dealershipStaff.getDealershipCode(), dealershipStaff.getDealershipName(),
                dealershipStaff.getUserName(), dealershipStaff.getPhonenumber(), getBeginTime(dealershipStaff),
                getEndTime(dealershipStaff));
        return ApiResponse.ok(getPageResult(PageUtil.convert(dealershipStaffPoList, DealershipStaffMptAssembler.INSTANCE::fromPo)));
    }

    /**
     * 导出门店员工
     *
     * @param response        响应
     * @param dealershipStaff 门店员工
     */
    @Log(title = "门店员工管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("org:dealership:staff:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, DealershipStaffMpt dealershipStaff) {
        log.info("管理后台用户[{}]导出门店员工", SecurityUtils.getUsername());
    }

    /**
     * 根据门店员工ID获取门店员工
     *
     * @param dealershipStaffId 门店员工ID
     * @return 门店员工
     */
    @RequiresPermissions("org:dealership:staff:query")
    @GetMapping(value = "/{dealershipStaffId}")
    public ApiResponse<DealershipStaffMpt> getInfo(@PathVariable Long dealershipStaffId) {
        log.info("管理后台用户[{}]根据门店员工ID[{}]获取门店员工", SecurityUtils.getUsername(), dealershipStaffId);
        DealershipStaffPo dealershipStaffPo = dealershipStaffAppService.getDealershipStaffById(dealershipStaffId);
        return ApiResponse.ok(DealershipStaffMptAssembler.INSTANCE.fromPo(dealershipStaffPo));
    }

    /**
     * 新增门店员工
     *
     * @param dealershipStaff 门店员工
     * @return 结果
     */
    @Log(title = "门店员工管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("org:dealership:staff:add")
    @PostMapping
    public ApiResponse<Integer> add(@Validated @RequestBody DealershipStaffMpt dealershipStaff) {
        log.info("管理后台用户[{}]新增门店员工[{}]", SecurityUtils.getUsername(), dealershipStaff.getUserName());
        if (!dealershipStaffAppService.checkUserIdUnique(dealershipStaff.getId(), dealershipStaff.getUserId())) {
            return ApiResponse.fail("新增门店员工'" + dealershipStaff.getUserName() + "'失败，用户ID已存在");
        }
        DealershipStaffPo dealershipStaffPo = DealershipStaffMptAssembler.INSTANCE.toPo(dealershipStaff);
        dealershipStaffPo.setCreateBy(SecurityUtils.getUserId().toString());
        return ApiResponse.ok(dealershipStaffAppService.createDealershipStaff(dealershipStaffPo));
    }

    /**
     * 修改保存门店员工
     *
     * @param dealershipStaff 门店员工
     * @return 结果
     */
    @Log(title = "门店员工管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("org:dealership:staff:edit")
    @PutMapping
    public ApiResponse<Integer> edit(@Validated @RequestBody DealershipStaffMpt dealershipStaff) {
        log.info("管理后台用户[{}]修改保存门店员工[{}]", SecurityUtils.getUsername(), dealershipStaff.getUserName());
        if (!dealershipStaffAppService.checkUserIdUnique(dealershipStaff.getId(), dealershipStaff.getUserId())) {
            return ApiResponse.fail("修改保存门店员工'" + dealershipStaff.getUserName() + "'失败，用户ID已存在");
        }
        DealershipStaffPo dealershipStaffPo = DealershipStaffMptAssembler.INSTANCE.toPo(dealershipStaff);
        dealershipStaffPo.setModifyBy(SecurityUtils.getUserId().toString());
        return ApiResponse.ok(dealershipStaffAppService.modifyDealershipStaff(dealershipStaffPo));
    }

    /**
     * 删除门店员工
     *
     * @param dealershipStaffIds 门店员工ID数组
     * @return 结果
     */
    @Log(title = "门店员工管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("org:dealership:staff:remove")
    @DeleteMapping("/{dealershipStaffIds}")
    public ApiResponse<Integer> remove(@PathVariable Long[] dealershipStaffIds) {
        log.info("管理后台用户[{}]删除门店员工[{}]", SecurityUtils.getUsername(), dealershipStaffIds);
        return ApiResponse.ok(dealershipStaffAppService.deleteDealershipStaffByIds(dealershipStaffIds));
    }

    /**
     * 获取组织树结构
     *
     * @param org 组织架构
     * @return 组织树结构
     */
    @RequiresPermissions("org:dealership:staff:list")
    @GetMapping(value = "/orgTree")
    public ApiResponse<List<TreeSelect>> orgTree(OrgMpt org) {
        log.info("管理后台用户[{}]获取组织树结构", SecurityUtils.getUsername());
        return ApiResponse.ok(orgAppService.selectOrgTreeList(org.getCode(), org.getName(), org.getOrgType(), org.getRegionCode()));
    }

    /**
     * 查询员工
     *
     * @param key 关键词
     * @return 员工列表
     */
    @RequiresPermissions("org:dealership:staff:list")
    @GetMapping(value = "/searchUser")
    public ApiResponse<List<TreeSelect>> searchUser(@RequestParam(required = false) String key) {
        log.info("管理后台用户[{}]查询员工[{}]", SecurityUtils.getUsername(), key);
        return ApiResponse.ok(dealershipStaffAppService.searchUser(key));
    }

    /**
     * 查询门店
     *
     * @param key 关键词
     * @return 员工列表
     */
    @RequiresPermissions("org:dealership:staff:list")
    @GetMapping(value = "/searchDealership")
    public ApiResponse<List<TreeSelect>> searchDealership(String key) {
        log.info("管理后台用户[{}]查询门店[{}]", SecurityUtils.getUsername(), key);
        return ApiResponse.ok(dealershipAppService.searchByKey(key));
    }
}
