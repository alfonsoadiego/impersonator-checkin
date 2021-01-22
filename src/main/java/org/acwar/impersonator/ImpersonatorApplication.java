package org.acwar.impersonator;

import org.acwar.impersonator.beans.IntratimeClockingList;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static java.lang.System.exit;

@SpringBootApplication
@ConfigurationPropertiesScan("org.acwar.impersonator.configuration")
public class ImpersonatorApplication implements CommandLineRunner {
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

    @Override
    public void run(String... args) throws Exception {

        String[] missingDates = {"14/7/2020","15/7/2020","16/7/2020","17/7/2020","20/7/2020","21/7/2020","22/7/2020","23/7/2020","24/7/2020","27/7/2020","28/7/2020","29/7/2020","30/7/2020","31/7/2020","3/8/2020","4/8/2020","5/8/2020","6/8/2020","7/8/2020","10/8/2020","11/8/2020","12/8/2020","13/8/2020","14/8/2020","17/8/2020","18/8/2020","19/8/2020","20/8/2020","21/8/2020","24/8/2020","25/8/2020","26/8/2020","27/8/2020","28/8/2020","31/8/2020","1/9/2020","2/9/2020","3/9/2020","4/9/2020","7/9/2020","8/9/2020","9/9/2020","10/9/2020","11/9/2020","14/9/2020","15/9/2020","16/9/2020","17/9/2020","18/9/2020","21/9/2020","22/9/2020","23/9/2020","24/9/2020","25/9/2020","28/9/2020","29/9/2020","30/9/2020","1/10/2020","19/10/2020","20/10/2020","21/10/2020","22/10/2020","23/10/2020","26/10/2020","27/10/2020","28/10/2020","29/10/2020","30/10/2020","10/11/2020","11/11/2020","12/11/2020","16/11/2020","17/11/2020","18/11/2020","19/11/2020","20/11/2020","23/11/2020","24/11/2020","25/11/2020","26/11/2020","27/11/2020","30/11/2020","1/12/2020","2/12/2020","3/12/2020","4/12/2020","9/12/2020","10/12/2020","11/12/2020","14/12/2020","15/12/2020","16/12/2020","17/12/2020","18/12/2020","21/12/2020","22/12/2020","23/12/2020","24/12/2020","4/1/2021","5/1/2021","7/1/2021","8/1/2021","11/1/2021","12/1/2021","13/1/2021","14/1/2021","15/1/2021","21/1/2021"};
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        for (String missingDate:missingDates){
            IntratimeClockingList list = intratimeRequestService.queryClockingsForDate(formatter.parse(missingDate));

            Map<IntratimeCommandsEnum, Date> missingDayDates = list.getMissingCommands(properties);
            if (!missingDayDates.isEmpty()) {
                log.debug(missingDayDates.toString());
                for (IntratimeCommandsEnum command : missingDayDates.keySet()) {
                    log.debug("Missing Command " + command.toString() + " for " + missingDayDates.get(command));
                    intratimeRequestService.launchCommand(missingDayDates.get(command), command);
                }
            }else{
                log.debug(list.getDate_listed() + " is complete registered");
            }
        }

        exit(0);

        scheduler.schedule(() ->{
                    IntratimeClockingList list = null;
                    Map<IntratimeCommandsEnum, Date> missingDayDates = null;
                    try {
                        list = intratimeRequestService.queryClockingsForDate(new Date());
                        missingDayDates = list.getMissingCommands(properties);
                    } catch (IntratimeCommandsExceptions intratimeCommandsExceptions) {
                        intratimeCommandsExceptions.printStackTrace();
                        log.error("Error in parameters");
                        System.exit(1);
                    }
                    if (!missingDayDates.isEmpty()) {
                        log.debug(missingDayDates.toString());
                        for (IntratimeCommandsEnum command : missingDayDates.keySet()) {
                            log.debug("Missing Command " + command.toString() + " for " + missingDayDates.get(command));
                            scheduler.schedule(
                                    new IntratimeSchedulable(intratimeService, command),
                                    missingDayDates.get(command)
                            );
                        }
                    }else{
                        log.debug(list.getDate_listed() + " is complete registered");
                    }
                },
                new CronTrigger("0 0 1 * * MON-FRI")
        );

    }

}
