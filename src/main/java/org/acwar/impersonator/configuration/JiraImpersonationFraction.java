package org.acwar.impersonator.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class JiraImpersonationFraction {

    @Getter @Setter
    private Double ratio;

    @Getter @Setter
    private String jiraKey;

    @Getter @Setter
    private String message;

}
