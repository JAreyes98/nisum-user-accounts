package com.jreyes.accountsapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jreyes.accountsapi.dto.PhoneRequestDto;
import com.jreyes.accountsapi.dto.PhoneResponseDto;
import com.jreyes.accountsapi.dto.UserRequestDto;
import com.jreyes.accountsapi.dto.UserResponseDto;
import com.jreyes.accountsapi.service.JWTService;
import com.jreyes.accountsapi.service.UserService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = SingUpController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class SingUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    static Stream<Arguments> signUpTestScenarios() {
        PhoneRequestDto validPhone = new PhoneRequestDto(null, "5551234", "1", "505");

        return Stream.of(
                Arguments.of(
                        "Success Case 1",
                        new UserRequestDto("Josue Reyes", "josue@example.com", "Password123!", List.of(validPhone)),
                        201,
                        null
                ),
                Arguments.of(
                        "Success Case 2 - Multiple phones",
                        new UserRequestDto("Ana Gomez", "ana.gomez@test.com", "SecurePass456!", List.of(validPhone, new PhoneRequestDto(null, "88887777", "2", "505"))),
                        201,
                        null
                ),
                Arguments.of(
                        "Success Case 3 - No phones",
                        new UserRequestDto("Carlos Perez", "carlos@mail.com", "AdminKey789@", Collections.emptyList()),
                        201,
                        null
                ),
                Arguments.of(
                        "Error: Name too short (< 5 characters)",
                        new UserRequestDto("Jos", "josue@example.com", "Password123!", List.of(validPhone)),
                        400,
                        "Name must be at least 5 characters long"
                ),
                Arguments.of(
                        "Name must be at least 5 characters long",
                        new UserRequestDto("fre", "josue@example.com", "Password123!", List.of(validPhone)),
                        400,
                        "Name must be at least 5 characters long"
                ),
                Arguments.of(
                        "Error: Invalid email (missing @)",
                        new UserRequestDto("Josue Reyes", "josue-example.com", "Password123!", List.of(validPhone)),
                        400,
                        "must be a well-formed email address"
                ),
                Arguments.of(
                        "Error: Blank email",
                        new UserRequestDto("Josue Reyes", "", "Password123!", List.of(validPhone)),
                        400,
                        "Email is required"
                ),
                Arguments.of(
                        "Error: Password too short (< 8 characters)",
                        new UserRequestDto("Josue Reyes", "josue@example.com", "Pass1!", List.of(validPhone)),
                        400,
                        "Password must be at least 8 characters long"
                ),
                Arguments.of(
                        "Error: Password missing uppercase/special character",
                        new UserRequestDto("Josue Reyes", "josue@example.com", "password123", List.of(validPhone)),
                        400,
                        "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character (@#$%^&+=!._-)"
                ),
                Arguments.of(
                        "Error: Phone missing country code",
                        new UserRequestDto("Josue Reyes", "josue@example.com", "Password123!", List.of(new PhoneRequestDto(null, "5551234", "1", ""))),
                        400,
                        "Country code must have between 1 and 3 characters"
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("signUpTestScenarios")
    void testSignUpScenarios(String testName, UserRequestDto requestDto, int expectedStatus, String expectedErrorMessage) throws Exception {
        if (expectedStatus == 201) {
            List<PhoneResponseDto> phoneResponses = requestDto.phones() != null ?
                    requestDto.phones().stream().map(p -> new PhoneResponseDto("id-phone", p.number(), p.cityCode(), p.countryCode())).toList()
                    : Collections.emptyList();

            UserResponseDto response = new UserResponseDto(
                    "1",
                    requestDto.name(),
                    requestDto.email(),
                    "mock-jwt-token",
                    new Date(),
                    new Date(),
                    phoneResponses
            );
            when(userService.save(any(UserRequestDto.class))).thenReturn(response);
        }

        var performRequest = mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        if (expectedStatus == 201) {
            performRequest.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value(requestDto.email()))
                    .andExpect(jsonPath("$.name").value(requestDto.name()))
                    .andExpect(jsonPath("$.token").value("mock-jwt-token"));
        } else {
            performRequest.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").value(expectedErrorMessage));
        }
    }
}