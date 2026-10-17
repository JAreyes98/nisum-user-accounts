package com.jreyes.accountsapi.controller;

import com.jreyes.accountsapi.dto.*;
import com.jreyes.accountsapi.service.UserPhoneService;
import com.jreyes.accountsapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "Users", description = "Comprehensive user management operations including profile updates, account deletion, information retrieval, and user phone number management.")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final UserPhoneService userPhoneService;

    @GetMapping("/{id}")
    @Operation(summary = "Find user by id", description = "Find user information by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Information",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    public ResponseEntity<UserSecureResponseDto> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update user details and phone numbers",
            description = "Updates user information by ID and synchronizes phone numbers, allowing addition, modification, or removal of associated phone entries."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Update",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateRequestDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data or validation", content = @Content)
    })
    public ResponseEntity<UserSecureResponseDto> updateUser(@PathVariable("id") String id, @Valid @RequestBody UserUpdateRequestDto userRequest) {
        return ResponseEntity.ok(userService.update(id, userRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user by ID",
            description = "Permanently deletes a user account and all associated phone numbers by user ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID format or bad request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/{userId}/phones")
    @Operation(
            summary = "Get user phone numbers",
            description = "Retrieves all phone numbers associated with the specified user ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved phone numbers list",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PhoneResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request format or invalid user ID format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    public ResponseEntity<List<PhoneResponseDto>> getPhones(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userPhoneService.findAll(userId));
    }

    @PostMapping("/{userId}/phones")
    @Operation(
            summary = "Add a phone number for a user",
            description = "Associates a new phone number with the specified user ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Phone successfully added",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PhoneResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or validation failure",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    public ResponseEntity<PhoneResponseDto> addPhone(@PathVariable("userId") String userId,@Valid @RequestBody PhoneRequestDto phoneReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userPhoneService.save(userId, phoneReq));
    }

    @GetMapping("/{userId}/phones/{id}")
    @Operation(
            summary = "Get a phone number by ID",
            description = "Retrieves a specific phone number record associated with the specified user ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Phone successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PhoneResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID format or bad request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User or phone number not found",
                    content = @Content
            )
    })
    public ResponseEntity<PhoneResponseDto> getPhoneById(@PathVariable("userId") String userId, @PathVariable("id") String id) {
        return ResponseEntity.ok(userPhoneService.findById(id, userId));
    }

    @Operation(
            summary = "Update a phone number",
            description = "Updates an existing phone number record for the specified user ID and phone ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Phone successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PhoneResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or validation failure",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User or phone number not found",
                    content = @Content
            )
    })
    @PutMapping("/{userId}/phones/{id}")
    public ResponseEntity<PhoneResponseDto> updatePhone(@PathVariable("userId") String userId, @PathVariable("id") String id, @RequestBody PhoneRequestDto phoneReq) {
        return ResponseEntity.ok(userPhoneService.update(userId, id, phoneReq));
    }

    @DeleteMapping("/{userId}/phones/{id}")
    @Operation(
            summary = "Delete a phone number",
            description = "Permanently deletes a specific phone number record by its ID for the specified user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Phone successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID format or bad request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User or phone number not found",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deletePhone(@PathVariable("userId") String userId, @PathVariable("id") String id) {
        userPhoneService.deleteById(userId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
