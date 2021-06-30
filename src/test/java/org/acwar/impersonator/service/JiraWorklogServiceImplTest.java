package org.acwar.impersonator.service;

import org.acwar.impersonator.configuration.JiraImpersonationFraction;
import org.acwar.impersonator.exceptions.JiraServiceExceptions;
import org.acwar.impersonator.service.impl.JiraWorklogManagementServiceImpl;
import org.acwar.impersonator.service.impl.JiraWorklogQueryServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class JiraWorklogServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraWorklogQueryServiceImpl.class);

    @TestConfiguration
    static class config{

        @Bean
        public JiraWorklogQueryServiceImpl service(){
            return new JiraWorklogQueryServiceImpl();
        }

        @Bean
        public List<JiraImpersonationFraction> list(){
            List<JiraImpersonationFraction> response = new ArrayList<>();

            JiraImpersonationFraction fraction = new JiraImpersonationFraction();
            fraction.setJiraKey("SANGTS-1095");
            fraction.setRatio(0.25);
            fraction.setMessage("Dailys y reuniones con cliente");
            response.add(fraction);
            fraction = new JiraImpersonationFraction();
            fraction.setJiraKey("SANGTS-110");
            fraction.setRatio(0.5);
            fraction.setMessage("Soporte a equipo e incidencias");
            response.add(fraction);
            fraction = new JiraImpersonationFraction();
            fraction.setJiraKey("SANGTS-110");
            fraction.setRatio(0.25);
            fraction.setMessage("Arquitectura y diseño. Documentacion");
            response.add(fraction);
            return  response;
        }

        @Bean
        public JiraWorklogManagementService management(){ return new JiraWorklogManagementServiceImpl(); }
    }
    @Autowired
    private JiraWorklogQueryServiceImpl jira;

    @Autowired
    private List<JiraImpersonationFraction> fractionList;

    @Autowired
    private JiraWorklogManagementServiceImpl managementService;

    @Test
    public void testWorklog() throws JiraServiceExceptions, ParseException {

        String[] missingDates = {"1/6/2021","2/6/2021","3/6/2021","4/6/2021","7/6/2021","8/6/2021","9/6/2021","10/6/2021","11/6/2021","14/6/2021","15/6/2021","16/6/2021","17/6/2021","18/6/2021","21/6/2021","22/6/2021","23/6/2021","24/6/2021","25/6/2021","28/6/2021","29/6/2021","30/6/2021"};
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        for (String date: missingDates) {

            formatter.parse(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(formatter.parse(date));

            Integer response = jira.configureSince(cal).configureUser("alfonso.adiego").setPass("CES1704tsk.").getWorklogHours();

            Integer remainingToLog = 30600 - response;

            for (JiraImpersonationFraction fraction : fractionList) {
                LOGGER.debug(fraction.getJiraKey() + ":" + String.valueOf(remainingToLog * fraction.getRatio()));
                managementService
                        .setPass("lePass")
                        .forDate(cal.getTime())
                        .createLog(remainingToLog * fraction.getRatio(), fraction.getJiraKey(), fraction.getMessage());
            }
            assertNotNull(response);
        }
        //Process from startDate to Today
    }

    /*

     */
}
