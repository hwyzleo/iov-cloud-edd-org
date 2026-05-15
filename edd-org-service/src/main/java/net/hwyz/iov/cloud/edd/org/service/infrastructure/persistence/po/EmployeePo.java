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

import java.time.LocalDate;

/**
 * <p>
 * 员工 数据对象
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
@TableName("tb_employee")
public class EmployeePo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工号
     */
    @TableField("code")
    private String code;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 性别：0-未知，1-男，2-女
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 出生日期
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 入职日期
     */
    @TableField("hire_date")
    private LocalDate hireDate;

    /**
     * 离职日期
     */
    @TableField("leave_date")
    private LocalDate leaveDate;

    /**
     * 关联EIAM用户ID
     */
    @TableField("eiam_user_id")
    private Long eiamUserId;

    /**
     * 关联EIAM账号
     */
    @TableField("eiam_account")
    private String eiamAccount;

    /**
     * 是否启用
     */
    @TableField("enable")
    private Boolean enable;

    /**
     * 显示顺序
     */
    @TableField("sort")
    private Integer sort;
}
