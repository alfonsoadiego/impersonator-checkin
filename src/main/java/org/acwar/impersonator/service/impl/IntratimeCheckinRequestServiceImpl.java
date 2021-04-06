package org.acwar.impersonator.service.impl;

import org.acwar.impersonator.beans.IntratimeClockingRequestResponse;
import org.acwar.impersonator.beans.IntratimeInOutBean;
import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.enums.IntratimeCommandsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service("intratimeCheckinRequestServiceImpl")
public class IntratimeCheckinRequestServiceImpl extends IntratimeAbstractService {

    @Autowired
    private IntratimeProperties properties;
    @Autowired
    private RestTemplate template;

    /**
     * Query for chekin times and request for the missing ones.
     *
     * @param commandDate
     * @param command Unused, ignored
     * @return
     */
    @Override
    public IntratimeInOutBean launchCommand(Date commandDate, IntratimeCommandsEnum command) {
        log.debug("Attempting request command " + command.toString());

        if ("true".equals(properties.getDryRun()))
            return new IntratimeInOutBean();

        HttpHeaders headers = getHttpHeaders();
        if (headers == null) return null;

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("clocking_request_type", "2");
        map.add("clocking_request_message", "Olvidos");
        map.add("clocking_request_manual_option", command.getCommandParam());
        map.add("date_start", formatDate(commandDate));
        map.add("date_end", formatDate(commandDate));
        map.add("user_project", "");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<IntratimeClockingRequestResponse> response;
        try {
            response = template.postForEntity(properties.getRequestCommandsUrl(), request, IntratimeClockingRequestResponse.class);
            if (response.getStatusCode().is2xxSuccessful())
                return response.getBody().getClocking_request().getCreated_clocking();
            else
                log.error("Communication problems:" + response.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
