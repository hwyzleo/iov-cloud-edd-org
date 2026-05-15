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
public class Department {

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

    public void changeParent(Long newParentId, String newAncestors) {
        if (this.id != null && this.id.equals(newParentId)) {
            throw new BusinessException("不能将部门设置为自己的子部门");
        }
        this.parentId = newParentId;
        this.ancestors = newAncestors;
    }

    public boolean isRoot() {
        return parentId == null || parentId == 0;
    }

    public void activate() {
        if (!Boolean.TRUE.equals(this.enable)) {
            throw new BusinessException("只有启用状态的部门才能激活");
        }
        this.enable = true;
    }

    public void deactivate() {
        if (Boolean.TRUE.equals(this.enable)) {
            this.enable = false;
        }
    }

    public void updateInfo(String name, String leader, String phone, String email, Integer sort) {
        if (name != null) this.name = name;
        if (leader != null) this.leader = leader;
        if (phone != null) this.phone = phone;
        if (email != null) this.email = email;
        if (sort != null) this.sort = sort;
    }
}
