package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.mpt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.DepartmentMpt;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler.DepartmentMptAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.dto.query.DepartmentQuery;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.DepartmentDto;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.DepartmentTreeDto;
import net.hwyz.iov.cloud.edd.org.service.application.service.DepartmentAppService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 部门相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/mpt/department/v1")
public class MptDepartmentController extends BaseController {

    private final DepartmentAppService departmentAppService;

    /**
     * 查询部门列表
     *
     * @param department 部门
     * @return 部门列表
     */
    @RequiresPermissions("org:department:list")
    @GetMapping(value = "/list")
    public ApiResponse<PageResult<DepartmentMpt>> list(DepartmentMpt department) {
        log.info("管理后台用户[{}]查询部门列表", SecurityUtils.getUsername());
        DepartmentQuery query = DepartmentQuery.builder()
                .code(department.getCode())
                .name(department.getName())
                .parentId(department.getParentId())
                .beginTime(getBeginTime(department) != null ? getBeginTime(department).toInstant() : null)
                .endTime(getEndTime(department) != null ? getEndTime(department).toInstant() : null)
                .build();
        List<DepartmentDto> dtoList = departmentAppService.searchDepartments(query);
        return ApiResponse.ok(getPageResult(PageUtil.convert(dtoList, DepartmentMptAssembler.INSTANCE::fromDto)));
    }

    /**
     * 查询部门树
     *
     * @param department 部门
     * @return 部门树
     */
    @RequiresPermissions("org:department:list")
    @GetMapping(value = "/tree")
    public ApiResponse<List<DepartmentTreeDto>> tree(DepartmentMpt department) {
        log.info("管理后台用户[{}]查询部门树", SecurityUtils.getUsername());
        DepartmentQuery query = DepartmentQuery.builder()
                .code(department.getCode())
                .name(department.getName())
                .parentId(department.getParentId())
                .build();
        return ApiResponse.ok(departmentAppService.getDepartmentTree(query));
    }

    /**
     * 查询部门（排除节点）
     *
     * @param departmentId 部门ID
     * @return 部门列表
     */
    @RequiresPermissions("org:department:list")
    @GetMapping(value = "/list/exclude/{departmentId}")
    public ApiResponse<List<DepartmentMpt>> listExcludeChild(@PathVariable Long departmentId) {
        log.info("管理后台用户[{}]查询部门（排除节点[{}]）", SecurityUtils.getUsername(), departmentId);
        DepartmentQuery query = DepartmentQuery.builder().build();
        List<DepartmentDto> dtoList = departmentAppService.searchDepartments(query);
        List<DepartmentMpt> departmentMptList = DepartmentMptAssembler.INSTANCE.fromDtoList(dtoList);
        departmentMptList.removeIf(d -> d.getId().longValue() == departmentId);
        return ApiResponse.ok(departmentMptList);
    }

    /**
     * 导出部门
     *
     * @param response   响应
     * @param department 部门
     */
    @Log(title = "部门管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("org:department:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, DepartmentMpt department) {
        log.info("管理后台用户[{}]导出部门", SecurityUtils.getUsername());
    }

    /**
     * 根据部门ID获取部门
     *
     * @param departmentId 部门ID
     * @return 部门
     */
    @RequiresPermissions("org:department:query")
    @GetMapping(value = "/{departmentId}")
    public ApiResponse<DepartmentMpt> getInfo(@PathVariable Long departmentId) {
        log.info("管理后台用户[{}]根据部门ID[{}]获取部门", SecurityUtils.getUsername(), departmentId);
        DepartmentDto dto = departmentAppService.getDepartmentById(departmentId);
        return ApiResponse.ok(DepartmentMptAssembler.INSTANCE.fromDto(dto));
    }

    /**
     * 新增部门
     *
     * @param department 部门
     * @return 结果
     */
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("org:department:add")
    @PostMapping
    public ApiResponse<DepartmentMpt> add(@Validated @RequestBody DepartmentMpt department) {
        log.info("管理后台用户[{}]新增部门[{}]", SecurityUtils.getUsername(), department.getName());
        if (department.getCode() != null && !departmentAppService.checkCodeUnique(department.getId(), department.getCode())) {
            return ApiResponse.fail("新增部门'" + department.getCode() + "'失败，部门编码已存在");
        }
        var cmd = DepartmentMptAssembler.INSTANCE.toCreateCmd(department);
        DepartmentDto dto = departmentAppService.createDepartment(cmd);
        return ApiResponse.ok(DepartmentMptAssembler.INSTANCE.fromDto(dto));
    }

    /**
     * 修改保存部门
     *
     * @param department 部门
     * @return 结果
     */
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("org:department:edit")
    @PutMapping
    public ApiResponse<DepartmentMpt> edit(@Validated @RequestBody DepartmentMpt department) {
        log.info("管理后台用户[{}]修改保存部门[{}]", SecurityUtils.getUsername(), department.getName());
        if (department.getCode() != null && !departmentAppService.checkCodeUnique(department.getId(), department.getCode())) {
            return ApiResponse.fail("修改保存部门'" + department.getCode() + "'失败，部门编码已存在");
        }
        var cmd = DepartmentMptAssembler.INSTANCE.toUpdateCmd(department);
        DepartmentDto dto = departmentAppService.updateDepartment(cmd);
        return ApiResponse.ok(DepartmentMptAssembler.INSTANCE.fromDto(dto));
    }

    /**
     * 删除部门
     *
     * @param departmentIds 部门ID数组
     * @return 结果
     */
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("org:department:remove")
    @DeleteMapping("/{departmentIds}")
    public ApiResponse<Void> remove(@PathVariable Long[] departmentIds) {
        log.info("管理后台用户[{}]删除部门[{}]", SecurityUtils.getUsername(), departmentIds);
        departmentAppService.deleteDepartments(departmentIds);
        return ApiResponse.ok();
    }

    /**
     * 获取部门树结构
     *
     * @param department 部门
     * @return 部门树结构
     */
    @RequiresPermissions("org:department:list")
    @GetMapping(value = "/deptTree")
    public ApiResponse<List<TreeSelect>> deptTree(DepartmentMpt department) {
        log.info("管理后台用户[{}]获取部门树结构", SecurityUtils.getUsername());
        DepartmentQuery query = DepartmentQuery.builder()
                .code(department.getCode())
                .name(department.getName())
                .build();
        List<DepartmentTreeDto> treeDtos = departmentAppService.getDepartmentTree(query);
        return ApiResponse.ok(convertToTreeSelect(treeDtos));
    }

    private List<TreeSelect> convertToTreeSelect(List<DepartmentTreeDto> treeDtos) {
        List<TreeSelect> treeSelects = new ArrayList<>();
        for (DepartmentTreeDto dto : treeDtos) {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(dto.getCode());
            treeSelect.setLabel(dto.getName());
            treeSelect.setChildren(dto.getChildren() != null ? convertToTreeSelect(dto.getChildren()) : new ArrayList<>());
            treeSelects.add(treeSelect);
        }
        return treeSelects;
    }
}
