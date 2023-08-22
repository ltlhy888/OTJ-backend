package com.ltech.bidding.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class JsonUtil {
    @SneakyThrows
    public Object convert(String json, TypeReference typeReference) {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json, typeReference);
    };
}
