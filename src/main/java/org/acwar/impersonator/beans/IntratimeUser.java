package org.acwar.impersonator.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class IntratimeUser {
    @Getter @Setter
    private String USER_ID;
    @Getter @Setter
    private String USER_COMPANY;
    @Getter @Setter
    private String USER_NAME;
    @Getter @Setter
    private String USER_EMAIL;
    @Getter @Setter
    private String USER_ROLE;
    @Getter @Setter
    private String USER_PASSWORD;
    @Getter @Setter
    private String USER_PIN_HAS_CHANGED;
    @Getter @Setter
    private String USER_LAST_ACCESS;
    @Getter @Setter
    private String USER_TOKEN;
    @Getter @Setter
    private String USER_IMAGE;
    @Getter @Setter
    private String USER_NIF;
    @Getter @Setter
    private String USER_AFFILIATION;
    @Getter @Setter
    private String USER_WORKING_TIME;
    @Getter @Setter
    private String USER_USERNAME;
    @Getter @Setter
    private String USER_PASWORD;
    @Getter @Setter
    private List<IntratimeWorkcenters> projects;
    @Getter @Setter
    private List<IntratimeWorkcenters> workcenters;
    @Getter @Setter
    private IntratimeCompany company;
}
