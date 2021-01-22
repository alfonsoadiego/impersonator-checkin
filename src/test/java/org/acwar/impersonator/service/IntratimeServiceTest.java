package org.acwar.impersonator.service;

import org.acwar.impersonator.beans.IntratimeInOutBean;
import org.acwar.impersonator.beans.IntratimeUser;
import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.enums.IntratimeCommandsEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class IntratimeServiceTest {

    @Autowired
    private IntratimeService serviceToTest;

    @Autowired
    private IntratimeProperties properties;

    @TestConfiguration
    static class config{

        @Bean
        public IntratimeService service(){
            return new IntratimeCheckinServiceImpl();
        }
        @Bean
        public IntratimeProperties properties(){
            IntratimeProperties response = new IntratimeProperties();
            response.setDryRun("true");
            response.setCommandsUrl("commandURL");
            response.setUser("TestUser");
            response.setPin("TestPin");
            response.setLoginUrl("loginUrl");
            return response;

        }

    }
    @MockBean
    private RestTemplate template;

    @Test
    public void testDryRun(){
        properties.setDryRun("true");
        serviceToTest.launchCommand(new Date(), IntratimeCommandsEnum.CHECKIN);

    }
    @Test
    public void testAllOk(){

        IntratimeUser userResponse = new IntratimeUser();
        userResponse.setUSER_TOKEN("A TOKEN");

        properties.setDryRun("false");

        Mockito.when(template.postForEntity(Mockito.eq(properties.getLoginUrl()),Mockito.anyObject(),Mockito.anyObject())).thenReturn(new ResponseEntity<>(userResponse, HttpStatus.OK));
        Mockito.when(template.postForEntity(Mockito.eq(properties.getCommandsUrl()),Mockito.anyObject(),Mockito.anyObject())).thenReturn(new ResponseEntity<>(new IntratimeInOutBean(), HttpStatus.OK));

        assertNotNull(serviceToTest.launchCommand(new Date(), IntratimeCommandsEnum.CHECKIN));

    }
    @Test
    public void testCommProblems(){

        IntratimeUser userResponse = new IntratimeUser();
        userResponse.setUSER_TOKEN("A TOKEN");

        properties.setDryRun("false");

        Mockito.when(template.postForEntity(Mockito.eq(properties.getLoginUrl()),Mockito.anyObject(),Mockito.anyObject())).thenReturn(new ResponseEntity<>(userResponse, HttpStatus.OK));
        Mockito.when(template.postForEntity(Mockito.eq(properties.getCommandsUrl()),Mockito.anyObject(),Mockito.anyObject())).thenReturn(new ResponseEntity<>(new IntratimeInOutBean(), HttpStatus.INTERNAL_SERVER_ERROR));

        assertNull(serviceToTest.launchCommand(new Date(), IntratimeCommandsEnum.CHECKIN));

    }

    @Test
    public void testFailingHeaders(){

        IntratimeUser userResponse = new IntratimeUser();
        userResponse.setUSER_TOKEN("A TOKEN");

        properties.setDryRun("false");

        Mockito.when(template.postForEntity(Mockito.eq(properties.getLoginUrl()),Mockito.anyObject(),Mockito.anyObject())).thenReturn(new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST));
        Mockito.when(template.postForEntity(Mockito.eq(properties.getCommandsUrl()),Mockito.anyObject(),Mockito.anyObject())).thenReturn(new ResponseEntity<>(new IntratimeInOutBean(), HttpStatus.OK));

        assertNull(serviceToTest.launchCommand(new Date(), IntratimeCommandsEnum.CHECKIN));

    }

    @Test
    public void testCommException(){

        IntratimeUser userResponse = new IntratimeUser();
        userResponse.setUSER_TOKEN("A TOKEN");

        properties.setDryRun("false");

        Mockito.when(template.postForEntity(Mockito.eq(properties.getLoginUrl()),Mockito.anyObject(),Mockito.anyObject())).thenReturn(new ResponseEntity<>(userResponse, HttpStatus.OK));
        Mockito.when(template.postForEntity(Mockito.eq(properties.getCommandsUrl()),Mockito.anyObject(),Mockito.anyObject())).thenThrow(new RestClientException("Test Exception"));

        assertNull(serviceToTest.launchCommand(new Date(), IntratimeCommandsEnum.CHECKIN));

    }

    @Test
    public void testSchedulablaObject(){
        IntratimeSchedulable schedulable = new IntratimeSchedulable(serviceToTest,IntratimeCommandsEnum.CHECKIN);
        IntratimeUser userResponse = new IntratimeUser();
        userResponse.setUSER_TOKEN("A TOKEN");

        properties.setDryRun("false");

        Mockito.when(template.postForEntity(Mockito.eq(properties.getLoginUrl()),Mockito.anyObject(),Mockito.anyObject())).thenReturn(new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST));
        Mockito.when(template.postForEntity(Mockito.eq(properties.getCommandsUrl()),Mockito.anyObject(),Mockito.anyObject())).thenReturn(new ResponseEntity<>(new IntratimeInOutBean(), HttpStatus.OK));

        schedulable.run();

    }


}
