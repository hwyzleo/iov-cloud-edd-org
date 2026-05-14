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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 组织结构 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2025-01-04
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_org")
public class OrgPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父组织ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 祖籍列表
     */
    @TableField("ancestors")
    private String ancestors;

    /**
     * 组织代码
     */
    @TableField("code")
    private String code;

    /**
     * 组织名称
     */
    @TableField("name")
    private String name;

    /**
     * 组织类型
     */
    @TableField("org_type")
    private String orgType;

    /**
     * 是否启用
     */
    @TableField("enable")
    private Boolean enable;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 子部门
     */
    private List<OrgPo> children = new ArrayList<>();
}
