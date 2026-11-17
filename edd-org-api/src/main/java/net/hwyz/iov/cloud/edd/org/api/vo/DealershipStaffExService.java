package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.*;

import java.util.Date;

/**
 * 对外服务销售门店员工
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealershipStaffExService {

    /**
     * 主键
     */
    private Long id;

    /**
     * 大区代码
     */
    private String regionCode;

    /**
     * 小区代码
     */
    private String areaCode;

    /**
     * 门店代码
     */
    private String dealershipCode;

    /**
     * 门店名称
     */
    private String dealershipName;

    /**
     * 员工ID（tb_employee.id）
     */
    private Long employeeId;

    /**
     * 员工工号
     */
    private String employeeCode;

    /**
     * 员工姓名
     */
    private String employeeName;

    /**
     * 员工手机号
     */
    private String phone;

    /**
     * 创建时间
     */
    private Date createTime;

}
