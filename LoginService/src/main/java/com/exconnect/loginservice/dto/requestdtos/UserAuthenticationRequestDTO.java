package com.exconnect.loginservice.dto.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthenticationRequestDTO {

    public String userName;
    public String email;

    public String hashPassword;
}
