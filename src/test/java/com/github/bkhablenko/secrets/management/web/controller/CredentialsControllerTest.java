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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MoreMockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MoreMockMvcResultMatchers.locationHeader;
import static org.springframework.test.web.servlet.result.MoreMockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CredentialsController.class)
public class CredentialsControllerTest {

    private static final String ID = "gmail";

    private static final String USERNAME = "john.smith@gmail.com";

    private static final String PASSWORD = "H3LLo$WoRLD!";

    @MockBean
    private CredentialsService credentialsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void storeCredentials_201() throws Exception {
        doNothing().when(credentialsService).storeCredentials(any(), any(), any());

        final StoreCredentialsRequest request = storeCredentialsRequest(ID, USERNAME, PASSWORD);
        mockMvc.perform(storeCredentials(request))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(locationHeader().endsWith("/api/v1/credentials/" + ID));

        verify(credentialsService).storeCredentials(ID, USERNAME, PASSWORD);
    }

    @Test
    public void storeCredentials_409() throws Exception {
        doThrow(CredentialsAlreadyExistException.class)
                .when(credentialsService).storeCredentials(any(), any(), any());

        final StoreCredentialsRequest request = storeCredentialsRequest(ID, USERNAME, PASSWORD);
        mockMvc.perform(storeCredentials(request))
                .andDo(print())
                .andExpect(status().isConflict());

        verify(credentialsService).storeCredentials(ID, USERNAME, PASSWORD);
    }

    @Test
    public void retrieveCredentials_200() throws Exception {
        when(credentialsService.retrieveCredentials(any()))
                .thenReturn(new Credentials(ID, USERNAME, PASSWORD));

        mockMvc.perform(retrieveCredentials(ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", is(equalTo(USERNAME))))
                .andExpect(jsonPath("password", is(equalTo(PASSWORD))));

        verify(credentialsService).retrieveCredentials(ID);
    }

    @Test
    public void retrieveCredentials_404() throws Exception {
        doThrow(CredentialsNotFoundException.class)
                .when(credentialsService).retrieveCredentials(any());

        mockMvc.perform(retrieveCredentials(ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(credentialsService).retrieveCredentials(ID);
    }

    @Test
    public void updateCredentials_204() throws Exception {
        doNothing().when(credentialsService).updateCredentials(any(), any(), any());

        final UpdateCredentialsRequest request = updateCredentialsRequest(USERNAME, PASSWORD);
        mockMvc.perform(updateCredentials(ID, request))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(credentialsService).updateCredentials(ID, USERNAME, PASSWORD);
    }

    @Test
    public void updateCredentials_404() throws Exception {
        doThrow(CredentialsNotFoundException.class)
                .when(credentialsService).updateCredentials(any(), any(), any());

        final UpdateCredentialsRequest request = updateCredentialsRequest(USERNAME, PASSWORD);
        mockMvc.perform(updateCredentials(ID, request))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(credentialsService).updateCredentials(ID, USERNAME, PASSWORD);
    }

    @Test
    public void revokeCredentials_204() throws Exception {
        doNothing().when(credentialsService).revokeCredentials(any());

        mockMvc.perform(revokeCredentials(ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(credentialsService).revokeCredentials(ID);
    }

    @Test
    public void revokeCredentials_404() throws Exception {
        doThrow(CredentialsNotFoundException.class)
                .when(credentialsService).revokeCredentials(any());

        mockMvc.perform(revokeCredentials(ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(credentialsService).revokeCredentials(ID);
    }

    private RequestBuilder storeCredentials(StoreCredentialsRequest request) throws Exception {
        return post("/api/v1/credentials")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request));
    }

    private RequestBuilder retrieveCredentials(String id) {
        return get("/api/v1/credentials/{id}", id);
    }

    private RequestBuilder updateCredentials(String id, UpdateCredentialsRequest request) throws Exception {
        return put("/api/v1/credentials/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request));
    }

    private RequestBuilder revokeCredentials(String id) {
        return delete("/api/v1/credentials/{id}", id);
    }

    private StoreCredentialsRequest storeCredentialsRequest(String id, String username, String password) {
        final StoreCredentialsRequest instance = new StoreCredentialsRequest();
        instance.setId(id);
        instance.setUsername(username);
        instance.setPassword(password);
        return instance;
    }

    private UpdateCredentialsRequest updateCredentialsRequest(String username, String password) {
        final UpdateCredentialsRequest instance = new UpdateCredentialsRequest();
        instance.setUsername(username);
        instance.setPassword(password);
        return instance;
    }
}
