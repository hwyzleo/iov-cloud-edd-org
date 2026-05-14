package net.hwyz.iov.cloud.edd.org.service.application.dto.cmd;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationCmd {

    private String code;
    private String name;
    private String orgType;
    private Long parentId;
    private Boolean enable;
    private Integer sort;
}
