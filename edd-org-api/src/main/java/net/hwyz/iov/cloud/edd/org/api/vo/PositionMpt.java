package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.bean.BaseRequest;

import java.util.Date;

/**
 * 管理后台岗位
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PositionMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 岗位编码
     */
    private String code;

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

}
