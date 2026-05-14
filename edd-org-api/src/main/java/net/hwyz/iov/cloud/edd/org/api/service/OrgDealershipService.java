package net.hwyz.iov.cloud.edd.org.api.service;

import net.hwyz.iov.cloud.edd.org.api.vo.DealershipExService;
import net.hwyz.iov.cloud.edd.org.api.fallback.OrgDealershipServiceFallbackFactory;
import net.hwyz.iov.cloud.framework.common.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 门店相关服务接口
 *
 * @author hwyz_leo
 */
@FeignClient(contextId = "orgDealershipService", value = ServiceNameConstants.EDD_ORG, path = "/service/dealership", fallbackFactory = OrgDealershipServiceFallbackFactory.class)
public interface OrgDealershipService {

    /**
     * 根据门店代码获取门店信息
     *
     * @param dealershipCode 门店代码
     */
    @GetMapping("/{dealershipCode}")
    DealershipExService getByCode(@PathVariable String dealershipCode);

}
