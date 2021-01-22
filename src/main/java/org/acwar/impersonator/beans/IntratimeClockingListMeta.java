package org.acwar.impersonator.beans;

import lombok.Getter;
import lombok.Setter;

public class IntratimeClockingListMeta {
    @Getter
    @Setter
    private Object page;
    @Getter
    @Setter
    private int total_time_seconds;
    @Getter
    @Setter
    private String total_time;
    @Getter
    @Setter
    private String total_amount;
}
