package org.acwar.impersonator;

import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.enums.IntratimeCommandsEnum;
import org.acwar.impersonator.helpers.CommandsDatesHelper;
import org.acwar.impersonator.service.IntratimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.Map;

@SpringBootApplication
@ConfigurationPropertiesScan("org.acwar.impersonator.configuration")
public class ImpersonatorApplication implements CommandLineRunner {
    private Logger log = LoggerFactory.getLogger(ImpersonatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ImpersonatorApplication.class, args);
    }

    @Autowired
    private IntratimeService intratimeService;
    @Autowired
    private IntratimeProperties properties;
    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    @Override
    public void run(String... args) throws Exception {
        log.debug(intratimeService.launchCommand(new Date(), IntratimeCommandsEnum.CHECKOUT).toString());
        Map<IntratimeCommandsEnum,Date> dayDates = CommandsDatesHelper.generateDatesSet(properties);

        scheduler.schedule(
                intratimeService.launchCommand(new Date(), IntratimeCommandsEnum.CHECKIN),
                new Date()
        );

    }

}
