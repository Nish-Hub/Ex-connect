package com.exconnect.loginservice.dao;


import com.exconnect.loginservice.dto.databasedtos.UserDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends CrudRepository<UserDTO, Long> {


    UserDTO findByUserName(String userName);
}
