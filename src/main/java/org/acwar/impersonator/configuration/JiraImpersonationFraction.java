package org.acwar.impersonator.configuration;

import lombok.Getter;
import lombok.Setter;


public class JiraImpersonationFraction {

    @Getter @Setter
    private Double ratio;

    @Getter @Setter
    private String jiraKey;

}
