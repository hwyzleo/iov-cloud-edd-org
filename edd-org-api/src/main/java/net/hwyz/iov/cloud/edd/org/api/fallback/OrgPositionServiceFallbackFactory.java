package net.hwyz.iov.cloud.edd.org.api.fallback;

import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.PositionExService;
import net.hwyz.iov.cloud.edd.org.api.service.OrgPositionService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 岗位相关服务降级处理
 *
 * @author hwyz_leo
 */
@Slf4j
@Component
public class OrgPositionServiceFallbackFactory implements FallbackFactory<OrgPositionService> {

    @Override
    public OrgPositionService create(Throwable throwable) {
        return new OrgPositionService() {
            @Override
            public PositionExService getById(Long id) {
                log.error("岗位服务根据岗位ID[{}]获取岗位信息调用失败", id, throwable);
                return null;
            }

            @Override
            public List<PositionExService> listByIds(List<Long> ids) {
                log.error("岗位服务根据岗位ID列表[{}]获取岗位信息调用失败", ids, throwable);
                return null;
            }
        };
    }
}
