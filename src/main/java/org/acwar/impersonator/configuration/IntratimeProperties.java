package org.acwar.impersonator.configuration;

import lombok.Getter;
import lombok.Setter;

public class IntratimeProperties {

    @Getter @Setter
    private String user;
    @Getter @Setter
    private String pin;
    @Getter @Setter
    private String loginUrl;
    @Getter @Setter
    private String commandsUrl;

}
