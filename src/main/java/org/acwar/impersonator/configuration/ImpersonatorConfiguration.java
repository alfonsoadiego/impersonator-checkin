package org.acwar.impersonator.configuration;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
public class ImpersonatorConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "intratime")
    IntratimeProperties properties(){
        return new IntratimeProperties();
    }

    @Bean
    RestTemplate objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(0,converter);

        return template;
    }
}
