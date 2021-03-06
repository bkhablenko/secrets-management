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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;

import javax.validation.Valid;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

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
    public ResponseEntity<?> storeCredentials(@RequestBody @Valid StoreCredentialsRequest request) {
        final String id = request.getId();
        logger.debug("Processing request to store credentials with ID: [{}]", id);
        final String username = request.getUsername();
        final String password = request.getPassword();
        credentialsService.storeCredentials(id, username, password);
        final UriComponents location = fromCurrentRequestUri().path("/{id}").buildAndExpand(id);
        return ResponseEntity.created(location.toUri()).build();
    }

    @GetMapping("/{id}")
    public RetrieveCredentialsResponse retrieveCredentials(@PathVariable String id) {
        logger.debug("Processing request to retrieve credentials with ID: [{}]", id);
        final Credentials credentials = credentialsService.retrieveCredentials(id);
        final String username = credentials.getUsername();
        final String password = credentials.getPassword();
        return new RetrieveCredentialsResponse(username, password);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCredentials(@PathVariable String id, @RequestBody @Valid UpdateCredentialsRequest request) {
        logger.debug("Processing request to update credentials with ID: [{}]", id);
        final String username = request.getUsername();
        final String password = request.getPassword();
        credentialsService.updateCredentials(id, username, password);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeCredentials(@PathVariable String id) {
        logger.debug("Processing request to revoke credentials with ID: [{}]", id);
        credentialsService.revokeCredentials(id);
    }
}
