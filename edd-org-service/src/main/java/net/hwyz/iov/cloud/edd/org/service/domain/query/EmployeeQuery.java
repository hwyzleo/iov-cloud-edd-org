package net.hwyz.iov.cloud.edd.org.service.domain.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeQuery {

    private final String code;
    private final String name;
    private final Integer gender;
    private final String phone;
    private final String eiamAccount;
    private final Boolean enable;
}
