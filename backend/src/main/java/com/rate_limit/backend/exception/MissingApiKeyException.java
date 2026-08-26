package com.rate_limit.backend.exception;

import com.rate_limit.backend.entities.ApiKey;
public class MissingApiKeyException extends RuntimeException {
    
    public MissingApiKeyException(ApiKey key)
    {
        super(key + " is missing");
    }
}
