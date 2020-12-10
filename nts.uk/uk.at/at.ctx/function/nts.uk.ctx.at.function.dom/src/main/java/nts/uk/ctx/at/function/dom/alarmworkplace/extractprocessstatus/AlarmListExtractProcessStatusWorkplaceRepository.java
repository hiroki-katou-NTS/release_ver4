package nts.uk.ctx.at.function.dom.alarmworkplace.extractprocessstatus;

import java.util.Optional;

public interface AlarmListExtractProcessStatusWorkplaceRepository {
	
	//List<AlarmListExtractInfoWorkplace> getAllAlListExtaProcess(String companyID);

	Optional<AlarmListExtractProcessStatusWorkplace> getBy(String companyId, String id);

	//Optional<AlarmListExtractInfoWorkplace> getAlListExtaProcess(String companyID, GeneralDate startDate, int startTime);

	//Optional<AlarmListExtractInfoWorkplace> getAlListExtaProcessByStatus(String companyID, GeneralDate startDate, int startTime, int status);

	//Optional<AlarmListExtractInfoWorkplace> getAlListExtaProcessByEndDate(String companyID, String employeeID);

    void add(AlarmListExtractProcessStatusWorkplace processStatus);

	void update(AlarmListExtractProcessStatusWorkplace processStatus);

	//void deleteAlListExtaProcess(String extraProcessStatusID);

	//boolean isAlListExtaProcessing(String companyId, String employeeId, int status);
}
