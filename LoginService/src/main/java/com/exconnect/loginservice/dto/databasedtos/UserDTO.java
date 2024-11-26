package com.exconnect.loginservice.dto.databasedtos;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
@Table(indexes = {
        @Index(name = "unique_username", columnList = "userName", unique = true)
})
public class UserDTO {

    public String userName;

    public String firstName;

    public String lastName;

    public String hashPassword;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;


    public UserDTO() {

    }

    public UserDTO(String userName, String firstName, String lastName, String hashPassword, Long userId) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashPassword = hashPassword;
        this.userId = userId;
    }
}
