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
public class UpdatePositionCmd {

    @NotNull(message = "岗位ID不能为空")
    private Long id;
    @NotBlank(message = "岗位编码不能为空")
    private String code;
    @NotBlank(message = "岗位名称不能为空")
    private String name;
    private Boolean enable;
    private Integer sort;
}
