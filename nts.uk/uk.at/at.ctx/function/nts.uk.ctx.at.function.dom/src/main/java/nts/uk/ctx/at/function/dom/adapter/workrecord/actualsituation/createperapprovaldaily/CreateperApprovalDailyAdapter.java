package nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovaldaily;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface CreateperApprovalDailyAdapter {
	public boolean createperApprovalDaily(String companyId, String executionId, List<String> employeeIDs,
			int processExecType, Integer createNewEmp, GeneralDate startDateClosure, GeneralDate endDateClosure);

	List<AppDataInfoDailyImport> getAppDataInfoDailyByExeID(String executionId);

	Optional<AppDataInfoDailyImport> getAppDataInfoDailyByID(String employeeId, String executionId);

	void addAppDataInfoDaily(AppDataInfoDailyImport appDataInfoDaily);
}
