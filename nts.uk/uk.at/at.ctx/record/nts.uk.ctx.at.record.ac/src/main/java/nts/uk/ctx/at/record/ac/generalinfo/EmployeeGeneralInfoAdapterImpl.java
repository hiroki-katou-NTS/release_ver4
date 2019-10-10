package nts.uk.ctx.at.record.ac.generalinfo;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.generalinfo.EmployeeGeneralInfoAdapter;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.EmployeeGeneralInfoImport;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExClassificationHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExClassificationHistoryImport;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExEmploymentHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExEmploymentHistoryImport;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExJobTitleHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExJobTitleHistoryImport;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExWorkPlaceHistoryImport;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExWorkplaceHistItemImport;
import nts.uk.ctx.bs.employee.pub.generalinfo.EmployeeGeneralInfoDto;
import nts.uk.ctx.bs.employee.pub.generalinfo.EmployeeGeneralInfoPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class EmployeeGeneralInfoAdapterImpl implements EmployeeGeneralInfoAdapter {

	@Inject
	private EmployeeGeneralInfoPub employeeGeneralInfoPub;

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public EmployeeGeneralInfoImport getEmployeeGeneralInfo(List<String> employeeIds, DatePeriod period,
			boolean checkEmployment, boolean checkClassification, boolean checkJobTitle, boolean checkWorkplace,
			boolean checkDepartment) {
		EmployeeGeneralInfoDto dto = this.employeeGeneralInfoPub.getPerEmpInfo(employeeIds, period, checkEmployment,
				checkClassification, checkJobTitle, checkWorkplace, checkDepartment);

		List<ExEmploymentHistoryImport> employmentHistoryImports = dto.getEmploymentDto().stream()
				.map(employmentHistory -> new ExEmploymentHistoryImport(employmentHistory.getEmployeeId(),
						employmentHistory.getEmploymentItems().stream().map(exEmpHistItem -> {
							return new ExEmploymentHistItemImport(exEmpHistItem.getHistoryId(),
									exEmpHistItem.getPeriod(), exEmpHistItem.getEmploymentCode());
						}).collect(Collectors.toList())))
				.collect(Collectors.toList());

		List<ExClassificationHistoryImport> exClassificationHistoryImports = dto.getClassificationDto().stream()
				.map(classificationHistory -> new ExClassificationHistoryImport(classificationHistory.getEmployeeId(),
						classificationHistory.getClassificationItems().stream().map(classificationHistItem -> {
							return new ExClassificationHistItemImport(classificationHistItem.getHistoryId(),
									classificationHistItem.getPeriod(), classificationHistItem.getClassificationCode());
						}).collect(Collectors.toList())))
				.collect(Collectors.toList());

		List<ExJobTitleHistoryImport> exJobTitleHistoryImports = dto.getJobTitleDto().stream()
				.map(jobTitle -> new ExJobTitleHistoryImport(jobTitle.getEmployeeId(),
						jobTitle.getJobTitleItems().stream().map(jobTitleItem -> {
							return new ExJobTitleHistItemImport(jobTitleItem.getHistoryId(), jobTitleItem.getPeriod(),
									jobTitleItem.getJobTitleId());
						}).collect(Collectors.toList())))
				.collect(Collectors.toList());

		List<ExWorkPlaceHistoryImport> exWorkPlaceHistoryImports = dto.getWorkplaceDto().stream()
				.map(workPlaceHistory -> new ExWorkPlaceHistoryImport(workPlaceHistory.getEmployeeId(),
						workPlaceHistory.getWorkplaceItems().stream().map(workPlaceHistoryItem -> {
							return new ExWorkplaceHistItemImport(workPlaceHistoryItem.getHistoryId(),
									workPlaceHistoryItem.getPeriod(), workPlaceHistoryItem.getWorkplaceId());
						}).collect(Collectors.toList())))
				.collect(Collectors.toList());

		return new EmployeeGeneralInfoImport(employmentHistoryImports, exClassificationHistoryImports,
				exJobTitleHistoryImports, exWorkPlaceHistoryImports);
	}

}
