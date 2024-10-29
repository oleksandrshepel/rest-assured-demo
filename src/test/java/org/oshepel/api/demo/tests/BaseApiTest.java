package org.oshepel.api.demo.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.filter.time.TimingFilter;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.oshepel.api.demo.config.TestConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Slf4j
public class BaseApiTest {

    protected void configureRestAssured(TestConfig testConfig) {
        AuthenticationScheme authenticationScheme = defaultBasicAuthScheme(testConfig);
        RestAssured.authentication = authenticationScheme;

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(buildBaseUrl(
                        testConfig.getTestProtocol(),
                        testConfig.getTestHostname(),
                        testConfig.getTestPort(),
                        testConfig.getTestBaseUrl()
                ))
                .setConfig(testConfig.getRestAssuredConfig())
                .setAuth(authenticationScheme)
                .addHeaders(testConfig.getHeaders())
                .setAccept(testConfig.getAccept())
                .setContentType(testConfig.getContentType())
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .addFilter(new TimingFilter())
                .build();

        HttpClientConfig httpClientConfig = HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", Integer.valueOf(testConfig.getConnectTimeout()))
                .setParam("http.socket.timeout", Integer.valueOf(testConfig.getSocketTimeout()));

        if (testConfig.isReuseHttpClientInstance()) {
            httpClientConfig.reuseHttpClientInstance();
        }

        RestAssured.config = RestAssuredConfig
                .newConfig()
                .httpClient(httpClientConfig);
    }

    protected Response callServiceGet(String relativeUrl) {
        return RestAssured.get(relativeUrl);
    }

    protected Response callServiceGet(String relativeUrl, Map<String, ?> pathParams) {
        return RestAssured.get(relativeUrl, pathParams);
    }

    protected Response callServiceGet(String relativeUrl, Map<String, ?> pathParams, Headers headers) {
        return RestAssured.given().headers(headers).get(relativeUrl, pathParams);
    }

    protected Response callServiceGet(String relativeUrl, Headers headers) {
        return RestAssured.given().headers(headers).get(relativeUrl);
    }

    protected Response callServicePost(String relativeUrl, String body) {
        return RestAssured.given().body(body).post(relativeUrl);
    }

    protected Response callServicePost(String relativeUrl, String body, Map<String, ?> pathParams) {
        return RestAssured.given().body(body).post(relativeUrl, pathParams);
    }

    protected AuthenticationScheme defaultBasicAuthScheme(TestConfig testConfig) {
        PreemptiveBasicAuthScheme basicAuthScheme = new PreemptiveBasicAuthScheme();
        basicAuthScheme.setUserName(testConfig.getTestUser().getUsername());
        basicAuthScheme.setPassword(testConfig.getTestUser().getPassword());
        return basicAuthScheme;
    }

    private String buildBaseUrl(String targetScheme, String targetHostname, String targetPort, String baseUrl) {
        try {
            return new URL(String.format("%s://%s:%s%s",
                    targetScheme,
                    targetHostname,
                    targetPort,
                    baseUrl)).toString();
        } catch (MalformedURLException e) {
            log.error("Could not build url");
            throw new RuntimeException(e.getMessage());
        }
    }
}
