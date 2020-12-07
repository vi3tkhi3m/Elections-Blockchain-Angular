package org.han.ica.oose.sneeuwklokje.exceptions;

public class NoPlannedElectionException extends Exception {

    public NoPlannedElectionException() {
    }

    public NoPlannedElectionException(String message) {
        super(message);
    }

    public NoPlannedElectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPlannedElectionException(Throwable cause) {
        super(cause);
    }

    public NoPlannedElectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
