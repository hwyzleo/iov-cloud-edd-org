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
public class Organization {

    private Long id;
    private String code;
    private String name;
    private String orgType;
    private Long parentId;
    private String ancestors;
    private Boolean enable;
    private Integer sort;
    private Instant createTime;
    private Instant modifyTime;

    public void activate() {
        if (!Boolean.TRUE.equals(this.enable)) {
            throw new BusinessException("只有启用状态的组织才能激活");
        }
    }

    public void deactivate() {
        if (Boolean.TRUE.equals(this.enable)) {
            this.enable = false;
        }
    }

    public void changeParent(Long newParentId, String newAncestors) {
        this.parentId = newParentId;
        this.ancestors = newAncestors;
    }

    public void updateInfo(String name, String orgType, Boolean enable, Integer sort) {
        if (name != null) this.name = name;
        if (orgType != null) this.orgType = orgType;
        if (enable != null) this.enable = enable;
        if (sort != null) this.sort = sort;
    }

    public boolean isRoot() {
        return parentId == null || parentId == 0;
    }
}
