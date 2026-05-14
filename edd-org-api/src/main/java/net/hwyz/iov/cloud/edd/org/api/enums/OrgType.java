package net.hwyz.iov.cloud.edd.org.api.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * 组织结构类型枚举类
 *
 * @author hwyz_leo
 */
@AllArgsConstructor
public enum OrgType {

    /** 组织 **/
    ORG,
    /** 大区 **/
    REGION,
    /** 小区 **/
    AREA,
    /** 门店 **/
    DEALERSHIP;

    public static OrgType valOf(String val) {
        return Arrays.stream(OrgType.values())
                .filter(orgType -> orgType.name().equals(val))
                .findFirst()
                .orElse(null);
    }

}
