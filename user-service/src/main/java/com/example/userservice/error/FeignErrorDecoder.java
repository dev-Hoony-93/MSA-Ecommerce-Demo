package com.example.userservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {

    private final Environment env;

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 400:
                break;
            case 404:
                if(methodKey.contains("getOrders")){
                    throw new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            env.getProperty("order-service.exception.order_is_empty"));
                }
                break;
        }
        return null;
    }
}
