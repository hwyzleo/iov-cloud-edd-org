package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipExService;
import net.hwyz.iov.cloud.edd.org.service.application.service.DealershipAppService;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler.DealershipExServiceAssembler;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipPo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
