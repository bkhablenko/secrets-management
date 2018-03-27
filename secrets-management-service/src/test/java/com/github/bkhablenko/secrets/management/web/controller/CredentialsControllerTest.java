package com.github.bkhablenko.secrets.management.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bkhablenko.secrets.management.domain.Credentials;
import com.github.bkhablenko.secrets.management.service.CredentialsService;
import com.github.bkhablenko.secrets.management.service.exception.CredentialsAlreadyExistException;
import com.github.bkhablenko.secrets.management.service.exception.CredentialsNotFoundException;
import com.github.bkhablenko.secrets.management.web.model.StoreCredentialsRequest;
import com.github.bkhablenko.secrets.management.web.model.UpdateCredentialsRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CredentialsController.class)
public class CredentialsControllerTest {

    private static final String USERNAME = "john.smith";

    private static final String PASSWORD = "H3LLo$WoRLD!";

    @MockBean
    private CredentialsService credentialsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void storeCredentials_204() throws Exception {
        doNothing().when(credentialsService).storeCredentials(any(), any());

        final StoreCredentialsRequest request = storeCredentialsRequest(USERNAME, PASSWORD);
        mockMvc.perform(storeCredentials(request))
                .andExpect(status().isNoContent());

        verify(credentialsService).storeCredentials(USERNAME, PASSWORD);
    }

    @Test
    public void storeCredentials_409() throws Exception {
        doThrow(CredentialsAlreadyExistException.class)
                .when(credentialsService).storeCredentials(any(), any());

        final StoreCredentialsRequest request = storeCredentialsRequest(USERNAME, PASSWORD);
        mockMvc.perform(storeCredentials(request))
                .andExpect(status().isConflict());

        verify(credentialsService).storeCredentials(USERNAME, PASSWORD);
    }

    @Test
    public void retrieveCredentials_200() throws Exception {
        when(credentialsService.retrieveCredentials(any()))
                .thenReturn(new Credentials(USERNAME, PASSWORD));

        mockMvc.perform(retrieveCredentials(USERNAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", is(equalTo(USERNAME))))
                .andExpect(jsonPath("password", is(equalTo(PASSWORD))));

        verify(credentialsService).retrieveCredentials(USERNAME);
    }

    @Test
    public void retrieveCredentials_404() throws Exception {
        doThrow(CredentialsNotFoundException.class)
                .when(credentialsService).retrieveCredentials(any());

        mockMvc.perform(retrieveCredentials(USERNAME))
                .andExpect(status().isNotFound());

        verify(credentialsService).retrieveCredentials(USERNAME);
    }

    @Test
    public void updateCredentials_204() throws Exception {
        doNothing().when(credentialsService).updateCredentials(any(), any());

        final UpdateCredentialsRequest request = updateCredentialsRequest(PASSWORD);
        mockMvc.perform(updateCredentials(USERNAME, request))
                .andExpect(status().isNoContent());

        verify(credentialsService).updateCredentials(USERNAME, PASSWORD);
    }

    @Test
    public void updateCredentials_404() throws Exception {
        doThrow(CredentialsNotFoundException.class)
                .when(credentialsService).updateCredentials(any(), any());

        final UpdateCredentialsRequest request = updateCredentialsRequest(PASSWORD);
        mockMvc.perform(updateCredentials(USERNAME, request))
                .andExpect(status().isNotFound());

        verify(credentialsService).updateCredentials(USERNAME, PASSWORD);
    }

    private RequestBuilder storeCredentials(StoreCredentialsRequest request) throws Exception {
        return post("/api/v1/credentials")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request));
    }

    private RequestBuilder retrieveCredentials(String username) {
        return get("/api/v1/credentials/{username}", username);
    }

    private RequestBuilder updateCredentials(String username, UpdateCredentialsRequest request) throws Exception {
        return put("/api/v1/credentials/{username}", username)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request));
    }

    private StoreCredentialsRequest storeCredentialsRequest(String username, String password) {
        final StoreCredentialsRequest instance = new StoreCredentialsRequest();
        instance.setUsername(username);
        instance.setPassword(password);
        return instance;
    }

    private UpdateCredentialsRequest updateCredentialsRequest(String password) {
        final UpdateCredentialsRequest instance = new UpdateCredentialsRequest();
        instance.setPassword(password);
        return instance;
    }
}
