package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在职员工选项（EDD-ORG-DSN-CR-004）
 * <p>
 * 供 EDD-MPT 用户绑定员工时选人使用：仅返回启用的在职员工，不包含账号/权限信息。
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeOptionExService {

    /**
     * 员工ID
     */
    private Long employeeId;

    /**
     * 工号
     */
    private String employeeCode;

    /**
     * 姓名
     */
    private String employeeName;

    /**
     * 手机号
     */
    private String phone;

}
