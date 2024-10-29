package org.oshepel.api.demo.config;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestUser {

    private String username;
    private String password;

}
