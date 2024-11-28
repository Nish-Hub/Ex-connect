package com.exconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenRequest {

    @NotNull(message = "token cannot be null")
    @NotBlank(message = "token cannot be blank")
    private String token;
}
