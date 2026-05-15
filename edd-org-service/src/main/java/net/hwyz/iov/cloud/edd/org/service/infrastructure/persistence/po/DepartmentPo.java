package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.hwyz.iov.cloud.framework.mysql.po.BasePo;

/**
 * <p>
 * 部门 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2026-05-14
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_department")
public class DepartmentPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父部门ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 祖级列表
     */
    @TableField("ancestors")
    private String ancestors;

    /**
     * 部门名称
     */
    @TableField("name")
    private String name;

    /**
     * 部门编码
     */
    @TableField("code")
    private String code;

    /**
     * 负责人
     */
    @TableField("leader")
    private String leader;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 显示顺序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 是否启用
     */
    @TableField("enable")
    private Boolean enable;
}
