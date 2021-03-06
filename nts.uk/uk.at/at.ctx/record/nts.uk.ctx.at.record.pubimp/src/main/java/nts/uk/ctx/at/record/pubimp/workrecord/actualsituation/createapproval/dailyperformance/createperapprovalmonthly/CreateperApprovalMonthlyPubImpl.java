package nts.uk.ctx.at.record.pubimp.workrecord.actualsituation.createapproval.dailyperformance.createperapprovalmonthly;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.AppDataInfoMonthly;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.AppDataInfoMonthlyRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.createperapprovalmonthly.CreateperApprovalMonthlyService;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.createperapprovalmonthly.OutputCreatePerApprovalMon;
import nts.uk.ctx.at.record.pub.workrecord.actualsituation.createapproval.dailyperformance.createperapprovalmonthly.CreateperApprovalMonthlyPub;
import nts.uk.ctx.at.record.pub.workrecord.actualsituation.createapproval.dailyperformance.createperapprovalmonthly.OutputCreatePerAppMonExport;

@Stateless
public class CreateperApprovalMonthlyPubImpl implements CreateperApprovalMonthlyPub {

	@Inject
	private CreateperApprovalMonthlyService createperApprovalMonthlyService;
	@Inject
	private AppDataInfoMonthlyRepository appDataInfoMonthlyRepository;
	@Override
	public OutputCreatePerAppMonExport createperApprovalMonthly(String companyId, String executionId, List<String> employeeIDs,
			int processExecType, GeneralDate startDateClosure) {
		OutputCreatePerApprovalMon data = createperApprovalMonthlyService.createperApprovalMonthly(companyId, executionId, employeeIDs, processExecType, startDateClosure);
		return new OutputCreatePerAppMonExport(data.isCreateperApprovalMon(),data.isCheckStop());
	}
	@Override
	public List<AppDataInfoMonthly> getAppDataInfoMonthlyByExeID(String executionId) {
		return appDataInfoMonthlyRepository.getAppDataInfoMonthlyByExeID(executionId);
	}
	@Override
	public Optional<AppDataInfoMonthly> getAppDataInfoMonthlyByID(String employeeId, String executionId) {
		// TODO Auto-generated method stub
		return appDataInfoMonthlyRepository.getAppDataInfoMonthlyByID(employeeId, executionId);
	}
	@Override
	public void addAppDataInfoMonthly(AppDataInfoMonthly appDataInfoMonthly) {
		appDataInfoMonthlyRepository.addAppDataInfoMonthly(appDataInfoMonthly);
		
	}

}
