package com.jreyes.accountsapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class DeleteUserPhoneTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldDeleteUserPhoneSuccessfullyInDatabase() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11/phones/p1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


        mockMvc.perform(MockMvcRequestBuilders.get("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11/phones/p1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("ResourceNotFound: User with Id and Phone id a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11: p1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11 was not found"));
    }
}
