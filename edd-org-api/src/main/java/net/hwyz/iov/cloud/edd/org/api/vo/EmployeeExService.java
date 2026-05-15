package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 对外服务员工
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeExService {

    /**
     * 员工ID
     */
    private Long id;

    /**
     * 工号
     */
    private String code;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 关联EIAM用户ID
     */
    private Long eiamUserId;

    /**
     * 关联EIAM账号
     */
    private String eiamAccount;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 创建时间
     */
    private Date createTime;

}
