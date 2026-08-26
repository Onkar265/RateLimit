package com.rate_limit.backend.exception;

import com.rate_limit.backend.entity.ApiKey;
public class RevokedApiKeyException extends RuntimeException{
    
    public RevokedApiKeyException(ApiKey key)
    {
        super(key + " was revoked");
    }
}
