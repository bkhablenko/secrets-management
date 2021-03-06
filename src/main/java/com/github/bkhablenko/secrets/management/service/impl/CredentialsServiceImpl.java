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
    public void storeCredentials(String id, String username, String password) {
        if (credentialsRepository.existsById(id)) {
            throw new CredentialsAlreadyExistException();
        }
        final Credentials credentials = new Credentials(id, username, password);
        credentialsRepository.save(credentials);
    }

    @Override
    public Credentials retrieveCredentials(String id) {
        return credentialsRepository.findById(id).orElseThrow(CredentialsNotFoundException::new);
    }

    @Override
    public void updateCredentials(String id, String username, String password) {
        if (!credentialsRepository.existsById(id)) {
            throw new CredentialsNotFoundException();
        }
        final Credentials credentials = new Credentials(id, username, password);
        credentialsRepository.save(credentials);
    }

    @Override
    public void revokeCredentials(String id) {
        if (!credentialsRepository.existsById(id)) {
            throw new CredentialsNotFoundException();
        }
        credentialsRepository.deleteById(id);
    }
}
