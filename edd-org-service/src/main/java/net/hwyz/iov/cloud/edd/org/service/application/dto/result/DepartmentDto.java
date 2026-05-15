package net.hwyz.iov.cloud.edd.org.service.application.dto.result;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private Long id;
    private Long parentId;
    private String ancestors;
    private String name;
    private String code;
    private String leader;
    private String phone;
    private String email;
    private Boolean enable;
    private Integer sort;
    private Instant createTime;
    private Instant modifyTime;
}
