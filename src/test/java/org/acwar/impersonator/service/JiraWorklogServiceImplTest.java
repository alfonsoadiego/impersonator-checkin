package org.acwar.impersonator.service;

import org.acwar.impersonator.configuration.JiraImpersonationFraction;
import org.acwar.impersonator.exceptions.JiraServiceExceptions;
import org.acwar.impersonator.service.impl.JiraWorklogManagementServiceImpl;
import org.acwar.impersonator.service.impl.JiraWorklogQueryServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

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
            response.add(fraction);
            fraction = new JiraImpersonationFraction();
            fraction.setJiraKey("SANGTS-110");
            fraction.setRatio(0.75);
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
    private JiraWorklogManagementService managementService;

    @Test
    public void testWorklog() throws JiraServiceExceptions, ParseException {

        String[] missingDates = {"1/3/2021","2/3/2021","3/3/2021","4/3/2021","5/3/2021","8/3/2021","9/3/2021","10/3/2021","11/3/2021","12/3/2021","15/3/2021","16/3/2021","17/3/2021","18/3/2021","19/3/2021","24/3/2021","25/3/2021"};//"4/1/2021","5/1/2021","7/1/2021","8/1/2021","11/1/2021","12/1/2021","13/1/2021","14/1/2021","15/1/2021","21/1/2021","22/1/2021","25/1/2021","26/1/2021","27/1/2021","28/1/2021","29/1/2021","1/2/2021","2/2/2021","3/2/2021","4/2/2021","5/2/2021","8/2/2021","9/2/2021"};
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        for (String date: missingDates) {

            formatter.parse(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(formatter.parse(date));

            Integer response = jira.configureSince(cal).configureUser("alfonso.adiego").getWorklogHours();

            Integer remainingToLog = 30600 - response;

            for (JiraImpersonationFraction fraction : fractionList) {
                LOGGER.debug(fraction.getJiraKey() + ":" + String.valueOf(remainingToLog * fraction.getRatio()));
                managementService.forDate(cal.getTime()).createLog(remainingToLog * fraction.getRatio(), fraction.getJiraKey());
            }
            assertNotNull(response);
        }
        //Process from startDate to Today
    }

    /*

     */
}
