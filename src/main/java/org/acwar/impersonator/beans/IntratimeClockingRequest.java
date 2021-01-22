package org.acwar.impersonator.beans;

import lombok.Getter;
import lombok.Setter;

public class IntratimeClockingRequest {
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_USER_ID;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_COMPANY_ID;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_TYPE;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_AMEND_OPTION;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_AMEND_PROJECT_ID;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_STATUS;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_DT_START;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_DT_END;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_COMMENTS;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_UPDATED_AT;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_CREATED_AT;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_ID;
    @Getter
    @Setter
    private String CLOCKING_REQUESTS_TARGET_CLOCKING_ID;
    @Getter
    @Setter
    private IntratimeInOutBean created_clocking;
    @Getter
    @Setter
    private Object type;
}
