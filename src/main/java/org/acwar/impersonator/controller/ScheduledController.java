package org.acwar.impersonator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.TimeUnit;

@RestController
public class ScheduledController {
    protected Logger log = LoggerFactory.getLogger(ScheduledController.class);

    @Autowired
    @Qualifier("localThreadPool")
    private ThreadPoolTaskScheduler scheduler;

    @RequestMapping("/")
    public String index() {
        StringBuilder builder = new StringBuilder();
        Iterator<Runnable> scheduledJobs = scheduler.getScheduledThreadPoolExecutor().getQueue().iterator();
        Calendar cal = Calendar.getInstance();
        while(scheduledJobs.hasNext()) {
            cal.add(Calendar.MILLISECOND,(int) ((RunnableScheduledFuture)scheduledJobs.next()).getDelay(TimeUnit.MILLISECONDS));
            builder.append("A job is pending to execute at " + cal.getTime());
        }

        return builder.toString();

    }

}
