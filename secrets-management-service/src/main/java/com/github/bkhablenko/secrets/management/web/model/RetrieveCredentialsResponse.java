package com.github.bkhablenko.secrets.management.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RetrieveCredentialsResponse {

    @JsonProperty("username")
    private final String username;

    @JsonProperty("password")
    private final String password;

    public RetrieveCredentialsResponse(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
