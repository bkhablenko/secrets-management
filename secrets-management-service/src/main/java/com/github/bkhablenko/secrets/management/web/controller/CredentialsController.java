package com.github.bkhablenko.secrets.management.web.controller;

import com.github.bkhablenko.secrets.management.domain.Credentials;
import com.github.bkhablenko.secrets.management.service.CredentialsService;
import com.github.bkhablenko.secrets.management.web.model.RetrieveCredentialsResponse;
import com.github.bkhablenko.secrets.management.web.model.StoreCredentialsRequest;
import com.github.bkhablenko.secrets.management.web.model.UpdateCredentialsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/credentials")
public class CredentialsController {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsController.class);

    private final CredentialsService credentialsService;

    @Autowired
    public CredentialsController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void storeCredentials(@RequestBody @Valid StoreCredentialsRequest request) {
        final String username = request.getUsername();
        final String password = request.getPassword();
        logger.debug("Processing request to store credentials with ID: [{}]", username);
        credentialsService.storeCredentials(username, password);
    }

    @GetMapping("/{username}")
    public RetrieveCredentialsResponse retrieveCredentials(@PathVariable String username) {
        logger.debug("Processing request to retrieve credentials with ID: [{}]", username);
        final Credentials credentials = credentialsService.retrieveCredentials(username);
        final String password = credentials.getPassword();
        return new RetrieveCredentialsResponse(username, password);
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCredentials(@PathVariable String username, @RequestBody @Valid UpdateCredentialsRequest request) {
        logger.debug("Processing request to update credentials with ID: [{}]", username);
        credentialsService.updateCredentials(username, request.getPassword());
    }
}
