package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.bean.BaseRequest;

import java.util.Date;
import java.util.List;

/**
 * 管理后台部门
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DepartmentMpt extends BaseRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门编码
     */
    private String code;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

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
     * 子部门列表
     */
    private List<DepartmentMpt> children;

}
