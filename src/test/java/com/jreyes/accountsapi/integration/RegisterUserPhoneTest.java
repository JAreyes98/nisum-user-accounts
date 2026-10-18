package com.jreyes.accountsapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jreyes.accountsapi.dto.PhoneRequestDto;
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
public class RegisterUserPhoneTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUserSuccessfullyInDatabase() throws Exception {

        PhoneRequestDto phone = new PhoneRequestDto(null, "6551234", "1", "505");


        mockMvc.perform(MockMvcRequestBuilders.post("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11/phones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.number").value("6551234"))
                .andExpect(jsonPath("$.cityCode").value("1"))
                .andExpect(jsonPath("$.countryCode").value("505"));
    }

    @Test
    void shouldRegisterUserFailInvalidCountryCodeInvalidInDatabase() throws Exception {

        PhoneRequestDto phone = new PhoneRequestDto(null, "5551234", "1", "5056");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11/phones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Country code must have between 1 and 3 characters"));
    }

    @Test
    void shouldRegisterUserPhoneFailDuplicatedEmailInvalidInDatabase() throws Exception {

        PhoneRequestDto phone = new PhoneRequestDto(null, "5551234", "1", "505");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11/phones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Phone number already exists"));
    }
}
