package net.hwyz.iov.cloud.edd.org.api.service;

import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeChangeExService;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeExService;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeOptionExService;
import net.hwyz.iov.cloud.framework.common.bean.PageResult;
import net.hwyz.iov.cloud.edd.org.api.fallback.OrgEmployeeServiceFallbackFactory;
import net.hwyz.iov.cloud.framework.common.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    /**
     * 增量查询员工变更（EDD-ORG-DSN-CR-004）
     *
     * @param since 水位（epoch 毫秒，不含）；为空表示从最早开始
     * @param page  页码（从 1 开始）
     * @param size  每页条数（上限 500）
     * @return 分页变更结果，rowValid=false 表示已逻辑删除
     */
    @GetMapping("/changes")
    PageResult<EmployeeChangeExService> getEmployeeChanges(@RequestParam(required = false) Long since,
                                                           @RequestParam(required = false) Integer page,
                                                           @RequestParam(required = false) Integer size);

    /**
     * 在职员工检索（供账号绑定选人，EDD-ORG-DSN-CR-004）
     *
     * @param key 关键词（工号/姓名/手机号）
     * @return 在职员工选项，最多 20 条
     */
    @GetMapping("/active")
    List<EmployeeOptionExService> getActiveEmployees(@RequestParam(required = false) String key);

}
