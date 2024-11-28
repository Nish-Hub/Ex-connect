package com.exconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class UserDTO {

    @NotNull(message = "userid cannot be null")
    @NotBlank(message = "userid cannot be blank")
    private String userId;

    @NotNull(message = "userName cannot be null")
    @NotBlank(message = "userName cannot be blank")
    private String userName;

    @NotNull(message = "userRoles cannot be null")
    @NotBlank(message = "userRoles cannot be blank")
    private List<String> userRoles;

    @NotNull(message = "email cannot be null")
    @NotBlank(message = "email cannot be blank")
    private String email;

    private Map<String, String> additionalParameters;

}
