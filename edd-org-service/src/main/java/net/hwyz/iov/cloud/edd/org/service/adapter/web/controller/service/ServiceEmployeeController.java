package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeChangeExService;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeExService;
import net.hwyz.iov.cloud.edd.org.api.vo.EmployeeOptionExService;
import net.hwyz.iov.cloud.framework.common.bean.PageResult;
import net.hwyz.iov.cloud.edd.org.service.application.service.EmployeeAppService;
import net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po.EmployeePo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工相关服务接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/service/employee")
public class ServiceEmployeeController {

    /**
     * 增量同步默认/最大分页
     */
    private static final int DEFAULT_SYNC_PAGE_SIZE = 200;
    private static final int MAX_SYNC_PAGE_SIZE = 500;

    /**
     * 在职员工检索返回上限
     */
    private static final int ACTIVE_OPTION_LIMIT = 20;

    private final EmployeeAppService employeeAppService;

    /**
     * 根据员工ID获取员工信息
     *
     * @param id 员工ID
     */
    @GetMapping("/{id}")
    public EmployeeExService getById(@PathVariable Long id) {
        log.info("根据员工ID[{}]获取员工信息", id);
        var dto = employeeAppService.getEmployeeById(id);
        return EmployeeExService.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .eiamUserId(dto.getEiamUserId())
                .eiamAccount(dto.getEiamAccount())
                .enable(dto.getEnable())
                .createTime(dto.getCreateTime() != null ? java.util.Date.from(dto.getCreateTime()) : null)
                .build();
    }

    /**
     * 根据员工工号获取员工信息
     *
     * @param code 员工工号
     */
    @GetMapping("/code/{code}")
    public EmployeeExService getByCode(@PathVariable String code) {
        log.info("根据员工工号[{}]获取员工信息", code);
        var dto = employeeAppService.getEmployeeByCode(code);
        return EmployeeExService.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .eiamUserId(dto.getEiamUserId())
                .eiamAccount(dto.getEiamAccount())
                .enable(dto.getEnable())
                .createTime(dto.getCreateTime() != null ? java.util.Date.from(dto.getCreateTime()) : null)
                .build();
    }

    /**
     * 根据EIAM用户ID获取员工信息
     *
     * @param eiamUserId EIAM用户ID
     */
    @GetMapping("/eiam/{eiamUserId}")
    public EmployeeExService getByEiamUserId(@PathVariable Long eiamUserId) {
        log.info("根据EIAM用户ID[{}]获取员工信息", eiamUserId);
        var dto = employeeAppService.getEmployeeByEiamUserId(eiamUserId);
        return EmployeeExService.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .eiamUserId(dto.getEiamUserId())
                .eiamAccount(dto.getEiamAccount())
                .enable(dto.getEnable())
                .createTime(dto.getCreateTime() != null ? java.util.Date.from(dto.getCreateTime()) : null)
                .build();
    }

    /**
     * 员工增量同步（EDD-ORG-DSN-CR-004）
     * <p>
     * 供 SEC-EIAM 定时拉取：按 modify_time 水位增量返回，含逻辑删除墓碑（rowValid=false），
     * 排序 modify_time, id 升序；调用方以最后一条记录的 modifyTime 推进水位。
     *
     * @param since 水位（epoch 毫秒或 ISO-8601，均可）；为空表示从最早开始
     * @param page  页码（从 1 开始，默认 1）
     * @param size  每页条数（默认 200，上限 500）
     */
    @GetMapping("/changes")
    public PageResult<EmployeeChangeExService> changes(@RequestParam(required = false) String since,
                                                       @RequestParam(required = false) Integer page,
                                                       @RequestParam(required = false) Integer size) {
        int pageNum = page == null || page < 1 ? 1 : page;
        int pageSize = size == null || size < 1 ? DEFAULT_SYNC_PAGE_SIZE : Math.min(size, MAX_SYNC_PAGE_SIZE);
        Date sinceDate = parseWatermark(since);
        log.info("增量同步员工变更：since={}, page={}, size={}", sinceDate, pageNum, pageSize);
        long total = employeeAppService.countEmployeeChanges(sinceDate);
        List<EmployeePo> poList = employeeAppService.findEmployeeChanges(sinceDate, (pageNum - 1) * pageSize, pageSize);
        List<EmployeeChangeExService> items = poList.stream()
                .map(po -> EmployeeChangeExService.builder()
                        .employeeId(po.getId())
                        .code(po.getCode())
                        .name(po.getName())
                        .phone(po.getPhone())
                        .email(po.getEmail())
                        .enable(po.getEnable())
                        .leaveDate(po.getLeaveDate())
                        .rowValid(po.getRowValid())
                        .modifyTime(po.getModifyTime())
                        .build())
                .collect(Collectors.toList());
        return new PageResult<>(items, total, pageNum, pageSize);
    }

    /**
     * 在职员工检索（供 EDD-MPT 用户绑定选人，EDD-ORG-DSN-CR-004）
     *
     * @param key 关键词（工号/姓名/手机号）
     */
    @GetMapping("/active")
    public List<EmployeeOptionExService> active(@RequestParam(required = false) String key) {
        log.info("检索在职员工，keyLength={}", key == null ? 0 : key.length());
        return employeeAppService.searchActiveEmployees(key, ACTIVE_OPTION_LIMIT).stream()
                .map(po -> EmployeeOptionExService.builder()
                        .employeeId(po.getId())
                        .employeeCode(po.getCode())
                        .employeeName(po.getName())
                        .phone(po.getPhone())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 解析水位：支持 epoch 毫秒与 ISO-8601（含 offset / Z）
     */
    private Date parseWatermark(String since) {
        if (since == null || since.isBlank()) {
            return null;
        }
        String value = since.trim();
        if (value.matches("\\d{1,19}")) {
            return new Date(Long.parseLong(value));
        }
        try {
            return Date.from(OffsetDateTime.parse(value).toInstant());
        } catch (Exception ignored) {
            try {
                return Date.from(Instant.parse(value));
            } catch (Exception e) {
                throw new IllegalArgumentException("since 格式非法，仅支持 epoch 毫秒或 ISO-8601：" + value);
            }
        }
    }
}
