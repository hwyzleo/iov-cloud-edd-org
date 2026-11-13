package net.hwyz.iov.cloud.edd.org.service.api;

import net.hwyz.iov.cloud.edd.org.api.enums.DealershipServiceType;
import net.hwyz.iov.cloud.edd.org.api.fallback.OrgDealershipServiceFallbackFactory;
import net.hwyz.iov.cloud.edd.org.api.service.OrgDealershipService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * EDD-ORG-DSN-CR-002 §4.2：门店服务 Feign 降级契约测试。
 * <p>
 * 上游故障时必须返回 null 交由调用方显式处理，禁止伪造门店或返回空列表冒充“无门店”。
 *
 * @author hwyz_leo
 * @since 2026-11-13
 */
class OrgDealershipServiceFallbackTest {

    private final OrgDealershipService fallback =
            new OrgDealershipServiceFallbackFactory().create(new IllegalStateException("ORG 服务不可用"));

    @Test
    @DisplayName("列表查询降级返回 null，不返回空列表或伪造门店")
    void getDealershipList_returns_null_on_failure() {
        assertThatCode(() -> assertThat(fallback.getDealershipList(DealershipServiceType.S, "31", "3101",
                "310105", "121.473962", "31.234567")).isNull())
                .doesNotThrowAnyException();

        assertThat(fallback.getDealershipList(DealershipServiceType.A, null, null, null, null, null)).isNull();
    }

    @Test
    @DisplayName("按编码查询既有降级行为保持不变")
    void getByCode_still_returns_null_on_failure() {
        assertThatCode(() -> assertThat(fallback.getByCode("SHDE01")).isNull()).doesNotThrowAnyException();
    }

}
