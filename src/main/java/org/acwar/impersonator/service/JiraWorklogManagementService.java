package org.acwar.impersonator.service;

import org.acwar.impersonator.exceptions.JiraServiceExceptions;
import org.acwar.impersonator.service.impl.JiraWorklogQueryServiceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public interface JiraWorklogManagementService {
    boolean createLog(double timeToLog, String jiraKey,String message);

    JiraWorklogManagementService forDate(Date date);
}
