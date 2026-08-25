package com.rate_limit.backend.exception;

import java.util.UUID;

public class ApiKeyNotFoundException extends RuntimeException{
    
    public ApiKeyNotFoundException(UUID Key)
    {
        super(Key +" not found");
    }
}
