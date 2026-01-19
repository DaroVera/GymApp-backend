package com.gymapp.backend.exception;

//Esta clase representa un error 404 personalizado

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
