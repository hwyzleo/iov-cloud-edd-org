package net.hwyz.iov.cloud.edd.org.service.application.dto.cmd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeCmd {

    @NotNull(message = "员工ID不能为空")
    private Long id;
    @NotBlank(message = "工号不能为空")
    private String code;
    @NotBlank(message = "姓名不能为空")
    private String name;
    private Integer gender;
    private LocalDate birthday;
    private String phone;
    private String email;
    private String idCard;
    private LocalDate hireDate;
    private Long eiamUserId;
    private String eiamAccount;
    private Boolean enable;
    private Integer sort;
    private List<Long> departmentIds;
    private List<Long> positionIds;
}
