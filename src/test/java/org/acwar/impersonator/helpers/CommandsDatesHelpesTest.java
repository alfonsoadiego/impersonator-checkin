package org.acwar.impersonator.helpers;

import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.exceptions.IntratimeCommandsExceptions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class CommandsDatesHelpesTest {

    @Autowired
    private IntratimeProperties properties;

    @TestConfiguration
    static class config{

        @Bean
        public IntratimeProperties properties(){
            IntratimeProperties response = new IntratimeProperties();
            response.setCheckInHour(9);
            response.setCheckInDelay(10);

            response.setBreakOutHour(10);
            response.setBreakOutDelay(10);

            response.setBreakDuration(30);
            response.setBreakAlteration(10);

            return response;

        }
    }

    private void resettProperties(IntratimeProperties properties){
        properties.setCheckInHour(9);
        properties.setCheckInDelay(10);

        properties.setBreakOutHour(10);
        properties.setBreakOutDelay(10);

        properties.setBreakDuration(30);
        properties.setBreakAlteration(10);
    }

    @Test
    public void testAllOk() throws IntratimeCommandsExceptions {
        assertNotNull(CommandsDatesHelper.generateFullDatesSet(properties));
    }

    @Test
    public  void testKOScenarios(){
        properties.setCheckInHour(-1);
        try{
            CommandsDatesHelper.generateFullDatesSet(properties);
        }catch (Exception e){
            assertEquals(IntratimeCommandsExceptions.class, e.getClass());
        }

        resettProperties(properties);
        properties.setCheckInDelay(-1);
        try{
            CommandsDatesHelper.generateFullDatesSet(properties);
        }catch (Exception e){
            assertEquals(IntratimeCommandsExceptions.class, e.getClass());
        }
        resettProperties(properties);
        properties.setBreakOutHour(-1);
        try{
            CommandsDatesHelper.generateFullDatesSet(properties);
        }catch (Exception e){
            assertEquals(IntratimeCommandsExceptions.class, e.getClass());
        }
        resettProperties(properties);
        properties.setBreakOutDelay(-1);
        try{
            CommandsDatesHelper.generateFullDatesSet(properties);
        }catch (Exception e){
            assertEquals(IntratimeCommandsExceptions.class, e.getClass());
        }
        resettProperties(properties);
        properties.setBreakDuration(-1);
        try{
            CommandsDatesHelper.generateFullDatesSet(properties);
        }catch (Exception e){
            assertEquals(IntratimeCommandsExceptions.class, e.getClass());
        }
        resettProperties(properties);
        properties.setBreakAlteration(-1);
        try{
            CommandsDatesHelper.generateFullDatesSet(properties);
        }catch (Exception e){
            assertEquals(IntratimeCommandsExceptions.class, e.getClass());
        }

        resettProperties(properties);
        properties.setCheckInHour(13);
        try{
            CommandsDatesHelper.generateFullDatesSet(properties);
        }catch (Exception e){
            assertEquals(IntratimeCommandsExceptions.class, e.getClass());
        }

    }


}
