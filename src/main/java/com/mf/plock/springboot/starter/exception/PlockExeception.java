package com.mf.plock.springboot.starter.exception;

public class PlockExeception extends RuntimeException{
    public PlockExeception(String message) {
        super(message);
    }

    public PlockExeception(String message, Throwable throwable){
        super(message, throwable);
    }
}
