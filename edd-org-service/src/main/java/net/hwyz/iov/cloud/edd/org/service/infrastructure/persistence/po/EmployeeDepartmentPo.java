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
 * 员工-部门关联 数据对象
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
@TableName("tb_employee_department")
public class EmployeeDepartmentPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 员工ID
     */
    @TableField("employee_id")
    private Long employeeId;

    /**
     * 部门ID
     */
    @TableField("department_id")
    private Long departmentId;

    /**
     * 是否主部门
     */
    @TableField("is_primary")
    private Boolean isPrimary;
}
