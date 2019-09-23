package nts.uk.ctx.at.function.ac.workrecord.actualsituation.createperapprovaldaily;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovaldaily.AppDataInfoDailyImport;
import nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovaldaily.CreateperApprovalDailyAdapter;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppDataInfoDaily;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.ErrorMessageRC;
import nts.uk.ctx.at.record.pub.workrecord.actualsituation.createapproval.dailyperformance.createperapprovaldaily.CreateperApprovalDailyPub;

@Stateless
public class CreateperApprovalDailyAc implements CreateperApprovalDailyAdapter {

	@Inject
	private CreateperApprovalDailyPub createperApprovalDailyPub;

	@Override
	public boolean createperApprovalDaily(String companyId, String executionId, List<String> employeeIDs,
			int processExecType, Integer createNewEmp, GeneralDate startDateClosure, GeneralDate endDateClosure) {
		boolean check = createperApprovalDailyPub.createperApprovalDaily(companyId, executionId, employeeIDs,
				processExecType, createNewEmp, startDateClosure, endDateClosure);
		return check;
	}

	@Override
	public List<AppDataInfoDailyImport> getAppDataInfoDailyByExeID(String executionId) {
		List<AppDataInfoDaily> data = createperApprovalDailyPub.getAppDataInfoDailyByExeID(executionId);
		return data.stream().map(c -> convertToDomain(c)).collect(Collectors.toList());
	}

	@Override
	public Optional<AppDataInfoDailyImport> getAppDataInfoDailyByID(String employeeId, String executionId) {
		Optional<AppDataInfoDaily> data = createperApprovalDailyPub.getAppDataInfoDailyByID(employeeId, executionId);
		if (!data.isPresent()) {
			return Optional.empty();
		}
		return Optional.of(convertToDomain(data.get()));
	}

	@Override
	public void addAppDataInfoDaily(AppDataInfoDailyImport appDataInfoDaily) {
		createperApprovalDailyPub.addAppDataInfoDaily(convertToImport(appDataInfoDaily));

	}

	private AppDataInfoDailyImport convertToDomain(AppDataInfoDaily domain) {
		return new AppDataInfoDailyImport(domain.getEmployeeId(), domain.getExecutionId(),
				domain.getErrorMessage().v());
	}

	private AppDataInfoDaily convertToImport(AppDataInfoDailyImport imp) {
		return new AppDataInfoDaily(imp.getEmployeeId(), imp.getExecutionId(),
				new ErrorMessageRC(imp.getErrorMessage()));
	}

}
