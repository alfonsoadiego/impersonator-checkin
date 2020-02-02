package org.acwar.impersonator.enums;

import lombok.Getter;
import lombok.Setter;

public enum IntratimeCommandsEnum {

    CHECKIN(0),
    BREAKOUT(2),
    BREAKBACK(3),
    CHECKOUT(1);

    IntratimeCommandsEnum(int code) {
        setCommand(code);
    }

    @Getter
    @Setter
    private int command;

    public String getCommandParam() {
        return String.valueOf(command);
    }
}
