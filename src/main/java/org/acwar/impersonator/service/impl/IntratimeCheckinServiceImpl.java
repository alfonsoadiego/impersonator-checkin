package org.acwar.impersonator.service.impl;

import org.acwar.impersonator.beans.IntratimeInOutBean;
import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.enums.IntratimeCommandsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service("intratimeCheckinServiceImpl")
public class IntratimeCheckinServiceImpl extends IntratimeAbstractService {
    private Logger log = LoggerFactory.getLogger(IntratimeCheckinServiceImpl.class);

    @Autowired
    private IntratimeProperties properties;
    @Autowired
    private RestTemplate template;

    @Override
    public IntratimeInOutBean launchCommand(Date commandDate, IntratimeCommandsEnum command) {
        log.debug("Attempting command " + command.toString());

        if ("true".equals(properties.getDryRun()))
            return new IntratimeInOutBean();

        HttpHeaders headers = getHttpHeaders();
        if (headers == null) return null;

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("user_action", command.getCommandParam());
        map.add("user_timestamp", formatDate(new Date()));
        map.add("user_use_server_time", "true");
        map.add("user_gps_coordinates", "40.4489959,-3.6475087");
        map.add("inout_device_id", "2");
        map.add("expense_amount", "0");
        map.add("from_web", "true");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<IntratimeInOutBean> response;
        try {
            response = template.postForEntity(properties.getCommandsUrl(), request, IntratimeInOutBean.class);
            if (response.getStatusCode().is2xxSuccessful())
                return response.getBody();
            else
                log.error("Communication problems:" + response.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
