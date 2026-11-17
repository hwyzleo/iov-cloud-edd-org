package net.hwyz.iov.cloud.edd.org.api.fallback;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeChangeExService;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeExService;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeOptionExService;
import net.hwyz.iov.cloud.framework.common.bean.PageResult;
import net.hwyz.iov.cloud.edd.org.api.service.OrgEmployeeService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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

            @Override
            public PageResult<EmployeeChangeExService> getEmployeeChanges(Long since, Integer page, Integer size) {
                log.error("员工服务增量同步调用失败，since={}, page={}, size={}", since, page, size, throwable);
                // 返回 null 表示上游不可用，调用方必须显式处理，不得当作"无变更"
                return null;
            }

            @Override
            public List<EmployeeOptionExService> getActiveEmployees(String key) {
                log.error("员工服务在职员工检索调用失败，key={}", key == null ? null : key.length(), throwable);
                return Collections.emptyList();
            }
        };
    }
}
