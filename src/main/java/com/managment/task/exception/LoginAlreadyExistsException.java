package com.managment.task.exception;

public class LoginAlreadyExistsException extends RuntimeException{
    public LoginAlreadyExistsException(String message){
        super(message);
    }
}
