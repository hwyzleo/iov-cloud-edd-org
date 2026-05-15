package net.hwyz.iov.cloud.edd.org.service.application.dto.result;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentTreeDto {

    private Long id;
    private String code;
    private String name;
    private String leader;
    private Boolean enable;
    private Integer sort;
    private List<DepartmentTreeDto> children;
}
