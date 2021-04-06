package org.acwar.impersonator.service;

import org.acwar.impersonator.exceptions.JiraServiceExceptions;
import org.acwar.impersonator.service.impl.JiraWorklogQueryServiceImpl;

import java.util.Calendar;
import java.util.Map;

public interface JiraWorklogQueryService {
    Integer getWorklogHours();

    JiraWorklogQueryServiceImpl configureUser(String user);

    Map<String,Integer> getJiraWorklogs(String text) throws JiraServiceExceptions;

    JiraWorklogQueryServiceImpl configureSince(Calendar calendar);
}
