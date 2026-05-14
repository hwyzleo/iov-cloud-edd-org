package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.mpt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipMpt;
import net.hwyz.iov.cloud.edd.org.api.vo.OrgMpt;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler.DealershipMptAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.service.DealershipAppService;
import net.hwyz.iov.cloud.edd.org.service.application.service.OrgAppService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipPo;
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
 * 门店相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpt/dealership")
public class MptDealershipController extends BaseController {

    private final OrgAppService orgAppService;
    private final DealershipAppService dealershipAppService;

    /**
     * 分页查询门店信息
     *
     * @param dealership 门店信息
     * @return 门店信息列表
     */
    @RequiresPermissions("org:dealership:info:list")
    @GetMapping(value = "/list")
    public ApiResponse<PageResult<DealershipMpt>> list(DealershipMpt dealership) {
        log.info("管理后台用户[{}]分页查询门店信息", SecurityUtils.getUsername());
        startPage();
        List<DealershipPo> dealershipPoList = dealershipAppService.search(dealership.getCode(), dealership.getName(),
                dealership.getRegionCode(), dealership.getAreaCode(), getBeginTime(dealership), getEndTime(dealership));
        return ApiResponse.ok(getPageResult(PageUtil.convert(dealershipPoList, DealershipMptAssembler.INSTANCE::fromPo)));
    }

    /**
     * 导出门店信息
     *
     * @param response   响应
     * @param dealership 门店信息
     */
    @Log(title = "门店管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("org:dealership:info:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, DealershipMpt dealership) {
        log.info("管理后台用户[{}]导出门店信息", SecurityUtils.getUsername());
    }

    /**
     * 根据门店ID获取门店信息
     *
     * @param dealershipId 门店ID
     * @return 门店信息
     */
    @RequiresPermissions("org:dealership:info:query")
    @GetMapping(value = "/{dealershipId}")
    public ApiResponse<DealershipMpt> getInfo(@PathVariable Long dealershipId) {
        log.info("管理后台用户[{}]根据门店ID[{}]获取门店信息", SecurityUtils.getUsername(), dealershipId);
        DealershipPo dealershipPo = dealershipAppService.getDealershipById(dealershipId);
        return ApiResponse.ok(DealershipMptAssembler.INSTANCE.fromPo(dealershipPo));
    }

    /**
     * 新增门店信息
     *
     * @param dealership 门店信息
     * @return 结果
     */
    @Log(title = "门店管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("org:dealership:info:add")
    @PostMapping
    public ApiResponse<Integer> add(@Validated @RequestBody DealershipMpt dealership) {
        log.info("管理后台用户[{}]新增门店信息[{}]", SecurityUtils.getUsername(), dealership.getCode());
        if (!dealershipAppService.checkCodeUnique(dealership.getId(), dealership.getCode())) {
            return ApiResponse.fail("新增门店'" + dealership.getCode() + "'失败，门店代码已存在");
        }
        DealershipPo dealershipPo = DealershipMptAssembler.INSTANCE.toPo(dealership);
        dealershipPo.setCreateBy(SecurityUtils.getUserId().toString());
        return ApiResponse.ok(dealershipAppService.createDealership(dealershipPo));
    }

    /**
     * 修改保存门店信息
     *
     * @param dealership 门店信息
     * @return 结果
     */
    @Log(title = "门店管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("org:dealership:info:edit")
    @PutMapping
    public ApiResponse<Integer> edit(@Validated @RequestBody DealershipMpt dealership) {
        log.info("管理后台用户[{}]修改保存门店信息[{}]", SecurityUtils.getUsername(), dealership.getCode());
        if (!dealershipAppService.checkCodeUnique(dealership.getId(), dealership.getCode())) {
            return ApiResponse.fail("修改保存门店'" + dealership.getCode() + "'失败，门店代码已存在");
        }
        DealershipPo dealershipPo = DealershipMptAssembler.INSTANCE.toPo(dealership);
        dealershipPo.setModifyBy(SecurityUtils.getUserId().toString());
        return ApiResponse.ok(dealershipAppService.modifyDealership(dealershipPo));
    }

    /**
     * 删除门店信息
     *
     * @param dealershipIds 门店ID数组
     * @return 结果
     */
    @Log(title = "门店管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("org:dealership:info:remove")
    @DeleteMapping("/{dealershipIds}")
    public ApiResponse<Integer> remove(@PathVariable Long[] dealershipIds) {
        log.info("管理后台用户[{}]删除门店信息[{}]", SecurityUtils.getUsername(), dealershipIds);
        return ApiResponse.ok(dealershipAppService.deleteDealershipByIds(dealershipIds));
    }

    /**
     * 获取组织树结构
     *
     * @param org 组织架构
     * @return 组织树结构
     */
    @RequiresPermissions("org:dealership:info:list")
    @GetMapping(value = "/orgTree")
    public ApiResponse<List<TreeSelect>> orgTree(OrgMpt org) {
        log.info("管理后台用户[{}]获取组织树结构", SecurityUtils.getUsername());
        return ApiResponse.ok(orgAppService.selectOrgTreeList(org.getCode(), org.getName(), org.getOrgType(), org.getRegionCode()));
    }
}
