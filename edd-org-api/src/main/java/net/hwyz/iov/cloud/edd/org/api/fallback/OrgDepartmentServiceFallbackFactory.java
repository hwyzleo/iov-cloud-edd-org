package net.hwyz.iov.cloud.edd.org.api.fallback;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.DepartmentExService;
import net.hwyz.iov.cloud.edd.org.api.service.OrgDepartmentService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 部门相关服务降级处理
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
public class OrgDepartmentServiceFallbackFactory implements FallbackFactory<OrgDepartmentService> {

    @Override
    public OrgDepartmentService create(Throwable throwable) {
        return new OrgDepartmentService() {
            @Override
            public DepartmentExService getById(Long id) {
                log.error("部门服务根据部门ID[{}]获取部门信息调用失败", id, throwable);
                return null;
            }

            @Override
            public List<DepartmentExService> listByIds(List<Long> ids) {
                log.error("部门服务根据部门ID列表[{}]获取部门信息调用失败", ids, throwable);
                return null;
            }
        };
    }
}
