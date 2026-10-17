package com.jreyes.accountsapi.controller;

import com.jreyes.accountsapi.dto.UserRequestDto;
import com.jreyes.accountsapi.dto.UserResponseDto;
import com.jreyes.accountsapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign-up")
@AllArgsConstructor
public class SingUpController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user", description = "Registers a new user along with a list of phone numbers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data or validation failure", content = @Content)
    })
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserRequestDto userRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.save(userRequest));
    }

}
