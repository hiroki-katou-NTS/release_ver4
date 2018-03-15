package nts.uk.ctx.at.function.dom.alarm.extraprocessstatus;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface AlarmListExtraProcessStatusRepository {
	
	List<AlarmListExtraProcessStatus> getAllAlListExtaProcess(String companyID);
	
	Optional<AlarmListExtraProcessStatus> getAlListExtaProcess(String companyID,GeneralDate startDate,int startTime);
	
	Optional<AlarmListExtraProcessStatus> getAlListExtaProcessByEndDate(String companyID, String employeeID);
	
	void addAlListExtaProcess (AlarmListExtraProcessStatus alarmListExtraProcessStatus);
	
	void updateAlListExtaProcess (AlarmListExtraProcessStatus alarmListExtraProcessStatus);
	
	void deleteAlListExtaProcess (String companyID,GeneralDate startDate,int startTime);
}
