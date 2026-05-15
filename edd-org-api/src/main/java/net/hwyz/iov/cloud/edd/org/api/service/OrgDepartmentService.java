package net.hwyz.iov.cloud.edd.org.api.service;

import net.hwyz.iov.cloud.edd.org.api.vo.DepartmentExService;
import net.hwyz.iov.cloud.edd.org.api.fallback.OrgDepartmentServiceFallbackFactory;
import net.hwyz.iov.cloud.framework.common.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 部门相关服务接口
 *
 * @author hwyz_leo
 */
@FeignClient(contextId = "orgDepartmentService", value = ServiceNameConstants.EDD_ORG, path = "/service/department", fallbackFactory = OrgDepartmentServiceFallbackFactory.class)
public interface OrgDepartmentService {

    /**
     * 根据部门ID获取部门信息
     *
     * @param id 部门ID
     */
    @GetMapping("/{id}")
    DepartmentExService getById(@PathVariable Long id);

    /**
     * 根据部门ID列表获取部门信息
     *
     * @param ids 部门ID列表
     */
    @GetMapping("/list")
    List<DepartmentExService> listByIds(@RequestParam List<Long> ids);

}
