package net.hwyz.iov.cloud.edd.org.service.adapter.web.controller.mpt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hwyz.iov.cloud.edd.org.api.vo.PositionMpt;
import net.hwyz.iov.cloud.edd.org.service.adapter.web.assembler.PositionMptAssembler;
import net.hwyz.iov.cloud.edd.org.service.application.dto.query.PositionQuery;
import net.hwyz.iov.cloud.edd.org.service.application.dto.result.PositionDto;
import net.hwyz.iov.cloud.edd.org.service.application.service.PositionAppService;
import net.hwyz.iov.cloud.framework.audit.annotation.Log;
import net.hwyz.iov.cloud.framework.audit.enums.BusinessType;
import net.hwyz.iov.cloud.framework.common.bean.ApiResponse;
import net.hwyz.iov.cloud.framework.common.bean.PageResult;
import net.hwyz.iov.cloud.framework.security.annotation.RequiresPermissions;
import net.hwyz.iov.cloud.framework.security.util.SecurityUtils;
import net.hwyz.iov.cloud.framework.web.controller.BaseController;
import net.hwyz.iov.cloud.framework.web.util.PageUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位相关管理接口实现类
 *
 * @author hwyz_leo
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/mpt/position/v1")
public class MptPositionController extends BaseController {

    private final PositionAppService positionAppService;

    /**
     * 查询岗位列表
     *
     * @param position 岗位
     * @return 岗位列表
     */
    @RequiresPermissions("org:position:list")
    @GetMapping(value = "/list")
    public ApiResponse<PageResult<PositionMpt>> list(PositionMpt position) {
        log.info("管理后台用户[{}]查询岗位列表", SecurityUtils.getUsername());
        PositionQuery query = PositionQuery.builder()
                .code(position.getCode())
                .name(position.getName())
                .beginTime(getBeginTime(position) != null ? getBeginTime(position).toInstant() : null)
                .endTime(getEndTime(position) != null ? getEndTime(position).toInstant() : null)
                .build();
        List<PositionDto> dtoList = positionAppService.searchPositions(query);
        return ApiResponse.ok(getPageResult(PageUtil.convert(dtoList, PositionMptAssembler.INSTANCE::fromDto)));
    }

    /**
     * 导出岗位
     *
     * @param response 响应
     * @param position 岗位
     */
    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("org:position:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, PositionMpt position) {
        log.info("管理后台用户[{}]导出岗位", SecurityUtils.getUsername());
    }

    /**
     * 根据岗位ID获取岗位
     *
     * @param positionId 岗位ID
     * @return 岗位
     */
    @RequiresPermissions("org:position:query")
    @GetMapping(value = "/{positionId}")
    public ApiResponse<PositionMpt> getInfo(@PathVariable Long positionId) {
        log.info("管理后台用户[{}]根据岗位ID[{}]获取岗位", SecurityUtils.getUsername(), positionId);
        PositionDto dto = positionAppService.getPositionById(positionId);
        return ApiResponse.ok(PositionMptAssembler.INSTANCE.fromDto(dto));
    }

    /**
     * 新增岗位
     *
     * @param position 岗位
     * @return 结果
     */
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("org:position:add")
    @PostMapping
    public ApiResponse<PositionMpt> add(@Validated @RequestBody PositionMpt position) {
        log.info("管理后台用户[{}]新增岗位[{}]", SecurityUtils.getUsername(), position.getCode());
        if (!positionAppService.checkCodeUnique(position.getId(), position.getCode())) {
            return ApiResponse.fail("新增岗位'" + position.getCode() + "'失败，岗位编码已存在");
        }
        var cmd = PositionMptAssembler.INSTANCE.toCreateCmd(position);
        PositionDto dto = positionAppService.createPosition(cmd);
        return ApiResponse.ok(PositionMptAssembler.INSTANCE.fromDto(dto));
    }

    /**
     * 修改保存岗位
     *
     * @param position 岗位
     * @return 结果
     */
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("org:position:edit")
    @PutMapping
    public ApiResponse<PositionMpt> edit(@Validated @RequestBody PositionMpt position) {
        log.info("管理后台用户[{}]修改保存岗位[{}]", SecurityUtils.getUsername(), position.getCode());
        if (!positionAppService.checkCodeUnique(position.getId(), position.getCode())) {
            return ApiResponse.fail("修改保存岗位'" + position.getCode() + "'失败，岗位编码已存在");
        }
        var cmd = PositionMptAssembler.INSTANCE.toUpdateCmd(position);
        PositionDto dto = positionAppService.updatePosition(cmd);
        return ApiResponse.ok(PositionMptAssembler.INSTANCE.fromDto(dto));
    }

    /**
     * 删除岗位
     *
     * @param positionIds 岗位ID数组
     * @return 结果
     */
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("org:position:remove")
    @DeleteMapping("/{positionIds}")
    public ApiResponse<Void> remove(@PathVariable Long[] positionIds) {
        log.info("管理后台用户[{}]删除岗位[{}]", SecurityUtils.getUsername(), positionIds);
        positionAppService.deletePositions(positionIds);
        return ApiResponse.ok();
    }
}
