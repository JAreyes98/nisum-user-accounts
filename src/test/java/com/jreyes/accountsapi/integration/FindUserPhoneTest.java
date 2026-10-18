package com.jreyes.accountsapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class FindUserPhoneTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFindUserPhoneSuccessfullyInDatabase() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11/phones/p1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void shouldNotFindUserPhoneInDatabase() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11/phones/b1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("ResourceNotFound: User with Id and Phone id a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11: b1 was not found"));


    }

}
