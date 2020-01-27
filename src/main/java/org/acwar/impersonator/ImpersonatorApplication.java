package org.acwar.impersonator;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ImpersonatorApplication implements CommandLineRunner {
    private Logger log = LoggerFactory.getLogger(ImpersonatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ImpersonatorApplication.class, args);
    }

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @Override
    public void run(String... args) throws Exception {


        HttpPost post = new HttpPost("https://newapi.intratime.es/api/user/login");

        post.setHeader("Content-Type","application/x-www-form-urlencoded; charset:utf8");
        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", "alfonso.adiego@mercury-tfs.com"));
        urlParameters.add(new BasicNameValuePair("pin", "0647"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            log.debug(EntityUtils.toString(response.getEntity()));
        }

    }
}
