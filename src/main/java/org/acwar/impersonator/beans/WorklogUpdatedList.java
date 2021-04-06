package org.acwar.impersonator.beans;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

public class WorklogUpdatedList {

    @Getter @Setter
    private List<WorklogUpdated> values;

    @Getter @Setter
    private Timestamp since;
    @Getter @Setter
    private Timestamp until;
    @Getter @Setter
    private String self;
    @Getter @Setter
    private String nextPage;
    @Getter @Setter
    private Boolean lastPage;

}
