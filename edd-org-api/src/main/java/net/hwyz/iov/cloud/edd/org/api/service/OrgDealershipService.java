package net.hwyz.iov.cloud.edd.org.api.service;

import net.hwyz.iov.cloud.edd.org.api.enums.DealershipServiceType;
import net.hwyz.iov.cloud.edd.org.api.vo.Dealership;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipExService;
import net.hwyz.iov.cloud.edd.org.api.fallback.OrgDealershipServiceFallbackFactory;
import net.hwyz.iov.cloud.framework.common.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    /**
     * 通用门店列表查询（EDD-ORG-DSN-CR-002）
     * <p>
     * 复用 DealershipAppService 既有服务类型、区域与距离筛选语义，仅返回门店自身属性。
     * 调用失败时降级返回 null，调用方必须显式处理上游故障，不得解释为“无门店”。
     *
     * @param serviceType  服务类型（必填）
     * @param provinceCode 省级行政区代码
     * @param cityCode     地区级行政区代码
     * @param countyCode   县级行政区代码
     * @param requestLon   请求经度
     * @param requestLat   请求纬度
     * @return 门店列表，无结果为空列表
     */
    @GetMapping("/list")
    List<Dealership> getDealershipList(@RequestParam DealershipServiceType serviceType,
                                       @RequestParam(required = false) String provinceCode,
                                       @RequestParam(required = false) String cityCode,
                                       @RequestParam(required = false) String countyCode,
                                       @RequestParam(required = false) String requestLon,
                                       @RequestParam(required = false) String requestLat);

}
