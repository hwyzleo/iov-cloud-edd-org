package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.enums.DealershipServiceType;
import net.hwyz.iov.cloud.edd.org.api.vo.Dealership;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipExService;
import net.hwyz.iov.cloud.edd.org.service.application.service.DealershipAppService;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler.DealershipExServiceAssembler;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipPo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 门店相关服务接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/service/dealership")
public class ServiceDealershipController {

    private final DealershipAppService dealershipAppService;

    /**
     * 根据门店代码获取门店信息
     *
     * @param dealershipCode 门店代码
     */
    @GetMapping("/{dealershipCode}")
    public DealershipExService getByCode(@PathVariable String dealershipCode) {
        log.info("根据门店代码[{}]获取门店信息", dealershipCode);
        DealershipPo dealershipPo = dealershipAppService.getDealershipByCode(dealershipCode);
        return DealershipExServiceAssembler.INSTANCE.fromPo(dealershipPo);
    }

    /**
     * 通用门店列表查询（EDD-ORG-DSN-CR-002）
     * <p>
     * 仅做参数接收与委托，筛选、距离计算与排序语义统一保留在 DealershipAppService。
     *
     * @param serviceType  服务类型
     * @param provinceCode 省级行政区代码
     * @param cityCode     地区级行政区代码
     * @param countyCode   县级行政区代码
     * @param requestLon   请求经度
     * @param requestLat   请求纬度
     * @return 门店列表
     */
    @GetMapping("/list")
    public List<Dealership> getDealershipList(@RequestParam DealershipServiceType serviceType,
                                              @RequestParam(required = false) String provinceCode,
                                              @RequestParam(required = false) String cityCode,
                                              @RequestParam(required = false) String countyCode,
                                              @RequestParam(required = false) String requestLon,
                                              @RequestParam(required = false) String requestLat) {
        log.info("服务间调用获取服务类型[{}]门店列表，省[{}]，市[{}]，区[{}]，经度[{}]，纬度[{}]",
                serviceType, provinceCode, cityCode, countyCode, requestLon, requestLat);
        return dealershipAppService.getDealershipList(serviceType, provinceCode, cityCode, countyCode, requestLon, requestLat);
    }

}
