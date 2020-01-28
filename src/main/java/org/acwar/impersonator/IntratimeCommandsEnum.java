package org.acwar.impersonator;

import lombok.Getter;
import lombok.Setter;

public enum IntratimeCommandsEnum {

    CHECKIN(0),
    BREAKOUT(1),
    BREAKBACK(2),
    CHECKOUT(3);

    private  IntratimeCommandsEnum(int code){
        setCommand(code);
    }

    @Getter @Setter
    private int command;

    public String getCommandParam(){
        return String.valueOf(command);
    }
}
