package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.DepartmentExService;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler.DepartmentExServiceAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.service.DepartmentAppService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.DepartmentPo;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.repository.DepartmentRepositoryImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门相关服务接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/service/department")
public class ServiceDepartmentController {

    private final DepartmentAppService departmentAppService;

    /**
     * 根据部门ID获取部门信息
     *
     * @param id 部门ID
     */
    @GetMapping("/{id}")
    public DepartmentExService getById(@PathVariable Long id) {
        log.info("根据部门ID[{}]获取部门信息", id);
        var dto = departmentAppService.getDepartmentById(id);
        return DepartmentExService.builder()
                .id(dto.getId())
                .parentId(dto.getParentId())
                .ancestors(dto.getAncestors())
                .name(dto.getName())
                .code(dto.getCode())
                .leader(dto.getLeader())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .enable(dto.getEnable())
                .sort(dto.getSort())
                .createTime(dto.getCreateTime() != null ? java.util.Date.from(dto.getCreateTime()) : null)
                .build();
    }

    /**
     * 根据部门ID列表获取部门信息
     *
     * @param ids 部门ID列表
     */
    @GetMapping("/list")
    public List<DepartmentExService> listByIds(List<Long> ids) {
        log.info("根据部门ID列表[{}]获取部门信息", ids);
        return ids.stream()
                .map(id -> getById(id))
                .collect(Collectors.toList());
    }
}
