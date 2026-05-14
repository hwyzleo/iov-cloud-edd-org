package net.hwyz.iov.cloud.edd.org.api.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * 门店服务类型枚举类
 *
 * @author hwyz_leo
 */
@AllArgsConstructor
public enum DealershipServiceType {

    /** 交付 **/
    D,
    /** 销售 **/
    S,
    /** 售后 **/
    A;
    public static DealershipServiceType valOf(String val) {
        return Arrays.stream(DealershipServiceType.values())
                .filter(dealershipServiceType -> dealershipServiceType.name().equals(val))
                .findFirst()
                .orElse(null);
    }

}
