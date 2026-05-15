package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.PositionExService;
import net.hwyz.iov.cloud.edd.org.service.application.service.PositionAppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位相关服务接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/service/position")
public class ServicePositionController {

    private final PositionAppService positionAppService;

    /**
     * 根据岗位ID获取岗位信息
     *
     * @param id 岗位ID
     */
    @GetMapping("/{id}")
    public PositionExService getById(@PathVariable Long id) {
        log.info("根据岗位ID[{}]获取岗位信息", id);
        var dto = positionAppService.getPositionById(id);
        return PositionExService.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .enable(dto.getEnable())
                .sort(dto.getSort())
                .createTime(dto.getCreateTime() != null ? java.util.Date.from(dto.getCreateTime()) : null)
                .build();
    }

    /**
     * 根据岗位ID列表获取岗位信息
     *
     * @param ids 岗位ID列表
     */
    @GetMapping("/list")
    public List<PositionExService> listByIds(List<Long> ids) {
        log.info("根据岗位ID列表[{}]获取岗位信息", ids);
        return ids.stream()
                .map(id -> getById(id))
                .collect(Collectors.toList());
    }
}
