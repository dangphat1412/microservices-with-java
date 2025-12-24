package com.secure.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(name = "ErrorResponse", description = "Details about an error response")
public class ErrorResponseDto {
    @Schema(description = "The API path where the error occurred", example = "/api/v1/accounts/123")
    private String apiPath;

    @Schema(description = "The HTTP status code of the error", example = "404 NOT_FOUND")
    private HttpStatus errorCode;

    @Schema(description = "A descriptive error message", example = "Account not found with id : '123'")
    private String errorMessage;

    @Schema(description = "The timestamp when the error occurred", example = "2024-06-15T14:30:00")
    private LocalDateTime timestamp;
}
