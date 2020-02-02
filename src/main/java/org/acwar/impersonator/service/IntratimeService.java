package org.acwar.impersonator.service;

import org.acwar.impersonator.beans.IntratimeInOutBean;
import org.acwar.impersonator.enums.IntratimeCommandsEnum;

import java.util.Date;

public interface IntratimeService {
    IntratimeInOutBean launchCommand(Date commandDate, IntratimeCommandsEnum command);
}
