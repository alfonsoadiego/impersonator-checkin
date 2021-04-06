package org.acwar.impersonator;

import org.acwar.impersonator.beans.IntratimeClockingList;
import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.configuration.JiraImpersonationFraction;
import org.acwar.impersonator.configuration.JiraImpersonatorFractionList;
import org.acwar.impersonator.enums.IntratimeCommandsEnum;
import org.acwar.impersonator.exceptions.IntratimeCommandsExceptions;
import org.acwar.impersonator.service.JiraWorklogManagementService;
import org.acwar.impersonator.service.impl.IntratimeSchedulable;
import org.acwar.impersonator.service.IntratimeService;
import org.acwar.impersonator.service.impl.JiraWorklogQueryServiceImpl;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootApplication
@ConfigurationPropertiesScan("org.acwar.impersonator.configuration")
public class ImpersonatorApplication{
    private Logger log = LoggerFactory.getLogger(ImpersonatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ImpersonatorApplication.class, args);
    }

    @Qualifier("intratimeCheckinServiceImpl")
    @Autowired
    private IntratimeService intratimeService;

    @Qualifier("intratimeCheckinRequestServiceImpl")
    @Autowired
    private IntratimeService intratimeRequestService;

    @Autowired
    private IntratimeProperties properties;
    @Autowired
    @Qualifier("localThreadPool")
    private ThreadPoolTaskScheduler scheduler;

    @Autowired
    private JiraWorklogQueryServiceImpl jira;

    @Autowired
    private JiraImpersonatorFractionList fractionList;

    @Autowired
    private JiraWorklogManagementService managementService;


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) throws Exception {

        scheduler.schedule(() ->{

                    Calendar cal = Calendar.getInstance();

                    Integer response = jira.configureSince(cal).configureUser("alfonso.adiego").getWorklogHours();

                    Integer remainingToLog = 30600 - response;

                    for (JiraImpersonationFraction fraction : fractionList.getFractions()) {
                        log.debug(fraction.getJiraKey() + ":" + String.valueOf(remainingToLog * fraction.getRatio()));
                        managementService.forDate(cal.getTime()).createLog(remainingToLog * fraction.getRatio(), fraction.getJiraKey());
                    }

                },
                new CronTrigger("0 0 21 ? * MON-FRI")
        );
        return args -> {
            log.debug("Scheduled tasks");
        };
    }

}
