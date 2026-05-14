package net.hwyz.iov.cloud.edd.org.api.fallback;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipExService;
import net.hwyz.iov.cloud.edd.org.api.service.OrgDealershipService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 门店相关服务降级处理
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
public class OrgDealershipServiceFallbackFactory implements FallbackFactory<OrgDealershipService> {

    @Override
    public OrgDealershipService create(Throwable throwable) {
        return new OrgDealershipService() {
            @Override
            public DealershipExService getByCode(String dealershipCode) {
                log.error("门店服务根据门店代码[{}]获取门店信息调用失败", dealershipCode, throwable);
                return null;
            }
        };
    }
}
