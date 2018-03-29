package com.github.bkhablenko.secrets.management.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class StoreCredentialsRequest {

    @JsonProperty("username")
    @NotBlank
    private String username;

    @JsonProperty("password")
    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
