package com.github.bkhablenko.secrets.management.service.impl;

import com.github.bkhablenko.secrets.management.domain.Credentials;
import com.github.bkhablenko.secrets.management.domain.repository.CredentialsRepository;
import com.github.bkhablenko.secrets.management.service.exception.CredentialsAlreadyExistException;
import com.github.bkhablenko.secrets.management.service.exception.CredentialsNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CredentialsServiceImplTest {

    private static final String ID = "gmail";

    private static final String USERNAME = "john.smith@gmail.com";

    private static final String PASSWORD = "H3LLo$WoRLD!";

    private final CredentialsRepository credentialsRepository = mock(CredentialsRepository.class);

    private final CredentialsServiceImpl credentialsService = new CredentialsServiceImpl(credentialsRepository);

    private static Optional<Credentials> credentials(String id, String username, String password) {
        return Optional.of(new Credentials(id, username, password));
    }

    @Test(expected = CredentialsAlreadyExistException.class)
    public void storeCredentials_shouldThrowException_ifCredentialsAlreadyExist() {
        when(credentialsRepository.existsById(any())).thenThrow(new CredentialsAlreadyExistException());
        credentialsService.storeCredentials(ID, USERNAME, PASSWORD);
    }

    @Test
    public void storeCredentials_shouldPersistNewEntity_onSuccess() {
        when(credentialsRepository.existsById(any())).thenReturn(false);
        credentialsService.storeCredentials(ID, USERNAME, PASSWORD);
        final ArgumentCaptor<Credentials> arg = ArgumentCaptor.forClass(Credentials.class);
        verify(credentialsRepository).save(arg.capture());
        final Credentials credentials = arg.getValue();
        assertThat(credentials, is(notNullValue()));
        assertThat(credentials.getId(), is(equalTo(ID)));
        assertThat(credentials.getUsername(), is(equalTo(USERNAME)));
        assertThat(credentials.getPassword(), is(equalTo(PASSWORD)));
    }

    @Test(expected = CredentialsNotFoundException.class)
    public void retrieveCredentials_shouldThrowException_ifCredentialsNotFound() {
        when(credentialsRepository.findById(any())).thenReturn(Optional.empty());
        credentialsService.retrieveCredentials(ID);
    }

    @Test
    public void retrieveCredentials_shouldReturnExistingEntity_onSuccess() {
        when(credentialsRepository.findById(any())).thenReturn(credentials(ID, USERNAME, PASSWORD));
        final Credentials credentials = credentialsService.retrieveCredentials(ID);
        verify(credentialsRepository).findById(ID);
        assertThat(credentials, is(notNullValue()));
        assertThat(credentials.getUsername(), is(equalTo(USERNAME)));
        assertThat(credentials.getPassword(), is(equalTo(PASSWORD)));
    }

    @Test(expected = CredentialsNotFoundException.class)
    public void updateCredentials_shouldThrowException_ifCredentialsNotFound() {
        when(credentialsRepository.existsById(any())).thenReturn(false);
        final String updatedPassword = StringUtils.reverse(PASSWORD);
        credentialsService.updateCredentials(ID, USERNAME, updatedPassword);
    }

    @Test
    public void updateCredentials_shouldUpdateExistingEntity_onSuccess() {
        when(credentialsRepository.existsById(any())).thenReturn(true);
        final String updatedPassword = StringUtils.reverse(PASSWORD);
        credentialsService.updateCredentials(ID, USERNAME, updatedPassword);
        final ArgumentCaptor<Credentials> arg = ArgumentCaptor.forClass(Credentials.class);
        verify(credentialsRepository).save(arg.capture());
        final Credentials credentials = arg.getValue();
        assertThat(credentials, is(notNullValue()));
        assertThat(credentials.getId(), is(equalTo(ID)));
        assertThat(credentials.getUsername(), is(equalTo(USERNAME)));
        assertThat(credentials.getPassword(), is(equalTo(updatedPassword)));
    }

    @Test(expected = CredentialsNotFoundException.class)
    public void revokeCredentials_shouldThrowException_ifCredentialsNotFound() {
        when(credentialsRepository.existsById(any())).thenReturn(false);
        credentialsService.revokeCredentials(ID);
    }

    @Test
    public void revokeCredentials_shouldDeleteExistingEntity_onSuccess() {
        when(credentialsRepository.existsById(any())).thenReturn(true);
        credentialsService.revokeCredentials(ID);
        verify(credentialsRepository).deleteById(ID);
    }
}
