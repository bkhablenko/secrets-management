package com.github.bkhablenko.secrets.management.service;

import com.github.bkhablenko.secrets.management.domain.Credentials;

public interface CredentialsService {

    void storeCredentials(String username, String password);

    Credentials retrieveCredentials(String username);

    void updateCredentials(String username, String password);
}
