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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        log.debug(launchCommand(doLogin(),new Date(),IntratimeCommandsEnum.BREAKOUT));

        /*
        Date date = getCheckInTime(new Date(), properties.getCheckInHour(), properties.getCheckInDelay());
        log.debug(date.toString());

        Date breakOutTime = getBreakOutTime(date, properties.getBreakOutHour(), properties.getBreakOutDelay());
        log.debug(breakOutTime.toString());

        Date breakBackTime = getBreakBackTime(breakOutTime, properties.getBreakDuration(), properties.getBreakAlteration());
        log.debug(breakBackTime.toString());

        Date checkOutTime = getCheckOutTime(date, getTimeInBreak(breakOutTime, breakBackTime));
        log.debug(checkOutTime.toString());

         */

    }

    public Date getCheckInTime(Date date, int checkInOur, int checkInDelay) {
        int minutesDelay = (int) (Double.valueOf(checkInDelay) * (2 * Math.random() - 1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, (int) (Math.random() * 60));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, checkInOur);

        calendar.add(Calendar.MINUTE, minutesDelay);

        return calendar.getTime();
    }

    public Date getBreakOutTime(Date checkInDate, int breakOutHour, int breakOutDelay) {
        int minutesDelay = (int) (Double.valueOf(breakOutDelay) * (2 * Math.random() - 1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, (int) (Math.random() * 60));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, breakOutHour);
        calendar.add(Calendar.MINUTE, minutesDelay);
        return calendar.getTime();
    }

    public Date getBreakBackTime(Date breakOutTime, int breakDuration, int breakAlteration) {
        int minutesDelay = (int) (Double.valueOf(breakAlteration) * (2 * Math.random() - 1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(breakOutTime);
        calendar.set(Calendar.SECOND, (int) (Math.random() * 60));
        calendar.add(Calendar.MINUTE, breakDuration);
        calendar.add(Calendar.MINUTE, breakAlteration);

        return calendar.getTime();
    }

    public Date getCheckOutTime(Date date, long timeInBreak) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, (int) (Math.random() * 60));
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        calendar.add(Calendar.MINUTE, 30);
        calendar.add(Calendar.MINUTE, (int) timeInBreak);

        return calendar.getTime();
    }

    public long getTimeInBreak(Date breakOut, Date breakBack) {
        return (breakBack.getTime() - breakOut.getTime()) / (60 * 1000) % 60;
    }

    private IntratimeUser doLogin() {
        log.debug("Attempting Login");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("user", properties.getUser());
        map.add("pin", properties.getPin());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<IntratimeUser> response = template.postForEntity(properties.getLoginUrl(), request, IntratimeUser.class);

        log.debug("login successfull");
        return response.getBody();
    }

    private String launchCommand(IntratimeUser loggedUser, Date commandDate, IntratimeCommandsEnum command) {
        log.debug("Attempting command " + command.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("token", loggedUser.getUSER_TOKEN());
        headers.set("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("user_action", command.getCommandParam());
        map.add("user_timestamp", formatDate(commandDate));
        map.add("user_use_server_time", "true");
        map.add("user_gps_coordinates", "40.449355,-3.575887");
        map.add("inout_device_id", "2");
        map.add("expense_amount", "0");
        map.add("from_web", "true");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = template.postForEntity(properties.getCommandsUrl(), request, String.class);

        return response.getBody();
    }


    private String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return dateFormat.format(date);
    }
}

/*

Request URL: https://newapi.intratime.es/api/user/clocking
Request Method: POST

POST /api/user/clocking HTTP/1.1
Host: newapi.intratime.es
Connection: keep-alive
Content-Length: 209
Accept: application/vnd.apiintratime.v1+json
token: 421475eb03b61e10df6e1774be3a9a3b64fe8109
Origin: https://panel.intratime.es
User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36
DNT: 1
Content-Type: application/x-www-form-urlencoded; charset:utf8
Sec-Fetch-Site: same-site
Sec-Fetch-Mode: cors
Referer: https://panel.intratime.es/weblogin/
Accept-Encoding: gzip, deflate, br
Accept-Language: en,es;q=0.9,gl;q=0.8,de;q=0.7,en-GB;q=0.6

user_action: 0 (ENTRADA), 1 (PAUSA), 2 (VUELTA), 3 (SALIDA)
user_timestamp: 2020-01-28 09:14:30
user_use_server_time: true
user_gps_coordinates: 0,0
user_project:
user_file:
user_expense:
user_comment:
inout_device_id: 2
expense_amount: 0
from_web: true

user_action: 3
user_timestamp: 2020-01-28 09:17:24
user_use_server_time: true
user_gps_coordinates: 0,0
user_project:
user_file:
user_expense:
user_comment:
inout_device_id: 2
expense_amount: 0
from_web: true

*/

