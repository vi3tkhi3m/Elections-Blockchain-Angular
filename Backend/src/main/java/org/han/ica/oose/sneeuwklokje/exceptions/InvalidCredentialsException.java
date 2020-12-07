package org.han.ica.oose.sneeuwklokje.exceptions;

public class InvalidCredentialsException extends Exception {

    /**
     * Creates a InvalidCredentialsException.
     * @param message the message of the exception.
     * @param cause the cause of the exception.
     */
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
