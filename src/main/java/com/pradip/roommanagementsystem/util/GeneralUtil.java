package com.pradip.roommanagementsystem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneralUtil {
    @Autowired
    private ObjectMapper mapper;

    public <T, U> U convertObject(T object, Class<U> clazz) {
        return mapper.convertValue(object, clazz);
    }
}
