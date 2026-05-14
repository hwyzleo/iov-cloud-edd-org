package net.hwyz.iov.cloud.edd.org.service.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.hwyz.iov.cloud.framework.mysql.po.BasePo;

import java.math.BigDecimal;

/**
 * <p>
 * 销售门店 数据对象
 * </p>
 *
 * @author hwyz_leo
 * @since 2024-10-30
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_dealership")
public class DealershipPo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 门店代码
     */
    @TableField("code")
    private String code;

    /**
     * 门店全称
     */
    @TableField("name")
    private String name;

    /**
     * 简称
     */
    @TableField("short_name")
    private String shortName;

    /**
     * 英文名称
     */
    @TableField("eng_name")
    private String engName;

    /**
     * 曾用名称
     */
    @TableField("former_name")
    private String formerName;

    /**
     * 经营类型：1-直营，2-授权，3-经销
     */
    @TableField("store_format")
    private Short storeFormat;

    /**
     * 服务类型：S-销售，D-交付，A-售后
     */
    @TableField("service_type")
    private String serviceType;

    /**
     * 注册资金
     */
    @TableField("registered_capital")
    private BigDecimal registeredCapital;

    /**
     * 展厅面积
     */
    @TableField("showroom_area")
    private String showroomArea;

    /**
     * 占地面积
     */
    @TableField("ground_area")
    private String groundArea;

    /**
     * 经营范围
     */
    @TableField("business_scope")
    private String businessScope;

    /**
     * 营业时间
     */
    @TableField("business_hours")
    private String businessHours;

    /**
     * 门店地址
     */
    @TableField("address")
    private String address;

    /**
     * 地址经度
     */
    @TableField("lon")
    private String lon;

    /**
     * 地址纬度
     */
    @TableField("lat")
    private String lat;

    /**
     * 大区代码
     */
    @TableField("region_code")
    private String regionCode;

    /**
     * 小区代码
     */
    @TableField("area_code")
    private String areaCode;

    /**
     * 省级行政区代码
     */
    @TableField("province_code")
    private String provinceCode;

    /**
     * 地区级行政区代码
     */
    @TableField("city_code")
    private String cityCode;

    /**
     * 县级行政区代码
     */
    @TableField("county_code")
    private String countyCode;

    /**
     * 传真
     */
    @TableField("fax")
    private String fax;

    /**
     * 电话
     */
    @TableField("tel")
    private String tel;

    /**
     * 手机
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 邮编
     */
    @TableField("zipcode")
    private String zipcode;

    /**
     * 电子邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 服务电话
     */
    @TableField("service_tel")
    private String serviceTel;

    /**
     * 法人
     */
    @TableField("legal_person")
    private String legalPerson;

    /**
     * 店长
     */
    @TableField("manager")
    private String manager;

    /**
     * 门店状态：0-停业，1-营业，2-在建，3-取消，4-整改，5-撤销
     */
    @TableField("state")
    private String state;

    /**
     * 是否启用
     */
    @TableField("enable")
    private Boolean enable;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
}
