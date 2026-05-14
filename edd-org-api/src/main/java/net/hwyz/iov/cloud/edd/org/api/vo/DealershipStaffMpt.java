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
    private String dealershipCode;

    /**
     * 门店名称
     */
    private String dealershipName;

    /**
     * 员工用户ID
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 创建时间
     */
    private Date createTime;

}
