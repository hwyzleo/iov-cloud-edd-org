package net.hwyz.iov.cloud.edd.org.api.service;

import net.hwyz.iov.cloud.edd.org.api.vo.PositionExService;
import net.hwyz.iov.cloud.edd.org.api.fallback.OrgPositionServiceFallbackFactory;
import net.hwyz.iov.cloud.framework.common.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 岗位相关服务接口
 *
 * @author hwyz_leo
 */
@FeignClient(contextId = "orgPositionService", value = ServiceNameConstants.EDD_ORG, path = "/service/position", fallbackFactory = OrgPositionServiceFallbackFactory.class)
public interface OrgPositionService {

    /**
     * 根据岗位ID获取岗位信息
     *
     * @param id 岗位ID
     */
    @GetMapping("/{id}")
    PositionExService getById(@PathVariable Long id);

    /**
     * 根据岗位ID列表获取岗位信息
     *
     * @param ids 岗位ID列表
     */
    @GetMapping("/list")
    List<PositionExService> listByIds(@RequestParam List<Long> ids);

}
