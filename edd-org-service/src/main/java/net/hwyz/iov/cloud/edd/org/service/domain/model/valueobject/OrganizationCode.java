package net.hwyz.iov.cloud.edd.org.service.domain.model.valueobject;

import lombok.Value;
import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;

@Value
public class OrganizationCode {

    String value;

    public OrganizationCode(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException("组织代码不能为空");
        }
        if (value.length() > 50) {
            throw new BusinessException("组织代码长度不能超过50个字符");
        }
        this.value = value;
    }

    public boolean isValid() {
        return value != null && !value.trim().isEmpty() && value.length() <= 50;
    }
}
