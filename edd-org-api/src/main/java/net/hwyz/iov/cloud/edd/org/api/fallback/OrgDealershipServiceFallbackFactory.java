package net.hwyz.iov.cloud.edd.org.api.fallback;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.enums.DealershipServiceType;
import net.hwyz.iov.cloud.edd.org.api.vo.Dealership;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipExService;
import net.hwyz.iov.cloud.edd.org.api.service.OrgDealershipService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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

            @Override
            public List<Dealership> getDealershipList(DealershipServiceType serviceType, String provinceCode,
                                                      String cityCode, String countyCode, String requestLon,
                                                      String requestLat) {
                log.error("门店服务通用门店列表查询调用失败，服务类型[{}]，省[{}]，市[{}]，区[{}]，经度[{}]，纬度[{}]",
                        serviceType, provinceCode, cityCode, countyCode, requestLon, requestLat, throwable);
                return null;
            }
        };
    }
}
