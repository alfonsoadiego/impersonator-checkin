package org.acwar.impersonator;

import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.enums.IntratimeCommandsEnum;
import org.acwar.impersonator.exceptions.IntratimeCommandsExceptions;
import org.acwar.impersonator.helpers.CommandsDatesHelper;
import org.acwar.impersonator.service.IntratimeSchedulable;
import org.acwar.impersonator.service.IntratimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Date;
import java.util.Map;

@SpringBootApplication
@ConfigurationPropertiesScan("org.acwar.impersonator.configuration")
public class ImpersonatorApplication implements CommandLineRunner {
    private Logger log = LoggerFactory.getLogger(ImpersonatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ImpersonatorApplication.class, args);
    }

    @Qualifier("intratimeServiceImpl")
    @Autowired
    private IntratimeService intratimeService;
    @Autowired
    private IntratimeProperties properties;
    @Autowired
    @Qualifier("localThreadPool")
    private ThreadPoolTaskScheduler scheduler;

    @Override
    public void run(String... args) throws Exception {

        scheduler.schedule(new Runnable() {
                               @Override
                               public void run() {
                                   Map<IntratimeCommandsEnum, Date> dayDates = null;
                                   try {
                                       dayDates = CommandsDatesHelper.generateDatesSet(properties);
                                   } catch (IntratimeCommandsExceptions intratimeCommandsExceptions) {
                                       intratimeCommandsExceptions.printStackTrace();
                                       log.error("Error in parameters");
                                       System.exit(1);
                                   }
                                   for (IntratimeCommandsEnum command : dayDates.keySet()) {
                                       log.debug("Command " + command.toString() + " scheduled for " + dayDates.get(command));
                                       scheduler.schedule(
                                               new IntratimeSchedulable(intratimeService, command),
                                               dayDates.get(command)
                                       );
                                   }
                               }
                           },
                new CronTrigger("0 1 * * 1-5 *")
        );
    }

}
