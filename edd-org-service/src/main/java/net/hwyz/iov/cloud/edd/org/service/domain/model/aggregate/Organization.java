package net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import net.hwyz.iov.cloud.edd.org.service.domain.exception.BusinessException;
import net.hwyz.iov.cloud.edd.org.service.domain.model.valueobject.OrganizationCode;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    private Long id;
    private OrganizationCode code;
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
        this.enable = true;
    }

    public void deactivate() {
        if (Boolean.TRUE.equals(this.enable)) {
            this.enable = false;
        }
    }

    public void changeParent(Long newParentId, String newAncestors) {
        if (this.id != null && this.id.equals(newParentId)) {
            throw new BusinessException("不能将组织设置为自己的子组织");
        }
        this.parentId = newParentId;
        this.ancestors = newAncestors;
    }

    public void updateInfo(String name, String orgType, Integer sort) {
        if (name != null) this.name = name;
        if (orgType != null) this.orgType = orgType;
        if (sort != null) this.sort = sort;
    }

    public boolean isRoot() {
        return parentId == null || parentId == 0;
    }
}
