package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipStaffExService;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler.DealershipStaffExServiceAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.service.DealershipStaffAppService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DealershipStaffPo;
import net.hwyz.iov.cloud.framework.common.bean.PageResult;
import net.hwyz.iov.cloud.framework.web.controller.BaseController;
import net.hwyz.iov.cloud.framework.web.util.PageUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 门店员工相关服务接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/service/dealershipStaff")
public class ServiceDealershipStaffController extends BaseController {

    private final DealershipStaffAppService dealershipStaffAppService;

    /**
     * 分页查询门店员工信息
     *
     * @param dealershipCode 门店代码
     */
    @GetMapping("/page")
    public PageResult<DealershipStaffExService> searchPage(@RequestParam(required = false) String dealershipCode, @RequestParam(required = false) Integer pageNum,
                                                           @RequestParam(required = false) Integer pageSize) {
        log.info("分页查询门店员工信息");
        startPage();
        List<DealershipStaffPo> dealershipStaffPoList = dealershipStaffAppService.search(null, null,
                dealershipCode, null, null, null, null, null);
        return getPageResult(PageUtil.convert(dealershipStaffPoList, DealershipStaffExServiceAssembler.INSTANCE::fromPo));
    }

}
