package net.hwyz.iov.cloud.edd.org.service.domain.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PositionQuery {

    private final String code;
    private final String name;
    private final Boolean enable;
}
