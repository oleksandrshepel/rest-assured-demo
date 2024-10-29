package org.oshepel.api.demo.config;

import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@Builder
@ToString
public class TestConfig {

    @Builder.Default
    private TestUser testUser = TestUser
            .builder()
            .username(Optional
                    .ofNullable(System.getenv("TEST_USERNAME"))
                    .filter(StringUtils::isNoneEmpty)
                    .orElse("user"))
            .password(Optional
                    .ofNullable(System.getenv("TEST_PASSWORD"))
                    .filter(StringUtils::isNoneEmpty)
                    .orElse("password"))
            .build();
    @Builder.Default
    private String testProtocol = Optional
            .ofNullable(System.getenv("TEST_PROTOCOL"))
            .filter(StringUtils::isNoneEmpty)
            .orElse("http");
    @Builder.Default
    private String testPort = Optional
            .ofNullable(System.getenv("TEST_PORT"))
            .filter(StringUtils::isNoneEmpty)
            .orElse(null);
    @Builder.Default
    private String testHostname = Optional
            .ofNullable(System.getenv("TEST_HOSTNAME"))
            .filter(StringUtils::isNoneEmpty)
            .orElse("80");
    @Builder.Default
    private String testBaseUrl = Optional
            .ofNullable(System.getenv("TEST_BASE_URL"))
            .filter(StringUtils::isNoneEmpty)
            .orElse("");
    @Builder.Default
    private String connectTimeout = Optional
            .ofNullable(System.getenv("CONNECT_TIMEOUT"))
            .filter(StringUtils::isNoneEmpty)
            .orElse("2000");
    @Builder.Default
    private String connectRequestTimeout = Optional
            .ofNullable(System.getenv("CONNECT_REQUEST_TIMEOUT"))
            .filter(StringUtils::isNoneEmpty)
            .orElse("2000");
    @Builder.Default
    private String socketTimeout = Optional
            .ofNullable(System.getenv("SOCKET_TIMEOUT"))
            .filter(StringUtils::isNoneEmpty)
            .orElse("10000");
    @Builder.Default
    private RestAssuredConfig restAssuredConfig = RestAssuredConfig.newConfig();
    @Builder.Default
    private ContentType accept = ContentType.JSON;
    @Builder.Default
    private ContentType contentType = ContentType.JSON;
    @Builder.Default
    private Map<String, String> headers = new HashMap<>();
    @Builder.Default
    private Integer closeIdleConnectionsAfterEachResponseAfterMillis = 0;
    @Builder.Default
    private boolean reuseHttpClientInstance = false;

    public String getTestHostname() {
        if (Objects.isNull(testHostname)) {
            throw new RuntimeException("No test hostname provided");
        }
        return testHostname;
    }
}
