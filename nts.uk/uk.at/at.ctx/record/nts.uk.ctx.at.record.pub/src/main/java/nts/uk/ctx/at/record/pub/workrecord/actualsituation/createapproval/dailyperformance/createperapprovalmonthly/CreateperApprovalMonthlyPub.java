package nts.uk.ctx.at.record.pub.workrecord.actualsituation.createapproval.dailyperformance.createperapprovalmonthly;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.AppDataInfoMonthly;

public interface CreateperApprovalMonthlyPub {
<<<<<<< HEAD
	public OutputCreatePerAppMonExport createperApprovalMonthly(String companyId,String executionId,List<String> employeeIDs,int processExecType,GeneralDate startDateClosure);
=======
	public boolean createperApprovalMonthly(String companyId, String executionId, List<String> employeeIDs,
			int processExecType, GeneralDate startDateClosure);

	List<AppDataInfoMonthly> getAppDataInfoMonthlyByExeID(String executionId);

	Optional<AppDataInfoMonthly> getAppDataInfoMonthlyByID(String employeeId, String executionId);

	void addAppDataInfoMonthly(AppDataInfoMonthly appDataInfoMonthly);
>>>>>>> 2d85ea9... fixbug kbt002 108717
}
