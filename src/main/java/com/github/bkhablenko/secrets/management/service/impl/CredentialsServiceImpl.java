package com.github.bkhablenko.secrets.management.service.impl;

import com.github.bkhablenko.secrets.management.domain.Credentials;
import com.github.bkhablenko.secrets.management.domain.repository.CredentialsRepository;
import com.github.bkhablenko.secrets.management.service.CredentialsService;
import com.github.bkhablenko.secrets.management.service.exception.CredentialsAlreadyExistException;
import com.github.bkhablenko.secrets.management.service.exception.CredentialsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialsServiceImpl implements CredentialsService {

    private final CredentialsRepository credentialsRepository;

    @Autowired
    public CredentialsServiceImpl(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public void storeCredentials(String username, String password) {
        if (credentialsRepository.existsById(username)) {
            throw new CredentialsAlreadyExistException();
        }
        final Credentials credentials = new Credentials(username, password);
        credentialsRepository.save(credentials);
    }

    @Override
    public Credentials retrieveCredentials(String username) {
        return credentialsRepository.findById(username).orElseThrow(CredentialsNotFoundException::new);
    }

    @Override
    public void updateCredentials(String username, String password) {
        if (!credentialsRepository.existsById(username)) {
            throw new CredentialsNotFoundException();
        }
        final Credentials credentials = new Credentials(username, password);
        credentialsRepository.save(credentials);
    }

    @Override
    public void revokeCredentials(String username) {
        if (!credentialsRepository.existsById(username)) {
            throw new CredentialsNotFoundException();
        }
        credentialsRepository.deleteById(username);
    }
}
