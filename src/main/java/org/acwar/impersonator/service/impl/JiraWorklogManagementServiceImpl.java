package org.acwar.impersonator.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.acwar.impersonator.beans.worklogissue.request.Worklog;
import org.acwar.impersonator.beans.worklogissue.response.WorklogResponse;
import org.acwar.impersonator.service.JiraWorklogManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class JiraWorklogManagementServiceImpl implements JiraWorklogManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraWorklogManagementServiceImpl.class);

    @Value("${jira.apiUrl:http://jira.mercury-tfs.com/rest/api/2/}")
    public String JIRA_URL;
    private String JIRA_API_ISSUES;

    private Date requestsDate;

    @Value("${jira.userPassword:none}")
    @Getter @Setter
    private String jiraPassword;

    public JiraWorklogManagementService setPass(String jiraPassword){
        setJiraPassword(jiraPassword);
        return this;
    }

    @PostConstruct
    public void JiraWorklogManagementServicePost(){
        LOGGER.debug("JiraWorklogManagementServiceImpl instiated");
        JIRA_API_ISSUES =  JIRA_URL + "issue/";
        LOGGER.debug("Using " + JIRA_API_ISSUES);
    }

    @Override
    public boolean createLog(double timeToLog, String jiraKey, String message) {
        RestTemplate restTemplate = new RestTemplate();

        Worklog log = new Worklog();
        log.setComment(message);
        log.setTimeSpentSeconds((int) Math.round(timeToLog));
        log.setStarted(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(requestsDate));

        HttpEntity<Worklog> request = new HttpEntity<>(log,buildHttpHeaders());
        try{
            ResponseEntity<WorklogResponse> response = restTemplate.postForEntity(
                    JIRA_API_ISSUES +jiraKey+"/worklog",
                    request,
                    WorklogResponse.class
            );
        } catch (Exception e) {
            LOGGER.error("Unable to create Log:"+ e.getMessage());
        }
        return false;
    }

    @Override
    public JiraWorklogManagementService forDate(Date date){
        this.requestsDate = date;
        return this;
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setBasicAuth("alfonso.adiego",jiraPassword);
        httpHeader.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeader.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return httpHeader;
    }

}
