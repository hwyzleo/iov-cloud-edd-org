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
public class PositionDto {

    private Long id;
    private String code;
    private String name;
    private Boolean enable;
    private Integer sort;
    private Instant createTime;
    private Instant modifyTime;
}
