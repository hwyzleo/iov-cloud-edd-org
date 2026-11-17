package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.annotation.Excel;
import net.hwyz.iov.cloud.framework.common.bean.BaseRequest;

import java.util.Date;

/**
 * 管理后台销售门店员工
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DealershipStaffMpt extends BaseRequest {

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
    @Excel(name = "门店代码", sort = 1)
    private String dealershipCode;

    /**
     * 门店名称
     */
    @Excel(name = "门店名称", sort = 2)
    private String dealershipName;

    /**
     * 员工ID（tb_employee.id）
     */
    private Long employeeId;

    /**
     * 员工工号
     */
    @Excel(name = "工号", sort = 3)
    private String employeeCode;

    /**
     * 员工姓名
     */
    @Excel(name = "姓名", sort = 4)
    private String employeeName;

    /**
     * 员工手机号
     */
    @Excel(name = "手机号码", sort = 5)
    private String phone;

    /**
     * 备注
     */
    @Excel(name = "备注", sort = 6)
    private String description;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", sort = 7, dateFormat = "yyyy-MM-dd HH:mm:ss", width = 20)
    private Date createTime;

}
