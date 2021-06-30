package org.acwar.impersonator;

import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.configuration.JiraImpersonationFraction;
import org.acwar.impersonator.configuration.JiraImpersonatorFractionList;
import org.acwar.impersonator.service.IntratimeService;
import org.acwar.impersonator.service.JiraWorklogManagementService;
import org.acwar.impersonator.service.JiraWorklogQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Calendar;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootApplication
@ConfigurationPropertiesScan("org.acwar.impersonator.configuration")
public class ImpersonatorApplication{
    private Logger log = LoggerFactory.getLogger(ImpersonatorApplication.class);

    public ImpersonatorApplication(@Qualifier("localThreadPool") ThreadPoolTaskScheduler scheduler,
                                   JiraWorklogQueryService jira,
                                   JiraImpersonatorFractionList fractionList,
                                   JiraWorklogManagementService managementService) {
        this.scheduler = scheduler;
        this.jira = jira;
        this.fractionList = fractionList;
        this.managementService = managementService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ImpersonatorApplication.class, args);
    }

    private final ThreadPoolTaskScheduler scheduler;
    private final JiraWorklogQueryService jira;
    private final JiraImpersonatorFractionList fractionList;
    private final JiraWorklogManagementService managementService;


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) throws Exception {

        log.debug("Using " + fractionList.toString());

        scheduler.schedule(() ->{

                    Calendar cal = Calendar.getInstance();

                    Integer response = jira.configureSince(cal).configureUser("alfonso.adiego").getWorklogHours();


                    Integer remainingToLog;
                    if (Calendar.FRIDAY == cal.get(Calendar.DAY_OF_WEEK))
                        remainingToLog = 21600 - response;
                    else
                        remainingToLog = 30600 - response;

                    for (JiraImpersonationFraction fraction : fractionList.getFractions()) {
                        log.debug(fraction.getJiraKey() + ":" + String.valueOf(remainingToLog * fraction.getRatio()));
                        managementService.forDate(cal.getTime()).createLog(remainingToLog * fraction.getRatio(), fraction.getJiraKey(), fraction.getMessage());
                    }

                },
                new CronTrigger("0 0 20 ? * MON-FRI")
        );
        return args -> {
            log.debug("Scheduled tasks");
        };
    }

}
