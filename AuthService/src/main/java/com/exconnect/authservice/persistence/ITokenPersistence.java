package com.exconnect.authservice.persistence;

import com.exconnect.dto.UserDTO;

import java.util.Map;

public interface ITokenPersistence<T,U extends Map> {

    public boolean persistToken(T token,U userInfoMap);

    public U fetchToken(T token);

    public boolean removeToken(T token);



}
