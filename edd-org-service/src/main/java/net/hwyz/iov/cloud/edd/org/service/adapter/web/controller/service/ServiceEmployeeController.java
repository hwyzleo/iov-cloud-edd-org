package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeExService;
import net.hwyz.iov.cloud.edd.org.service.application.service.EmployeeAppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工相关服务接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/service/employee")
public class ServiceEmployeeController {

    private final EmployeeAppService employeeAppService;

    /**
     * 根据员工ID获取员工信息
     *
     * @param id 员工ID
     */
    @GetMapping("/{id}")
    public EmployeeExService getById(@PathVariable Long id) {
        log.info("根据员工ID[{}]获取员工信息", id);
        var dto = employeeAppService.getEmployeeById(id);
        return EmployeeExService.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .eiamUserId(dto.getEiamUserId())
                .eiamAccount(dto.getEiamAccount())
                .enable(dto.getEnable())
                .createTime(dto.getCreateTime() != null ? java.util.Date.from(dto.getCreateTime()) : null)
                .build();
    }

    /**
     * 根据员工工号获取员工信息
     *
     * @param code 员工工号
     */
    @GetMapping("/code/{code}")
    public EmployeeExService getByCode(@PathVariable String code) {
        log.info("根据员工工号[{}]获取员工信息", code);
        var dto = employeeAppService.getEmployeeByCode(code);
        return EmployeeExService.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .eiamUserId(dto.getEiamUserId())
                .eiamAccount(dto.getEiamAccount())
                .enable(dto.getEnable())
                .createTime(dto.getCreateTime() != null ? java.util.Date.from(dto.getCreateTime()) : null)
                .build();
    }

    /**
     * 根据EIAM用户ID获取员工信息
     *
     * @param eiamUserId EIAM用户ID
     */
    @GetMapping("/eiam/{eiamUserId}")
    public EmployeeExService getByEiamUserId(@PathVariable Long eiamUserId) {
        log.info("根据EIAM用户ID[{}]获取员工信息", eiamUserId);
        var dto = employeeAppService.getEmployeeByEiamUserId(eiamUserId);
        return EmployeeExService.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .eiamUserId(dto.getEiamUserId())
                .eiamAccount(dto.getEiamAccount())
                .enable(dto.getEnable())
                .createTime(dto.getCreateTime() != null ? java.util.Date.from(dto.getCreateTime()) : null)
                .build();
    }
}
