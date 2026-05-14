package net.hwyz.iov.cloud.edd.org.api.fallback;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.service.OrgDealershipStaffService;
import net.hwyz.iov.cloud.edd.org.api.vo.DealershipStaffExService;
import net.hwyz.iov.cloud.framework.common.bean.PageResult;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 门店员工相关服务降级处理
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
public class OrgDealershipStaffServiceFallbackFactory implements FallbackFactory<OrgDealershipStaffService> {

    @Override
    public OrgDealershipStaffService create(Throwable throwable) {
        return new OrgDealershipStaffService() {
            @Override
            public PageResult<DealershipStaffExService> searchPage(String dealershipCode, Integer pageNum, Integer pageSize) {
                log.error("门店员工服务分页查询门店员工信息调用失败", throwable);
                return null;
            }
        };
    }
}
