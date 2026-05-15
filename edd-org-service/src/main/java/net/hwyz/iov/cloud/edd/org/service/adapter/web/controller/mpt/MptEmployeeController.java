package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.mpt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeMpt;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler.EmployeeMptAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.dto.query.EmployeeQuery;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.EmployeeDto;
import net.hwyz.iov.cloud.edd.org.service.application.service.EmployeeAppService;
import net.hwyz.iov.cloud.framework.audit.annotation.Log;
import net.hwyz.iov.cloud.framework.audit.enums.BusinessType;
import net.hwyz.iov.cloud.framework.common.bean.ApiResponse;
import net.hwyz.iov.cloud.framework.common.bean.PageResult;
import net.hwyz.iov.cloud.framework.security.annotation.RequiresPermissions;
import net.hwyz.iov.cloud.framework.security.util.SecurityUtils;
import net.hwyz.iov.cloud.framework.web.controller.BaseController;
import net.hwyz.iov.cloud.framework.web.util.PageUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/mpt/employee/v1")
public class MptEmployeeController extends BaseController {

    private final EmployeeAppService employeeAppService;
    private final EmployeeMptAssembler employeeMptAssembler;

    /**
     * 查询员工列表
     *
     * @param employee 员工
     * @return 员工列表
     */
    @RequiresPermissions("org:employee:list")
    @GetMapping(value = "/list")
    public ApiResponse<PageResult<EmployeeMpt>> list(EmployeeMpt employee) {
        log.info("管理后台用户[{}]查询员工列表", SecurityUtils.getUsername());
        EmployeeQuery query = EmployeeQuery.builder()
                .code(employee.getCode())
                .name(employee.getName())
                .gender(employee.getGender())
                .phone(employee.getPhone())
                .eiamAccount(employee.getEiamAccount())
                .beginTime(getBeginTime(employee) != null ? getBeginTime(employee).toInstant() : null)
                .endTime(getEndTime(employee) != null ? getEndTime(employee).toInstant() : null)
                .build();
        List<EmployeeDto> dtoList = employeeAppService.searchEmployees(query);
        return ApiResponse.ok(getPageResult(PageUtil.convert(dtoList, employeeMptAssembler::fromDto)));
    }

    /**
     * 导出员工
     *
     * @param response 响应
     * @param employee 员工
     */
    @Log(title = "员工管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("org:employee:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, EmployeeMpt employee) {
        log.info("管理后台用户[{}]导出员工", SecurityUtils.getUsername());
    }

    /**
     * 根据员工ID获取员工
     *
     * @param employeeId 员工ID
     * @return 员工
     */
    @RequiresPermissions("org:employee:query")
    @GetMapping(value = "/{employeeId}")
    public ApiResponse<EmployeeMpt> getInfo(@PathVariable Long employeeId) {
        log.info("管理后台用户[{}]根据员工ID[{}]获取员工", SecurityUtils.getUsername(), employeeId);
        EmployeeDto dto = employeeAppService.getEmployeeById(employeeId);
        return ApiResponse.ok(employeeMptAssembler.fromDto(dto));
    }

    /**
     * 新增员工
     *
     * @param employee 员工
     * @return 结果
     */
    @Log(title = "员工管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("org:employee:add")
    @PostMapping
    public ApiResponse<EmployeeMpt> add(@Validated @RequestBody EmployeeMpt employee) {
        log.info("管理后台用户[{}]新增员工[{}]", SecurityUtils.getUsername(), employee.getCode());
        if (!employeeAppService.checkCodeUnique(employee.getId(), employee.getCode())) {
            return ApiResponse.fail("新增员工'" + employee.getCode() + "'失败，员工工号已存在");
        }
        var cmd = employeeMptAssembler.toCreateCmd(employee);
        EmployeeDto dto = employeeAppService.createEmployee(cmd);
        return ApiResponse.ok(employeeMptAssembler.fromDto(dto));
    }

    /**
     * 修改保存员工
     *
     * @param employee 员工
     * @return 结果
     */
    @Log(title = "员工管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("org:employee:edit")
    @PutMapping
    public ApiResponse<EmployeeMpt> edit(@Validated @RequestBody EmployeeMpt employee) {
        log.info("管理后台用户[{}]修改保存员工[{}]", SecurityUtils.getUsername(), employee.getCode());
        if (!employeeAppService.checkCodeUnique(employee.getId(), employee.getCode())) {
            return ApiResponse.fail("修改保存员工'" + employee.getCode() + "'失败，员工工号已存在");
        }
        var cmd = employeeMptAssembler.toUpdateCmd(employee);
        EmployeeDto dto = employeeAppService.updateEmployee(cmd);
        return ApiResponse.ok(employeeMptAssembler.fromDto(dto));
    }

    /**
     * 删除员工
     *
     * @param employeeIds 员工ID数组
     * @return 结果
     */
    @Log(title = "员工管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("org:employee:remove")
    @DeleteMapping("/{employeeIds}")
    public ApiResponse<Void> remove(@PathVariable Long[] employeeIds) {
        log.info("管理后台用户[{}]删除员工[{}]", SecurityUtils.getUsername(), employeeIds);
        employeeAppService.deleteEmployees(employeeIds);
        return ApiResponse.ok();
    }
}
