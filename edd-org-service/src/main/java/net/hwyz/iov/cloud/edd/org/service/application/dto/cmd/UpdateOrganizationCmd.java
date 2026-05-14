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
public class UpdateOrganizationCmd {

    @NotNull(message = "组织ID不能为空")
    private Long id;
    @NotBlank(message = "组织代码不能为空")
    private String code;
    @NotBlank(message = "组织名称不能为空")
    private String name;
    private String orgType;
    @NotNull(message = "父组织ID不能为空")
    private Long parentId;
    private Boolean enable;
    private Integer sort;
}
