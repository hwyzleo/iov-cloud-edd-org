package net.hwyz.iov.cloud.edd.org.service.application.dto.query;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionQuery {

    private String code;
    private String name;
    private Instant beginTime;
    private Instant endTime;
}
