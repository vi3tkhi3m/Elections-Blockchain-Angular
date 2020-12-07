package org.han.ica.oose.sneeuwklokje.exceptions;

public class NoServiceNodeException extends Exception {

    /**
     * Creates a NoServiceNodeException.
     * @param message the message of the exception.
     * @param cause the cause of the exception.
     */
    public NoServiceNodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
