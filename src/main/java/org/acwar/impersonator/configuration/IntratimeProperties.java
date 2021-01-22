package org.acwar.impersonator.configuration;

import lombok.Getter;
import lombok.Setter;

public class IntratimeProperties {

    @Getter
    @Setter
    private String user;
    @Getter
    @Setter
    private String pin;
    @Getter
    @Setter
    private String loginUrl;
    @Getter
    @Setter
    private String commandsUrl;

    @Getter
    @Setter
    private String requestCommandsUrl;

    @Getter
    @Setter
    private String queryUrl;

    @Getter
    @Setter
    private String dryRun;

    /*
    Hora general de entrada y horquilla de desfase
     */
    @Getter
    @Setter
    private int checkInDelay;
    @Getter
    @Setter
    private int checkInHour;

    /*
    Hora general de salida a comer, horquilla desfase,
    duracion comida y horquilla desfase duracion
     */
    @Getter
    @Setter
    private int breakOutHour;
    @Getter
    @Setter
    private int breakOutDelay;
    @Getter
    @Setter
    private int breakDuration;
    @Getter
    @Setter
    private int breakAlteration;

}
