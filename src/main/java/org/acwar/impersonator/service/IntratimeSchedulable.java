package org.acwar.impersonator.service;

import lombok.Getter;
import lombok.Setter;
import org.acwar.impersonator.enums.IntratimeCommandsEnum;

import java.util.Date;

public class IntratimeSchedulable implements Runnable {

    @Getter @Setter
    private IntratimeService commandsLauncher;
    @Getter @Setter
    private IntratimeCommandsEnum action;

    public IntratimeSchedulable (IntratimeService commandsLauncher, IntratimeCommandsEnum action){
        setCommandsLauncher(commandsLauncher);
        setAction(action);
    }
    @Override
    public void run() {
        commandsLauncher.launchCommand(new Date(),action);
    }
}
