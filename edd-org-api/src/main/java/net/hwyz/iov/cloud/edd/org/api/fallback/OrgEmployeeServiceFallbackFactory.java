package net.hwyz.iov.cloud.edd.org.api.fallback;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeExService;
import net.hwyz.iov.cloud.edd.org.api.service.OrgEmployeeService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 员工相关服务降级处理
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
public class OrgEmployeeServiceFallbackFactory implements FallbackFactory<OrgEmployeeService> {

    @Override
    public OrgEmployeeService create(Throwable throwable) {
        return new OrgEmployeeService() {
            @Override
            public EmployeeExService getById(Long id) {
                log.error("员工服务根据员工ID[{}]获取员工信息调用失败", id, throwable);
                return null;
            }

            @Override
            public EmployeeExService getByCode(String code) {
                log.error("员工服务根据员工工号[{}]获取员工信息调用失败", code, throwable);
                return null;
            }

            @Override
            public EmployeeExService getByEiamUserId(Long eiamUserId) {
                log.error("员工服务根据EIAM用户ID[{}]获取员工信息调用失败", eiamUserId, throwable);
                return null;
            }
        };
    }
}
