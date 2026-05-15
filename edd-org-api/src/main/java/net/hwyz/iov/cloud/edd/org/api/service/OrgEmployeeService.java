package net.hwyz.iov.cloud.edd.org.api.service;

import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeExService;
import net.hwyz.iov.cloud.edd.org.api.fallback.OrgEmployeeServiceFallbackFactory;
import net.hwyz.iov.cloud.framework.common.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 员工相关服务接口
 *
 * @author hwyz_leo
 */
@FeignClient(contextId = "orgEmployeeService", value = ServiceNameConstants.EDD_ORG, path = "/service/employee", fallbackFactory = OrgEmployeeServiceFallbackFactory.class)
public interface OrgEmployeeService {

    /**
     * 根据员工ID获取员工信息
     *
     * @param id 员工ID
     */
    @GetMapping("/{id}")
    EmployeeExService getById(@PathVariable Long id);

    /**
     * 根据员工工号获取员工信息
     *
     * @param code 员工工号
     */
    @GetMapping("/code/{code}")
    EmployeeExService getByCode(@PathVariable String code);

    /**
     * 根据EIAM用户ID获取员工信息
     *
     * @param eiamUserId EIAM用户ID
     */
    @GetMapping("/eiam/{eiamUserId}")
    EmployeeExService getByEiamUserId(@PathVariable Long eiamUserId);

}
