package net.hwyz.iov.cloud.edd.org.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 销售门店
 *
 * @author hwyz_leo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dealership {

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
     * 门店距离
     */
    private Double distance;

}
