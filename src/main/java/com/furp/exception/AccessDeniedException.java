package com.furp.exception;

public class AccessDeniedException extends ServiceException {
    public AccessDeniedException(String message) {
        super(message, 403);
    }
}
