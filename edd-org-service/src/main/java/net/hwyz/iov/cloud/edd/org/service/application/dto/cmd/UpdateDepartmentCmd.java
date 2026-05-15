package net.hwyz.iov.cloud.edd.org.service.application.dto.cmd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDepartmentCmd {

    @NotNull(message = "部门ID不能为空")
    private Long id;
    @NotBlank(message = "部门名称不能为空")
    private String name;
    private String code;
    @NotNull(message = "父部门ID不能为空")
    private Long parentId;
    private String leader;
    private String phone;
    private String email;
    private Boolean enable;
    private Integer sort;
}
