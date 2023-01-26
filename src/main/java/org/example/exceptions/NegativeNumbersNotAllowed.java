package org.example.exceptions;

public class NegativeNumbersNotAllowed extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public NegativeNumbersNotAllowed() {
    }
    public NegativeNumbersNotAllowed(String message) {
        super(message);
    }
}