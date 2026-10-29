package net.hwyz.iov.cloud.edd.org.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * EDD-ORG-DSN-CR-001：生产 mapper 时间契约测试（真实 Spring 上下文 + 框架装配）。
 * <p>
 * 注入生产 ObjectMapper（已应用 IovJacksonAutoConfiguration customizer、postConfigurer
 * 模块注册与 JacksonStartupProbe 启动探针），验证：
 * <ul>
 *   <li>epoch-millis 模式：Instant/OffsetDateTime/LocalDateTime/java.util.Date 均输出
 *       13 位 epoch 毫秒数字（复现存量输出）；LocalDateTime 按显式 Asia/Shanghai 业务时区；</li>
 *   <li>双读：13 位毫秒 / 10 位秒 / ISO-8601 字符串归一至同一 Instant；</li>
 *   <li>非法输入默认拒绝（数组、无 offset ISO、空串、窗口外数字等），null 保持 null；</li>
 *   <li>排除范围：普通数字/字符串/布尔输出不变；</li>
 *   <li>UTC 与 Asia/Shanghai JVM 下 Instant/Date 输出一致（不依赖 JVM 默认时区）。</li>
 * </ul>
 * 上下文能成功加载即证明框架启动探针通过（date-format=epoch-millis, zone-id=Asia/Shanghai）。
 *
 * @author hwyz_leo
 * @since 2026-10-29
 */
@SpringBootTest
class OrgJacksonContractTest {

    private static final Instant SAMPLE_INSTANT = Instant.parse("2026-10-28T12:43:32Z");
    private static final OffsetDateTime SAMPLE_OFFSET = OffsetDateTime.parse("2026-10-28T20:43:32+08:00");
    private static final LocalDateTime SAMPLE_LOCAL = LocalDateTime.parse("2026-10-14T14:26:13");

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 上下文加载_框架启动探针通过() {
        // 探针（SmartInitializingSingleton）在上下文 refresh 时运行，加载成功即通过
    }

    @Test
    void epochMillis模式_Instant输出13位毫秒数字() throws Exception {
        assertThat(objectMapper.writeValueAsString(SAMPLE_INSTANT)).isEqualTo("1793191412000");
    }

    @Test
    void epochMillis模式_OffsetDateTime输出13位毫秒数字() throws Exception {
        assertThat(objectMapper.writeValueAsString(SAMPLE_OFFSET))
                .isEqualTo(String.valueOf(SAMPLE_OFFSET.toInstant().toEpochMilli()));
    }

    @Test
    void epochMillis模式_LocalDateTime按AsiaShanghai业务时区输出毫秒数字() throws Exception {
        // 2026-10-14T14:26:13 为 Asia/Shanghai 墙上时间 → 2026-10-14T06:26:13Z
        assertThat(objectMapper.writeValueAsString(SAMPLE_LOCAL)).isEqualTo("1791959173000");
    }

    @Test
    void javaUtilDate输出epoch毫秒数字_排除范围不变() throws Exception {
        // api/vo 唯一时间字段 java.util.Date（FW-WEB CR 排除范围：RuoYi 旧式 Date 不混入本轮）
        assertThat(objectMapper.writeValueAsString(Date.from(SAMPLE_INSTANT))).isEqualTo("1793191412000");
    }

    @Test
    void 双读_13位毫秒10位秒ISO归一至同一Instant() throws Exception {
        long millis = SAMPLE_INSTANT.toEpochMilli();
        long seconds = SAMPLE_INSTANT.getEpochSecond();

        Instant fromMillis = objectMapper.readValue(String.valueOf(millis), Instant.class);
        Instant fromSeconds = objectMapper.readValue(String.valueOf(seconds), Instant.class);
        Instant fromIso = objectMapper.readValue("\"" + SAMPLE_INSTANT + "\"", Instant.class);

        assertThat(fromMillis).isEqualTo(SAMPLE_INSTANT);
        assertThat(fromSeconds).isEqualTo(SAMPLE_INSTANT);
        assertThat(fromIso).isEqualTo(SAMPLE_INSTANT);
    }

    @Test
    void 非法输入默认拒绝_数组秒小数非法ISO空串窗口外() {
        // 数组
        assertThatThrownBy(() -> objectMapper.readValue("[]", Instant.class)).isInstanceOf(Exception.class);
        // 无 offset 的 ISO（Instant 必须含 Z/offset）
        assertThatThrownBy(() -> objectMapper.readValue("\"2026-10-28T12:43:32\"", Instant.class))
                .isInstanceOf(Exception.class);
        // 非法日期
        assertThatThrownBy(() -> objectMapper.readValue("\"2026-13-45T99:99:99Z\"", Instant.class))
                .isInstanceOf(Exception.class);
        // 空字符串
        assertThatThrownBy(() -> objectMapper.readValue("\"\"", Instant.class)).isInstanceOf(Exception.class);
        // 窗口外数字（epoch 0）
        assertThatThrownBy(() -> objectMapper.readValue("0", Instant.class)).isInstanceOf(Exception.class);
        // 错误类型
        assertThatThrownBy(() -> objectMapper.readValue("\"abc\"", Instant.class)).isInstanceOf(Exception.class);
    }

    @Test
    void null保持null不转换为epoch0() throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("createTime", null);
        map.put("modifyTime", null);
        JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(map));
        assertThat(node.get("createTime").isNull()).isTrue();
        assertThat(node.get("modifyTime").isNull()).isTrue();
    }

    @Test
    void 排除范围_普通数字字符串布尔不变() throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", 123L);
        map.put("sort", 7);
        map.put("name", "测试名称");
        map.put("enable", true);
        JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(map));
        assertThat(node.get("id").isIntegralNumber()).isTrue();
        assertThat(node.get("id").asLong()).isEqualTo(123L);
        assertThat(node.get("sort").asInt()).isEqualTo(7);
        assertThat(node.get("name").asText()).isEqualTo("测试名称");
        assertThat(node.get("enable").asBoolean()).isTrue();
    }

    @Test
    void 时区一致性_JVM默认时区切换不影响Instant与Date输出() throws Exception {
        TimeZone original = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            String utcInstant = objectMapper.writeValueAsString(SAMPLE_INSTANT);
            String utcDate = objectMapper.writeValueAsString(Date.from(SAMPLE_INSTANT));

            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
            String shInstant = objectMapper.writeValueAsString(SAMPLE_INSTANT);
            String shDate = objectMapper.writeValueAsString(Date.from(SAMPLE_INSTANT));

            assertThat(utcInstant).isEqualTo(shInstant);
            assertThat(utcDate).isEqualTo(shDate);
            assertThat(shInstant).isEqualTo("1793191412000");
        } finally {
            TimeZone.setDefault(original);
        }
    }
}
