package com.rate_limit.backend.exception;

import com.rate_limit.backend.entity.ApiKey;
public class MissingApiKeyException extends RuntimeException {
    
    public MissingApiKeyException(ApiKey key)
    {
        super(key + " is missing");
    }

    public MissingApiKeyException()
    {
        super("key is missing");
    }
}
