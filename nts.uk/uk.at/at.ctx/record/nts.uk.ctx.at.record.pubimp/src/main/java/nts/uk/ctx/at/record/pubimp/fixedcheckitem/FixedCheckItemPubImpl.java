package nts.uk.ctx.at.record.pubimp.fixedcheckitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.service.workrecord.erroralarm.recordcheck.ErAlWorkRecordCheckService;
import nts.uk.ctx.at.record.app.service.workrecord.erroralarm.recordcheck.result.ContinuousHolidayCheckResult;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkadminunverified.CheckAdminUnverifiedService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkingData.CheckingDataService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.CheckPrincipalUnconfirmService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.worktimenotregister.WorkTimeNotRegisterService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.worktypenotregister.WorkTypeNotRegisterService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.otkcustomize.ContinuousHolCheckSet;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.otkcustomize.repo.ContinuousHolCheckSetRepo;
import nts.uk.ctx.at.record.pub.fixedcheckitem.FixedCheckItemPub;
import nts.uk.ctx.at.record.pub.fixedcheckitem.ValueExtractAlarmWRPubExport;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class FixedCheckItemPubImpl implements FixedCheckItemPub {

	@Inject
	private WorkTypeNotRegisterService workTypeNotRegisterService;
	
	@Inject
	private WorkTimeNotRegisterService workTimeNotRegisterService;
	
	@Inject
	private CheckPrincipalUnconfirmService checkPrincipalUnconfirmService;
	
	@Inject
	private CheckAdminUnverifiedService checkAdminUnverifiedService;
	
	@Inject
	private CheckingDataService checkingDataService;
	
	@Inject
	private ErAlWorkRecordCheckService erAlWorkRecordCheckService; 
	
	@Inject
	private ContinuousHolCheckSetRepo continuousHolCheckSetRepo;
	
	
	@Override
	public Optional<ValueExtractAlarmWRPubExport> checkWorkTypeNotRegister(String workplaceID,String employeeID, GeneralDate date, String workTypeCD) {
		Optional<ValueExtractAlarmWR> data = workTypeNotRegisterService.checkWorkTypeNotRegister(workplaceID,employeeID, date, workTypeCD);
		if(data.isPresent()) {
			return Optional.of(convertToExport(data.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<ValueExtractAlarmWRPubExport> checkWorkTimeNotRegister(String workplaceID,String employeeID, GeneralDate date, String workTimeCD) {
		Optional<ValueExtractAlarmWR> data = workTimeNotRegisterService.checkWorkTimeNotRegister(workplaceID,employeeID, date, workTimeCD);

		if(data.isPresent()) {
			return Optional.of(convertToExport(data.get()));
		}
		return Optional.empty();
	}

	@Override
	public List<ValueExtractAlarmWRPubExport> checkPrincipalUnconfirm(String workplaceID, String employeeID,
			GeneralDate startDate, GeneralDate endDate) {
		return checkPrincipalUnconfirmService.checkPrincipalUnconfirm(workplaceID, employeeID, startDate, endDate)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
	}
	
	@Override
	public Map<String, List<GeneralDate>> checkPrincipalConfirmed(List<String> employeeID,
			GeneralDate startDate, GeneralDate endDate) {
		return checkPrincipalUnconfirmService.checkPrincipalConfirmed(employeeID, startDate, endDate);
	}

	@Override
	public List<ValueExtractAlarmWRPubExport> checkAdminUnverified(String workplaceID, String employeeID,
			GeneralDate startDate, GeneralDate endDate) {
		return checkAdminUnverifiedService.checkAdminUnverified(workplaceID, employeeID, startDate, endDate)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
	}

	@Override
	public List<ValueExtractAlarmWRPubExport> checkingData(String workplaceID,String employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		return checkingDataService.checkingData(workplaceID,employeeID, startDate, endDate)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
	}
	
	private ValueExtractAlarmWRPubExport convertToExport(ValueExtractAlarmWR valueExtractAlarmWR) {
		return new ValueExtractAlarmWRPubExport(
				valueExtractAlarmWR.getWorkplaceID().orElse(null),
				valueExtractAlarmWR.getEmployeeID(),
				valueExtractAlarmWR.getAlarmValueDate(),
				valueExtractAlarmWR.getClassification(),
				valueExtractAlarmWR.getAlarmItem(),
				valueExtractAlarmWR.getAlarmValueMessage(),
				valueExtractAlarmWR.getComment().orElse(null),
				valueExtractAlarmWR.getCheckedValue().orElse(null)
				);
	}

	@Override
	public List<ValueExtractAlarmWRPubExport> checkAdminUnverified(String workplaceID, String employeeID,
			DatePeriod datePeriod) {
		return checkAdminUnverifiedService.checkAdminUnverified(workplaceID, employeeID,datePeriod)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
	}
	
	@Override
	public Map<String, List<GeneralDate>> checkAdminUnverified(List<String> employeeID,
			DatePeriod datePeriod) {
		return checkAdminUnverifiedService.checkAdminUnverified(employeeID, datePeriod);
	}

	@Override
	public List<ValueExtractAlarmWRPubExport> checkContinuousVacation(String employeeID, DatePeriod datePeriod) {
		List<ValueExtractAlarmWRPubExport> listResult = new ArrayList<>();
		ContinuousHolidayCheckResult data = erAlWorkRecordCheckService.checkContinuousHolidays(employeeID, datePeriod);
		if(data.getCheckResult().isEmpty())
			return Collections.emptyList();
		data.getCheckResult().forEach((k,v)->{
			ValueExtractAlarmWRPubExport value = new ValueExtractAlarmWRPubExport(
					null,
					employeeID,
					k,
					TextResource.localize("KAL010_1"),
					TextResource.localize("KAL010_301"),
					TextResource.localize("KAL010_302"),
					"",
					null,
					v);
			listResult.add(value);		
		});
		
		return listResult;
	}

	@Override
	public int getContinuousHolCheckSet(String companyId) {
		Optional<ContinuousHolCheckSet> data = continuousHolCheckSetRepo.findSpecial(companyId);
		if(data.isPresent())
			return data.get().getMaxContinuousDays().v();
		return 0;
	}
	
	
	
	

}
