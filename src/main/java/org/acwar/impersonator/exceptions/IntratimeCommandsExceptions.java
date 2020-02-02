package org.acwar.impersonator.exceptions;

import lombok.Getter;
import lombok.Setter;

public class IntratimeCommandsExceptions extends Exception {

    @Getter
    @Setter
    private String message;

    public IntratimeCommandsExceptions(String message, Throwable e) {
        super();
        setMessage(message);
    }

    public IntratimeCommandsExceptions(String message) {
        super();
        setMessage(message);
    }

}
