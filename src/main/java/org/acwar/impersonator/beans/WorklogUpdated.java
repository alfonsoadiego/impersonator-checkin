package org.acwar.impersonator.beans;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

public class WorklogUpdated {
    @Getter @Setter
    private int worklogId;

    @Getter @Setter
    private Timestamp updatedTime;
}
