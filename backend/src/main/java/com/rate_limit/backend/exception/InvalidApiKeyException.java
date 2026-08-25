package com.rate_limit.backend.exception;

public class InvalidApiKeyException extends RuntimeException {

    public InvalidApiKeyException(String apiKeyHash)
    {
        super(apiKeyHash + " is not valid");
    }

}
