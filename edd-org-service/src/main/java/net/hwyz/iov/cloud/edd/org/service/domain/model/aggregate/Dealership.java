package net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dealership {

    private Long id;
    private String code;
    private String name;
    private String shortName;
    private String engName;
    private String formerName;
    private Short storeFormat;
    private String serviceType;
    private BigDecimal registeredCapital;
    private String showroomArea;
    private String groundArea;
    private String businessScope;
    private String businessHours;
    private String address;
    private String lon;
    private String lat;
    private String regionCode;
    private String areaCode;
    private String provinceCode;
    private String cityCode;
    private String countyCode;
    private String fax;
    private String tel;
    private String mobile;
    private String zipcode;
    private String email;
    private String serviceTel;
    private String legalPerson;
    private String manager;
    private String state;
    private Boolean enable;
    private Integer sort;
    private Instant createTime;
    private Instant modifyTime;
}