package com.github.bkhablenko.secrets.management.service;

import com.github.bkhablenko.secrets.management.domain.Credentials;

public interface CredentialsService {

    void storeCredentials(String id, String username, String password);

    Credentials retrieveCredentials(String id);

    void updateCredentials(String id, String username, String password);

    void revokeCredentials(String id);
}
