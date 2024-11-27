package com.exconnect.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {

    private String userId;
    private String userName;
    private List<String> userRoles;
    private String email;
}
