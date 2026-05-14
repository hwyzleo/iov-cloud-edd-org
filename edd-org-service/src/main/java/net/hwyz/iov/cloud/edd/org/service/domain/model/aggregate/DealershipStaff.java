package net.hwyz.iov.cloud.edd.org.service.domain.model.aggregate;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealershipStaff {

    private Long id;
    private String dealershipCode;
    private String dealershipName;
    private Long userId;
    private String userName;
    private String nickName;
    private String phonenumber;
    private Instant createTime;
    private Instant modifyTime;
}