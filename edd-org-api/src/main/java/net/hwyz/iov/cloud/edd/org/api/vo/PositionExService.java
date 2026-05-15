package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 对外服务岗位
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionExService {

    /**
     * 岗位ID
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
