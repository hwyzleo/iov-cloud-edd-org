package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

/**
 * 员工增量同步变更项（EDD-ORG-DSN-CR-004）
 * <p>
 * 供 SEC-EIAM 定时拉取；只暴露身份与在职状态所需字段，不包含账号、密码、权限等非 ORG 属主数据。
 * `rowValid=false` 表示该员工已被逻辑删除（墓碑），下游应据此停用对应账号。
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeChangeExService {

    /**
     * 员工ID（ORG 员工主数据主键，下游身份标识）
     */
    private Long employeeId;

    /**
     * 工号
     */
    private String code;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
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
     * 离职日期
     */
    private LocalDate leaveDate;

    /**
     * 行是否有效：false 表示已逻辑删除（墓碑）
     */
    private Boolean rowValid;

    /**
     * 修改时间（同步水位，epoch 毫秒）
     */
    private Date modifyTime;

}
