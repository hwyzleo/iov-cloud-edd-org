package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.bean.BaseRequest;

import java.util.Date;
import java.util.List;

/**
 * 管理后台员工
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmployeeMpt extends BaseRequest {

    /**
     * 主键
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
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 部门ID列表，第一个为主部门
     */
    private List<Long> departmentIds;

    /**
     * 岗位ID列表，第一个为主岗位
     */
    private List<Long> positionIds;

    /**
     * 所属部门名称，逗号分隔，主部门标记"(主)"
     */
    private String departmentNames;

    /**
     * 岗位名称，逗号分隔，主岗位标记"(主)"
     */
    private String positionNames;

}
