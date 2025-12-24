package com.secure.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Accounts", description = "Details about an account")
public class AccountsDto {
    @NotEmpty(message = "Account number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
    @Schema(description = "Account Number of Bank Account", example = "1234567890")
    private Long accountNumber;

    @NotEmpty(message = "Account type cannot be null or empty")
    @Schema(description = "Account type of Bank Account", example = "Savings")
    private String accountType;

    @NotEmpty(message = "Branch address cannot be null or empty")
    @Schema(description = "Branch address of Bank Account", example = "123 Main St")
    private String branchAddress;
}
