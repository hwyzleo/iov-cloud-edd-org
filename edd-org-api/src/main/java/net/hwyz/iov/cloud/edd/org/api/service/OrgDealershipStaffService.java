package net.hwyz.iov.cloud.edd.org.api.service;

import net.hwyz.iov.cloud.edd.org.api.vo.DealershipStaffExService;
import net.hwyz.iov.cloud.edd.org.api.fallback.OrgDealershipStaffServiceFallbackFactory;
import net.hwyz.iov.cloud.framework.common.bean.PageResult;
import net.hwyz.iov.cloud.framework.common.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 门店员工相关服务接口
 *
 * @author hwyz_leo
 */
@FeignClient(contextId = "orgDealershipStaffService", value = ServiceNameConstants.EDD_ORG, path = "/service/dealershipStaff", fallbackFactory = OrgDealershipStaffServiceFallbackFactory.class)
public interface OrgDealershipStaffService {

    /**
     * 分页查询门店员工信息
     *
     * @param dealershipCode 门店代码
     */
    @GetMapping("/page")
    PageResult<DealershipStaffExService> searchPage(@RequestParam(required = false) String dealershipCode,
                                                    @RequestParam(required = false) Integer pageNum,
                                                    @RequestParam(required = false) Integer pageSize);

}
