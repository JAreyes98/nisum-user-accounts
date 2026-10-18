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

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class FindUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFindUserSuccessfullyInDatabase() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Josue Reyes"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("josue@example.com"));


    }

}
