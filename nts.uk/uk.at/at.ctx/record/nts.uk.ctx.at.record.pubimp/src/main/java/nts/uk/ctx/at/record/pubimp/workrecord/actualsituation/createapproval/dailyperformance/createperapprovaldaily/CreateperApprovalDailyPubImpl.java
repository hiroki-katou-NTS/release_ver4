package nts.uk.ctx.at.record.pubimp.workrecord.actualsituation.createapproval.dailyperformance.createperapprovaldaily;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppDataInfoDaily;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppDataInfoDailyRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.createperapprovaldaily.CreateperApprovalDailyService;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.createperapprovaldaily.OutputCreatePerApprovalDaily;
import nts.uk.ctx.at.record.pub.workrecord.actualsituation.createapproval.dailyperformance.createperapprovaldaily.CreateperApprovalDailyPub;
import nts.uk.ctx.at.record.pub.workrecord.actualsituation.createapproval.dailyperformance.createperapprovaldaily.OutputCreatePerAppDailyExport;

@Stateless
public class CreateperApprovalDailyPubImpl implements CreateperApprovalDailyPub {

	@Inject
	private CreateperApprovalDailyService createperApprovalDailyService;
	
	@Inject
	private AppDataInfoDailyRepository appDataInfoDailyRepository; 
	@Override
	public OutputCreatePerAppDailyExport createperApprovalDaily(String companyId, String executionId, List<String> employeeIDs,
			int processExecType, Integer createNewEmp, GeneralDate startDateClosure,GeneralDate endDateClosure) {
		OutputCreatePerApprovalDaily data = createperApprovalDailyService.createperApprovalDaily(companyId, executionId, employeeIDs, processExecType, createNewEmp, startDateClosure,endDateClosure);
		return new OutputCreatePerAppDailyExport(data.isCreateperApprovalDaily(),data.isCheckStop());
	}
	@Override
	public List<AppDataInfoDaily> getAppDataInfoDailyByExeID(String executionId) {
		return appDataInfoDailyRepository.getAppDataInfoDailyByExeID(executionId);
	}
	@Override
	public Optional<AppDataInfoDaily> getAppDataInfoDailyByID(String employeeId, String executionId) {
		return appDataInfoDailyRepository.getAppDataInfoDailyByID(employeeId, executionId);
	}
	@Override
	public void addAppDataInfoDaily(AppDataInfoDaily appDataInfoDaily) {
		appDataInfoDailyRepository.addAppDataInfoDaily(appDataInfoDaily);
		
	}

}
