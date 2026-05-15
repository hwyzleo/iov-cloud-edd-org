package net.hwyz.iov.cloud.edd.org.service.application.dto.result;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

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
    private List<Long> departmentIds;
    private List<Long> positionIds;
    private String departmentNames;
    private String positionNames;
}
