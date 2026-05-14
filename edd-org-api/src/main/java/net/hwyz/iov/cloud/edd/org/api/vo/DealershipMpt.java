package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.*;
import net.hwyz.iov.cloud.framework.common.bean.BaseRequest;

import java.util.Date;

/**
 * 管理后台销售门店
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DealershipMpt extends BaseRequest {

    /**
     * 门店ID
     */
    private Long id;

    /**
     * 门店代码
     */
    private String code;

    /**
     * 门店全称
     */
    private String name;

    /**
     * 门店地址
     */
    private String address;

    /**
     * 大区代码
     */
    private String regionCode;

    /**
     * 小区代码
     */
    private String areaCode;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

}
