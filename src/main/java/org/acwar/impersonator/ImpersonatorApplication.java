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
