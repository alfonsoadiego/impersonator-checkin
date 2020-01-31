package org.acwar.impersonator.helpers;

import org.acwar.impersonator.configuration.IntratimeProperties;
import org.acwar.impersonator.enums.IntratimeCommandsEnum;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandsDatesHelper {

    public static Map<IntratimeCommandsEnum,Date> generateDatesSet(IntratimeProperties properties){
        Map<IntratimeCommandsEnum,Date> response = new HashMap<>();

        Date checkInTime = getCheckInTime(new Date(), properties.getCheckInHour(), properties.getCheckInDelay());
        response.put(IntratimeCommandsEnum.CHECKIN,checkInTime);

        Date breakOutTime = getBreakOutTime(checkInTime, properties.getBreakOutHour(), properties.getBreakOutDelay());
        response.put(IntratimeCommandsEnum.BREAKOUT,breakOutTime);

        Date breakBackTime = getBreakBackTime(breakOutTime, properties.getBreakDuration(), properties.getBreakAlteration());
        response.put(IntratimeCommandsEnum.BREAKBACK,breakBackTime);

        Date checkOutTime = getCheckOutTime(checkInTime, getTimeInBreak(breakOutTime, breakBackTime));
        response.put(IntratimeCommandsEnum.CHECKOUT,checkOutTime);

        return response;
    }

    private static Date getCheckInTime(Date date, int checkInOur, int checkInDelay) {
        int minutesDelay = (int) (Double.valueOf(checkInDelay) * (2 * Math.random() - 1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, (int) (Math.random() * 60));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, checkInOur);

        calendar.add(Calendar.MINUTE, minutesDelay);

        return calendar.getTime();
    }

    private static Date getBreakOutTime(Date checkInDate, int breakOutHour, int breakOutDelay) {
        int minutesDelay = (int) (Double.valueOf(breakOutDelay) * (2 * Math.random() - 1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, (int) (Math.random() * 60));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, breakOutHour);
        calendar.add(Calendar.MINUTE, minutesDelay);
        return calendar.getTime();
    }

    private static Date getBreakBackTime(Date breakOutTime, int breakDuration, int breakAlteration) {
        int minutesDelay = (int) (Double.valueOf(breakAlteration) * (2 * Math.random() - 1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(breakOutTime);
        calendar.set(Calendar.SECOND, (int) (Math.random() * 60));
        calendar.add(Calendar.MINUTE, breakDuration);
        calendar.add(Calendar.MINUTE, breakAlteration);

        return calendar.getTime();
    }

    private static Date getCheckOutTime(Date date, long timeInBreak) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, (int) (Math.random() * 60));
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        calendar.add(Calendar.MINUTE, 30);
        calendar.add(Calendar.MINUTE, (int) timeInBreak);

        return calendar.getTime();
    }

    private static long getTimeInBreak(Date breakOut, Date breakBack) {
        return (breakBack.getTime() - breakOut.getTime()) / (60 * 1000) % 60;
    }
}
