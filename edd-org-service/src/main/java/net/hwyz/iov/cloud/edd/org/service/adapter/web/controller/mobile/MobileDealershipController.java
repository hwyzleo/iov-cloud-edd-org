package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.mobile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.enums.DealershipServiceType;
import net.hwyz.iov.cloud.edd.org.api.vo.Dealership;
import net.hwyz.iov.cloud.edd.org.service.application.service.DealershipAppService;
import net.hwyz.iov.cloud.framework.common.bean.ApiResponse;
import net.hwyz.iov.cloud.framework.common.bean.ClientAccount;
import net.hwyz.iov.cloud.framework.common.util.ParamHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 销售门店相关手机接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/mobile/dealership/v1")
public class MobileDealershipController {

    private final DealershipAppService dealershipAppService;

    /**
     * 获取销售门店列表
     *
     * @param serviceType   服务类型
     * @param provinceCode  省级行政区代码
     * @param cityCode      地区级行政区代码
     * @param countyCode    县级行政区代码
     * @param requestLon    请求经度
     * @param requestLat    请求纬度
     * @param clientAccount 终端用户
     * @return 销售门店列表
     */
    @GetMapping("")
    public ApiResponse<List<Dealership>> getDealershipList(@RequestParam DealershipServiceType serviceType,
                                                           @RequestParam(required = false) String provinceCode,
                                                           @RequestParam(required = false) String cityCode,
                                                           @RequestParam(required = false) String countyCode,
                                                           @RequestParam(required = false) String requestLon,
                                                           @RequestParam(required = false) String requestLat,
                                                           @RequestHeader ClientAccount clientAccount) {
        log.info("手机客户端[{}]获取服务类型[{}]销售门店列表", ParamHelper.getClientAccountInfo(clientAccount), serviceType);
        return ApiResponse.ok(dealershipAppService.getDealershipList(serviceType, provinceCode, cityCode, countyCode, requestLon, requestLat));
    }

}
