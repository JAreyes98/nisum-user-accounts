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
@AutoConfigureMockMvc
@Transactional
public class RegisterUserTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUserSuccessfullyInDatabase() throws Exception {

        PhoneRequestDto phone = new PhoneRequestDto(null, "5551234", "1", "505");
        UserRequestDto request = new UserRequestDto(
                "Josue Reyes",
                "josuedavid@example.com",
                "Password123!",
                List.of(phone)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("josuedavid@example.com"))
                .andExpect(jsonPath("$.name").value("Josue Reyes"))
                .andExpect(jsonPath("$.token").exists()) // Verifica que generó el JWT real
                .andExpect(jsonPath("$.phones[0].id").exists())
                .andExpect(jsonPath("$.phones[0].number").value("5551234"))
                .andExpect(jsonPath("$.phones[0].cityCode").value("1"))
                .andExpect(jsonPath("$.phones[0].countryCode").value("505"));
    }

    @Test
    void shouldRegisterUserFailEmailInvalidInDatabase() throws Exception {

        PhoneRequestDto phone = new PhoneRequestDto(null, "5551234", "1", "505");
        UserRequestDto request = new UserRequestDto(
                "Josue Reyes",
                "josuedavid",
                "Password123!",
                List.of(phone)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("must be a well-formed email address"));
    }

    @Test
    void shouldRegisterUserFailDuplicatedEmailInvalidInDatabase() throws Exception {

        PhoneRequestDto phone = new PhoneRequestDto(null, "5551234", "1", "505");
        UserRequestDto request = new UserRequestDto(
                "Josue Reyes",
                "josue@example.com",
                "Password123!",
                List.of(phone)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("User with email josue@example.com already exists"));
    }
    @Test
    void shouldRegisterUserFailPasswordInvalidInDatabase() throws Exception {

        PhoneRequestDto phone = new PhoneRequestDto(null, "5551234", "1", "505");
        UserRequestDto request = new UserRequestDto(
                "Josue Reyes",
                "josue@example.com",
                "12345645565!",
                List.of(phone)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character (@#$%^&+=!._-)"));
    }
    @Test
    void shouldRegisterUserFailPasswordLengthInvalidInDatabase() throws Exception {

        PhoneRequestDto phone = new PhoneRequestDto(null, "5551234", "1", "505");
        UserRequestDto request = new UserRequestDto(
                "Josue Reyes",
                "josue@example.com",
                "123@Ma",
                List.of(phone)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Password must be at least 8 characters long"));
    }
}
