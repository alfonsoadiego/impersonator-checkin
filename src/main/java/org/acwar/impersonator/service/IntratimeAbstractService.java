package org.acwar.impersonator.service;

import org.acwar.impersonator.beans.IntratimeClockingList;
import org.acwar.impersonator.beans.IntratimeUser;
import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.exceptions.IntratimeCommandsExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public abstract class IntratimeAbstractService implements IntratimeService {
    protected Logger log = LoggerFactory.getLogger(IntratimeAbstractService.class);

    @Autowired
    private IntratimeProperties properties;
    @Autowired
    private RestTemplate template;

    protected HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("token", doLogin().getUSER_TOKEN());
            headers.set("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        } catch (IntratimeCommandsExceptions e) {
            log.error("Unable to do login request.");
            return null;
        }
        return headers;
    }

    protected IntratimeUser doLogin() throws IntratimeCommandsExceptions {
        log.trace("Attempting Login");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("user", properties.getUser());
        map.add("pin", properties.getPin());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<IntratimeUser> response = template.postForEntity(properties.getLoginUrl(), request, IntratimeUser.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.trace("login successfull");
            return response.getBody();
        } else
            throw new IntratimeCommandsExceptions("Error doing login", null);
    }

    @Override
    public IntratimeClockingList queryClockingsForDate(Date commandDate) {

        HttpHeaders headers = getHttpHeaders();
        if (headers == null) return new IntratimeClockingList();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(properties.getQueryUrl())
                .queryParam("type", "0,1,2,3")
                .queryParam("limit", "25")
                .queryParam("page", "1")
                .queryParam("from", formatStartOfDate(commandDate))
                .queryParam("to", formatEndOfDate(commandDate));

        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<IntratimeClockingList> response;
        try {
            response = template.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    request,
                    IntratimeClockingList.class);

            if (response.getStatusCode().is2xxSuccessful())
                return Objects.requireNonNull(response.getBody()).setDate_listed(commandDate);
            else
                log.error("Communication problems:" + response.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new IntratimeClockingList();
    }

    protected String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
    protected String formatStartOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
    protected String formatEndOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 59, 59);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
}
