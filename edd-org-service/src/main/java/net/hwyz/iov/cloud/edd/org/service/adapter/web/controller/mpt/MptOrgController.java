package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.mpt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.OrgMpt;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler.OrgMptAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.service.OrgAppService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.OrgPo;
import net.hwyz.iov.cloud.framework.audit.annotation.Log;
import net.hwyz.iov.cloud.framework.audit.enums.BusinessType;
import net.hwyz.iov.cloud.framework.common.bean.ApiResponse;
import net.hwyz.iov.cloud.framework.common.bean.PageResult;
import net.hwyz.iov.cloud.framework.common.util.StrUtil;
import net.hwyz.iov.cloud.framework.security.annotation.RequiresPermissions;
import net.hwyz.iov.cloud.framework.security.util.SecurityUtils;
import net.hwyz.iov.cloud.framework.web.controller.BaseController;
import net.hwyz.iov.cloud.framework.web.util.PageUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织架构相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpt/org")
public class MptOrgController extends BaseController {

    private final OrgAppService orgAppService;

    /**
     * 查询组织架构
     *
     * @param org 组织架构
     * @return 组织架构列表
     */
    @RequiresPermissions("org:dealership:org:list")
    @GetMapping(value = "/list")
    public ApiResponse<PageResult<OrgMpt>> list(OrgMpt org) {
        log.info("管理后台用户[{}]查询组织架构", SecurityUtils.getUsername());
        List<OrgPo> platformPoList = orgAppService.search(org.getCode(), org.getName(), org.getOrgType(), null,
                getBeginTime(org), getEndTime(org));
        return ApiResponse.ok(getPageResult(PageUtil.convert(platformPoList, OrgMptAssembler.INSTANCE::fromPo)));
    }

    /**
     * 查询组织架构（排除节点）
     *
     * @param orgId 组织架构ID
     * @return 组织架构列表
     */
    @RequiresPermissions("org:dealership:org:list")
    @GetMapping(value = "/list/exclude/{orgId}")
    public ApiResponse<List<OrgMpt>> listExcludeChild(@PathVariable Long orgId) {
        log.info("管理后台用户[{}]查询组织架构（排除节点[{}]）", SecurityUtils.getUsername(), orgId);
        List<OrgPo> platformPoList = orgAppService.search(null, null, null, null,
                null, null);
        List<OrgMpt> dealershipMptList = OrgMptAssembler.INSTANCE.fromPoList(platformPoList);
        dealershipMptList.removeIf(d -> d.getId().longValue() == orgId || ArrayUtils.contains(StrUtil.splitToArray(d.getAncestors(), ","), orgId + ""));
        return ApiResponse.ok(dealershipMptList);
    }

    /**
     * 导出组织架构
     *
     * @param response 响应
     * @param org      组织架构
     */
    @Log(title = "组织架构管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("org:dealership:org:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrgMpt org) {
        log.info("管理后台用户[{}]导出组织架构", SecurityUtils.getUsername());
    }

    /**
     * 根据组织架构ID获取组织架构
     *
     * @param orgId 组织架构ID
     * @return 组织架构
     */
    @RequiresPermissions("org:dealership:org:query")
    @GetMapping(value = "/{orgId}")
    public ApiResponse<OrgMpt> getInfo(@PathVariable Long orgId) {
        log.info("管理后台用户[{}]根据组织架构ID[{}]获取组织架构", SecurityUtils.getUsername(), orgId);
        OrgPo orgPo = orgAppService.getOrgById(orgId);
        return ApiResponse.ok(OrgMptAssembler.INSTANCE.fromPo(orgPo));
    }

    /**
     * 新增组织架构
     *
     * @param org 组织架构
     * @return 结果
     */
    @Log(title = "组织架构管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("org:dealership:org:add")
    @PostMapping
    public ApiResponse<Integer> add(@Validated @RequestBody OrgMpt org) {
        log.info("管理后台用户[{}]新增组织架构[{}]", SecurityUtils.getUsername(), org.getCode());
        if (!orgAppService.checkCodeUnique(org.getId(), org.getCode())) {
            return ApiResponse.fail("新增组织架构'" + org.getCode() + "'失败，组织架构代码已存在");
        }
        OrgPo orgPo = OrgMptAssembler.INSTANCE.toPo(org);
        orgPo.setCreateBy(SecurityUtils.getUserId().toString());
        return ApiResponse.ok(orgAppService.createOrg(orgPo));
    }

    /**
     * 修改保存组织架构
     *
     * @param org 组织架构
     * @return 结果
     */
    @Log(title = "组织架构管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("org:dealership:org:edit")
    @PutMapping
    public ApiResponse<Integer> edit(@Validated @RequestBody OrgMpt org) {
        log.info("管理后台用户[{}]修改保存组织架构[{}]", SecurityUtils.getUsername(), org.getCode());
        if (!orgAppService.checkCodeUnique(org.getId(), org.getCode())) {
            return ApiResponse.fail("修改保存组织架构'" + org.getCode() + "'失败，组织架构代码已存在");
        }
        OrgPo orgPo = OrgMptAssembler.INSTANCE.toPo(org);
        orgPo.setModifyBy(SecurityUtils.getUserId().toString());
        return ApiResponse.ok(orgAppService.modifyOrg(orgPo));
    }

    /**
     * 删除组织架构
     *
     * @param orgIds 组织架构ID数组
     * @return 结果
     */
    @Log(title = "组织架构管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("org:dealership:org:remove")
    @DeleteMapping("/{orgIds}")
    public ApiResponse<Integer> remove(@PathVariable Long[] orgIds) {
        log.info("管理后台用户[{}]删除组织架构[{}]", SecurityUtils.getUsername(), orgIds);
        return ApiResponse.ok(orgAppService.deleteOrgByIds(orgIds));
    }

}
