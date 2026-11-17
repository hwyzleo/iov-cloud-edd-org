package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import net.hwyz.iov.cloud.framework.mysql.po.BasePo;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 销售门店员工 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2024-12-30
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_dealership_staff")
public class DealershipStaffPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 门店代码
     */
    @TableField("dealership_code")
    private String dealershipCode;

    /**
     * 员工ID（tb_employee.id）
     */
    @TableField("employee_id")
    private Long employeeId;

    /**
     * 门店名称（关联 tb_dealership，仅用于展示）
     */
    @TableField(exist = false)
    private String dealershipName;

    /**
     * 员工工号（关联 tb_employee，仅用于展示）
     */
    @TableField(exist = false)
    private String employeeCode;

    /**
     * 员工姓名（关联 tb_employee，仅用于展示）
     */
    @TableField(exist = false)
    private String employeeName;

    /**
     * 员工手机号（关联 tb_employee，仅用于展示）
     */
    @TableField(exist = false)
    private String phone;
}
