package org.acwar.impersonator;

import org.acwar.impersonator.beans.IntratimeUser;
import org.acwar.impersonator.configuration.IntratimeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
@ConfigurationPropertiesScan("org.acwar.impersonator.configuration")
public class ImpersonatorApplication implements CommandLineRunner {
    private Logger log = LoggerFactory.getLogger(ImpersonatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ImpersonatorApplication.class, args);
    }

    @Autowired
    private RestTemplate template;
    @Autowired
    private IntratimeProperties properties;

    @Override
    public void run(String... args) throws Exception {
        log.debug(getAccesToken());

        Date date = getCheckInTime(new Date(),properties.getCheckInHour(),properties.getCheckInDelay());
        log.debug(date.toString());

        Date breakOutTime = getBreakOutTime(date,properties.getBreakOutHour(),properties.getBreakOutDelay());
        log.debug(breakOutTime.toString());

        Date breakBackTime = getBreakBackTime(breakOutTime,properties.getBreakDuration(), properties.getBreakAlteration());
        log.debug(breakBackTime.toString());

        Date checkOutTime = getCheckOutTime(date,getTimeInBreak(breakOutTime,breakBackTime));
        log.debug(checkOutTime.toString());

    }

    public Date getCheckInTime(Date date, int checkInOur, int checkInDelay) {
        int minutesDelay =(int)(Double.valueOf(checkInDelay)*(2*Math.random()-1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, (int)(Math.random() * 60));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, checkInOur);

        calendar.add(Calendar.MINUTE,minutesDelay);

        return calendar.getTime();
    }
    public Date getBreakOutTime(Date checkInDate, int breakOutHour, int breakOutDelay){
        int minutesDelay =(int)(Double.valueOf(breakOutDelay)*(2*Math.random()-1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, (int)(Math.random() * 60));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, breakOutHour);
        calendar.add(Calendar.MINUTE,minutesDelay);
        return calendar.getTime();
    }
    public Date getBreakBackTime(Date breakOutTime, int breakDuration, int breakAlteration){
        int minutesDelay =(int)(Double.valueOf(breakAlteration)*(2*Math.random()-1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(breakOutTime);
        calendar.set(Calendar.SECOND, (int)(Math.random() * 60));
        calendar.add(Calendar.MINUTE,breakDuration);
        calendar.add(Calendar.MINUTE,breakAlteration);

        return calendar.getTime();
    }

    public Date getCheckOutTime(Date date, long timeInBreak) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, (int)(Math.random() * 60));
        calendar.add(Calendar.HOUR_OF_DAY,8);
        calendar.add(Calendar.MINUTE,30);
        calendar.add(Calendar.MINUTE, (int) timeInBreak);

        return calendar.getTime();
    }

    public long getTimeInBreak(Date breakOut, Date breakBack){
        return (breakBack.getTime() - breakOut.getTime()) / (60 * 1000) % 60;
    }

    private String getAccesToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("user", properties.getUser());
        map.add("pin", properties.getPin());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<IntratimeUser> response = template.postForEntity( properties.getLoginUrl(), request , IntratimeUser.class );

        return response.getBody().getUSER_TOKEN();
    }
}
