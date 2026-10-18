package com.jreyes.accountsapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jreyes.accountsapi.dto.PhoneRequestDto;
import com.jreyes.accountsapi.dto.UserRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class UpdateUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldUpdateUserSuccessfullyInDatabase() throws Exception {

        PhoneRequestDto phone = new PhoneRequestDto(null, "5551234", "22", "505");
        UserRequestDto request = new UserRequestDto(
                "Josue Reyes",
                "josuedavid@example.com",
                "Password123!",
                List.of(phone)
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("josuedavid@example.com"))
                .andExpect(jsonPath("$.name").value("Josue Reyes"))
                .andExpect(jsonPath("$.phones[0].id").exists())
                .andExpect(jsonPath("$.phones[0].number").value("5551234"))
                .andExpect(jsonPath("$.phones[0].cityCode").value("22"))
                .andExpect(jsonPath("$.phones[0].countryCode").value("505"));
    }

    @Test
    void shouldUpdateUserPhonesSuccessfullyInDatabase() throws Exception {

        PhoneRequestDto phone1 = new PhoneRequestDto(null, "5551234", "22", "505");
        PhoneRequestDto phone2 = new PhoneRequestDto(null, "5551235", "22", "505");
        UserRequestDto request = new UserRequestDto(
                "Josue Reyes",
                "josuedavid@example.com",
                "Password123!",
                List.of(phone1, phone2)
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("josuedavid@example.com"))
                .andExpect(jsonPath("$.name").value("Josue Reyes"))
                .andExpect(jsonPath("$.phones[0].id").exists())
                .andExpect(jsonPath("$.phones[0].number").value("5551234"))
                .andExpect(jsonPath("$.phones[0].cityCode").value("22"))
                .andExpect(jsonPath("$.phones[0].countryCode").value("505"))
                .andExpect(jsonPath("$.phones[1].id").exists())
                .andExpect(jsonPath("$.phones[1].number").value("5551235"))
                .andExpect(jsonPath("$.phones[1].cityCode").value("22"))
                .andExpect(jsonPath("$.phones[1].countryCode").value("505"));
    }
}
