package com.exconnect.loginservice.dto.requestdtos;

import com.exconnect.loginservice.dto.databasedtos.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequestDTO {

    public String userName;

    public String email;

    public String hashPassword;

    public String firstName;

    public String middleName;

    public String lastName;

    public UserDTO getUserDto() {
        return UserDTO.builder().
                userName(userName).firstName(firstName).lastName(lastName).hashPassword(hashPassword).build();
    }

}
