package org.acwar.impersonator.exceptions;

import lombok.Getter;
import lombok.Setter;

public class JiraServiceExceptions extends Exception {

    @Getter
    @Setter
    private String message;

    public JiraServiceExceptions(String message, Throwable e) {
        super();
        setMessage(message);
    }

    public JiraServiceExceptions(String message) {
        super();
        setMessage(message);
    }

}
