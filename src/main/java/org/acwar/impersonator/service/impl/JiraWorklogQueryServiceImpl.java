package org.acwar.impersonator.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.acwar.impersonator.exceptions.JiraServiceExceptions;
import org.acwar.impersonator.beans.worklogid.request.WorkLogIdRequest;
import org.acwar.impersonator.beans.WorklogUpdated;
import org.acwar.impersonator.beans.WorklogUpdatedList;
import org.acwar.impersonator.beans.worklogid.response.Author;
import org.acwar.impersonator.beans.worklogid.response.WorklogIdResponse;
import org.acwar.impersonator.service.JiraWorklogQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class JiraWorklogQueryServiceImpl implements JiraWorklogQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraWorklogQueryServiceImpl.class);

    private Map<String,Map<String,Integer>> peopleMaps;

    private Calendar sinceCalendar;
    private String user;

    @Value("${jira.apiUrl:http://jira.mercury-tfs.com/rest/api/2/}")
    public String JIRA_URL;
    private String JIRA_API_WORKLOG = JIRA_URL + "worklog/";

    @Value("${jira.userPassword:none}")
    @Getter @Setter
    private String jiraPassword;

    @PostConstruct
    public void JiraWorklogQueryServicePost(){
        LOGGER.debug("JiraWorklogQueryServiceImpl instiated. ");
        JIRA_API_WORKLOG = JIRA_URL + "worklog/";
        LOGGER.debug("Using " + JIRA_API_WORKLOG);
    }

    public JiraWorklogQueryService setPass(String jiraPassword){
        setJiraPassword(jiraPassword);
        return this;
    }

    @Override
    public Integer getWorklogHours(){
        Map<String,Integer> response = new HashMap<>();
        try{
            response = getJiraWorklogs(user);
        } catch (JiraServiceExceptions jiraServiceExceptions) {
            LOGGER.error("Error getting worlogs", jiraServiceExceptions);
        }
        return (response.get(new SimpleDateFormat("yyyy-MM-dd").format(sinceCalendar.getTime()))==null)?0:response.get(new SimpleDateFormat("yyyy-MM-dd").format(sinceCalendar.getTime()));
    }

    @Override
    public Map<String,Integer> getJiraWorklogs(String user) throws JiraServiceExceptions {

        peopleMaps = new HashMap<>();

        try {

            HttpHeaders httpHeaders = buildHttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("body", httpHeaders);
            final RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<WorklogUpdatedList> response = restTemplate.exchange(JIRA_API_WORKLOG + "updated?since="+getTimeStamp(), HttpMethod.GET, entity, WorklogUpdatedList.class);
            processWorklogs(response.getBody().getValues());

            while (!response.getBody().getLastPage()){
                LOGGER.debug(getDate(response.getBody().getSince()) + "-" + getDate(response.getBody().getUntil()));
                response = restTemplate.exchange(response.getBody().getNextPage(), HttpMethod.GET, entity, WorklogUpdatedList.class);
                processWorklogs(response.getBody().getValues());
            }
            LOGGER.debug(response.toString());
            Map<String, Integer> stringIntegerMap = peopleMaps.get(user);
            return (stringIntegerMap!=null)?stringIntegerMap:new HashMap<>();

        } catch (Exception e) {
            LOGGER.error("Error exchanging issue: " + user, e);
            throw new JiraServiceExceptions(e.getMessage(), e);
        }
    }
    @Override
    public JiraWorklogQueryServiceImpl configureUser(String user){
        this.user = user;
        return this;
    }

    @Override
    public JiraWorklogQueryServiceImpl configureSince(Calendar calendar){
        sinceCalendar = calendar;
        return this;
    }
    private void processWorklogs(List<WorklogUpdated> values) {

        RestTemplate restTemplate = new RestTemplate();
        List<Integer> ids;
        ids = new ArrayList<>();

        for (WorklogUpdated log: values)
            ids.add(log.getWorklogId());

        WorkLogIdRequest workLogIdRequest = new WorkLogIdRequest();
        workLogIdRequest.setIds(ids);

        HttpEntity<WorkLogIdRequest> request = new HttpEntity<>(workLogIdRequest, buildHttpHeaders());
        try{
            ResponseEntity<WorklogIdResponse[]> response = restTemplate.postForEntity("http://jira.mercury-tfs.com/rest/api/2/worklog/list",
                        request,
                        WorklogIdResponse[].class);
            storeImputationsMap(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void storeImputationsMap(WorklogIdResponse[] body) {
        for(WorklogIdResponse response:body){

            Map<String,Integer> personImpute = getAuthorMap(response.getAuthor());

            if (personImpute.get(clearHours(response.getStarted()))==null)
                personImpute.put(clearHours(response.getStarted()),0);

            personImpute.put(clearHours(response.getStarted()),
                    personImpute.get(clearHours(response.getStarted())) + response.getTimeSpentSeconds());


        }
    }

    private String clearHours(String date){
        return date.substring(0,10);
    }

    private Map<String, Integer> getAuthorMap(Author author) {

        if (peopleMaps.get(author.getName())==null)
            peopleMaps.put(author.getName(), new HashMap<String, Integer>());

        return peopleMaps.get(author.getName());
    }

    private String getDate(Timestamp time){
        Date date = new Date(time.getTime());
        return date.toString();
    }
    private long getTimeStamp(){
        if (sinceCalendar== null) {
            sinceCalendar = Calendar.getInstance();
            sinceCalendar.set(2021, 0, 1);
        }
        Calendar response = (Calendar) sinceCalendar.clone();
        response.add(Calendar.DATE,-1);
        return response.getTime().getTime();
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setBasicAuth("alfonso.adiego",jiraPassword);
        httpHeader.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeader.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return httpHeader;
    }


}
