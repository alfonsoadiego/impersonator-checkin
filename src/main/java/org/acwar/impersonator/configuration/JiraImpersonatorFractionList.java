package org.acwar.impersonator.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "jira")
public class JiraImpersonatorFractionList {
    @Getter @Setter
    private List<JiraImpersonationFraction> fractions;
}
