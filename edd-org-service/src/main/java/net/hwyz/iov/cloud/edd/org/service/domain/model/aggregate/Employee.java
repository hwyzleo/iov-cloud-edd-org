package net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private Long id;
    private String code;
    private String name;
    private Integer gender;
    private LocalDate birthday;
    private String phone;
    private String email;
    private String idCard;
    private LocalDate hireDate;
    private LocalDate leaveDate;
    private Long eiamUserId;
    private String eiamAccount;
    private Boolean enable;
    private Integer sort;
    private Instant createTime;
    private Instant modifyTime;

    public void resign(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
        this.enable = false;
    }

    public boolean isActive() {
        return enable && leaveDate == null;
    }

    public void activate() {
        if (!Boolean.TRUE.equals(this.enable)) {
            throw new BusinessException("只有启用状态的员工才能激活");
        }
        this.enable = true;
    }

    public void deactivate() {
        if (Boolean.TRUE.equals(this.enable)) {
            this.enable = false;
        }
    }

    public void updateInfo(String name, Integer gender, LocalDate birthday, String phone, String email, String idCard, Integer sort) {
        if (name != null) this.name = name;
        if (gender != null) this.gender = gender;
        if (birthday != null) this.birthday = birthday;
        if (phone != null) this.phone = phone;
        if (email != null) this.email = email;
        if (idCard != null) this.idCard = idCard;
        if (sort != null) this.sort = sort;
    }
}
