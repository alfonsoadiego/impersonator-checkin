package org.acwar.impersonator.beans;

import lombok.Getter;
import lombok.Setter;
import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.enums.IntratimeCommandsEnum;
import org.acwar.impersonator.exceptions.IntratimeCommandsExceptions;
import org.acwar.impersonator.helpers.CommandsDatesHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntratimeClockingList {
    @Getter
    @Setter
    private IntratimeClockingListMeta meta;
    @Getter
    private Date date_listed;
    @Getter
    @Setter
    private Object links;
    @Getter
    @Setter
    private List<IntratimeClockingListInOutBean> data;

    public IntratimeClockingList setDate_listed(Date date_listed) {
        this.date_listed = date_listed;
        return this;
    }

    public Map<IntratimeCommandsEnum, Date> getMissingCommands(IntratimeProperties properties) throws IntratimeCommandsExceptions {
        if (!hasInOut(IntratimeCommandsEnum.CHECKIN)) {
            return CommandsDatesHelper.generateFullDatesSet(properties,getDate_listed());
        }
        if (hasInOut(IntratimeCommandsEnum.CHECKIN) &&
                !hasInOut(IntratimeCommandsEnum.BREAKOUT)) {
            return CommandsDatesHelper.generateDatesSet(properties,
                    getInOut(IntratimeCommandsEnum.CHECKIN));
        }
        if (hasInOut(IntratimeCommandsEnum.CHECKIN) &&
                hasInOut(IntratimeCommandsEnum.BREAKOUT) &&
                !hasInOut(IntratimeCommandsEnum.BREAKBACK)) {
            return CommandsDatesHelper.generateDatesSet(properties,
                    getInOut(IntratimeCommandsEnum.CHECKIN),
                    getInOut(IntratimeCommandsEnum.BREAKOUT));
        }
        if (hasInOut(IntratimeCommandsEnum.CHECKIN) &&
                hasInOut(IntratimeCommandsEnum.BREAKOUT) &&
                hasInOut(IntratimeCommandsEnum.BREAKBACK) &&
                !hasInOut(IntratimeCommandsEnum.CHECKOUT)) {
            return CommandsDatesHelper.generateDatesSet(properties,
                    getInOut(IntratimeCommandsEnum.CHECKIN),
                    getInOut(IntratimeCommandsEnum.BREAKOUT),
                    getInOut(IntratimeCommandsEnum.BREAKBACK));
        }
        return new HashMap<>();
    }

    private boolean hasInOut(IntratimeCommandsEnum inout){
        for (IntratimeClockingListInOutBean clocking:data){
            if (inout.getCommandParam().equals(clocking.getINOUT_TYPE()))
                return true;
        }
        return false;
    }

    private Date getInOut(IntratimeCommandsEnum inout)  {
        for (IntratimeClockingListInOutBean clocking:data){
            if (inout.getCommandParam().equals(clocking.getINOUT_TYPE())) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(clocking.getINOUT_DATE());
                } catch (ParseException e) {
                    return null;
                }
            }
        }
        return null;
    }

}
