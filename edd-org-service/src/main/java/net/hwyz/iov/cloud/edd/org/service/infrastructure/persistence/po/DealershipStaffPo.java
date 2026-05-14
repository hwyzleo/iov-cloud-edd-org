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
     * 门店名称
     */
    @TableField("dealership_name")
    private String dealershipName;

    /**
     * 员工用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 员工名称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 员工昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 员工手机
     */
    @TableField("phonenumber")
    private String phonenumber;
}
