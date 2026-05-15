package net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    private Long id;
    private String code;
    private String name;
    private Boolean enable;
    private Integer sort;
    private Instant createTime;
    private Instant modifyTime;

    public void activate() {
        if (!Boolean.TRUE.equals(this.enable)) {
            throw new BusinessException("只有启用状态的岗位才能激活");
        }
        this.enable = true;
    }

    public void deactivate() {
        if (Boolean.TRUE.equals(this.enable)) {
            this.enable = false;
        }
    }

    public void updateInfo(String name, Integer sort) {
        if (name != null) this.name = name;
        if (sort != null) this.sort = sort;
    }
}
