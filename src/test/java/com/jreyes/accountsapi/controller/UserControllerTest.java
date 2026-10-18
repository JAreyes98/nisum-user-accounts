package com.jreyes.accountsapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jreyes.accountsapi.dto.PhoneRequestDto;
import com.jreyes.accountsapi.dto.PhoneResponseDto;
import com.jreyes.accountsapi.dto.UserSecureResponseDto;
import com.jreyes.accountsapi.dto.UserUpdateRequestDto;
import com.jreyes.accountsapi.service.JWTService;
import com.jreyes.accountsapi.service.UserPhoneService;
import com.jreyes.accountsapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = UserController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @MockBean
        private UserPhoneService userPhoneService;

        @MockBean
        private JWTService jwtService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void shouldGetUserSuccessfully() throws Exception {
                List<PhoneResponseDto> phones = List.of(new PhoneResponseDto("p1", "5551234", "1", "505"));
                UserSecureResponseDto response = new UserSecureResponseDto("1", "Josue Reyes", "josue@example.com", new Date(), new Date(), phones);

                when(userService.findById("1")).thenReturn(response);

                mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value("1"))
                        .andExpect(jsonPath("$.name").value("Josue Reyes"))
                        .andExpect(jsonPath("$.email").value("josue@example.com"))
                        .andExpect(jsonPath("$.phones[0].id").value("p1"))
                        .andExpect(jsonPath("$.phones[0].number").value("5551234"))
                        .andExpect(jsonPath("$.phones[0].cityCode").value("1"))
                        .andExpect(jsonPath("$.phones[0].countryCode").value("505"));
        }

        @Test
        void shouldUpdateUserSuccessfully() throws Exception {
                PhoneRequestDto phone = new PhoneRequestDto(null, "5551234", "1", "505");
                UserUpdateRequestDto request = new UserUpdateRequestDto("Josue David", "josue.david@example.com", "Password123!", List.of(phone));
                List<PhoneResponseDto> phones = List.of(new PhoneResponseDto("p1", "5551234", "1", "505"));
                UserSecureResponseDto response = new UserSecureResponseDto("1", "Josue David", "josue.david@example.com", new Date(), new Date(), phones);

                when(userService.update(anyString(), any())).thenReturn(response);

                mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("Josue David"));
        }

        @Test
        void shouldFailUpdateUserWhenNameIsTooShort() throws Exception {
                PhoneRequestDto phone = new PhoneRequestDto(null, "5551234", "1", "505");
                UserUpdateRequestDto request = new UserUpdateRequestDto("Jos", "josue@example.com", "Password123!", List.of(phone));

                mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.mensaje").value("Name must be at least 5 characters long"));
        }

        @Test
        void shouldDeleteUserSuccessfully() throws Exception {
                doNothing().when(userService).deleteById("1");

                mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                        .andExpect(status().isNoContent());
        }

        @Test
        void shouldGetPhonesSuccessfully() throws Exception {
                List<PhoneResponseDto> phones = List.of(new PhoneResponseDto("p1", "5551234", "1", "505"));

                when(userPhoneService.findAll("1")).thenReturn(phones);

                mockMvc.perform(MockMvcRequestBuilders.get("/users/1/phones"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].number").value("5551234"));
        }

        @Test
        void shouldAddPhoneSuccessfully() throws Exception {
                PhoneRequestDto request = new PhoneRequestDto(null, "5551234", "1", "505");
                PhoneResponseDto response = new PhoneResponseDto("p1", "5551234", "1", "505");

                when(userPhoneService.save(anyString(), any())).thenReturn(response);

                mockMvc.perform(MockMvcRequestBuilders.post("/users/1/phones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").value("p1"));
        }

        @Test
        void shouldDeletePhoneSuccessfully() throws Exception {
                doNothing().when(userPhoneService).deleteById("1", "p1");

                mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/phones/p1"))
                        .andExpect(status().isNoContent());
        }
}