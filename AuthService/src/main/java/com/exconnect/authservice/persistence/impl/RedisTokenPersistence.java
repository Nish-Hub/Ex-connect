package com.exconnect.authservice.persistence.impl;


import com.exconnect.authservice.persistence.ITokenPersistence;
import com.exconnect.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
public class RedisTokenPersistence implements ITokenPersistence<String, Map> {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public boolean persistToken(String token,Map userInfoMap) {
        try {
            redisTemplate.opsForValue().set(token,userInfoMap);
            return true;
        }catch (Exception e){
            log.error("Error in saving to redis",e);
            return false;
        }

    }

    @Override
    public Map fetchToken(String token) {
        try {
            return (Map) redisTemplate.opsForValue().get(token);
        }catch (Exception e){
            log.error("Error in fetching from redis",e);
            return null;
        }


    }

    @Override
    public boolean removeToken(String token) {
        try {
            return Boolean.TRUE.equals(redisTemplate.delete(token));
        }catch (Exception e){
            log.error("Error in fetching from redis",e);
            return false;
        }
    }
}
