package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.bean.BaseRequest;

import java.util.Date;
import java.util.List;

/**
 * 管理后台组织结构
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrgMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 父组织ID
     */
    private Long parentId;

    /**
     * 大区代码
     */
    private String regionCode;

    /**
     * 祖籍列表
     */
    private String ancestors;

    /**
     * 组织代码
     */
    private String code;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织类型
     */
    private String orgType;

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

    /**
     * 子组织列表
     */
    private List<OrgMpt> children;

}
