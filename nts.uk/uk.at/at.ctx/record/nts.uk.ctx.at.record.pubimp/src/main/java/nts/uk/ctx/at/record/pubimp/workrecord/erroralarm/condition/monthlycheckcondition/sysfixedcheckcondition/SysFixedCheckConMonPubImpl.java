package nts.uk.ctx.at.record.pubimp.workrecord.erroralarm.condition.monthlycheckcondition.sysfixedcheckcondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.sysfixedcheckcondition.checkforagreement.CheckAgreementService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.sysfixedcheckcondition.monthlyunconfirmed.MonthlyUnconfirmedService;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.enums.SelfConfirmError;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.ctx.at.record.pub.fixedcheckitem.ValueExtractAlarmWRPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.sysfixedcheckcondition.SysFixedCheckConMonPub;
import nts.uk.ctx.at.record.pub.workrecord.identificationstatus.identityconfirmprocess.IdentityConfirmProcessExport;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.service.LeaveManagementService;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
@Stateless
public class SysFixedCheckConMonPubImpl implements SysFixedCheckConMonPub {

	@Inject
	private MonthlyUnconfirmedService monthlyUnconfirmedService;
	
	@Inject
	private CheckAgreementService checkAgreementService;
	
	@Inject
	private LeaveManagementService leaveManagementService;
	
	@Inject
	private IdentityProcessUseSetRepository identityProcessRepo;
	
	@Override
	public Optional<ValueExtractAlarmWRPubExport> checkAgreement(String employeeID, int yearMonth,int closureId,ClosureDate closureDate) {
		Optional<ValueExtractAlarmWR> data = checkAgreementService.checkAgreement(employeeID, yearMonth,closureId,closureDate);
		if(data.isPresent()) {
			return Optional.of(convertToExport(data.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<ValueExtractAlarmWRPubExport> checkMonthlyUnconfirmed(String employeeID, int yearMonth) {
		Optional<ValueExtractAlarmWR> data = monthlyUnconfirmedService.checkMonthlyUnconfirmed(employeeID, yearMonth);
		if(data.isPresent()) {
			return Optional.of(convertToExport(data.get()));
		}
		return Optional.empty();
	}
	
	@Override
	public List<ValueExtractAlarmWRPubExport> checkMonthlyUnconfirmeds(String employeeID, int yearMonth,IdentityConfirmProcessExport identityExport) {
		Optional<IdentityProcessUseSet> identityProcess = Optional.empty();
		if (identityExport != null) {
			Optional<SelfConfirmError> yourSelfConfirmError = Optional.empty();
			if(identityExport.getYourSelfConfirmError().isPresent()){
				yourSelfConfirmError = Optional.of(EnumAdaptor.valueOf(identityExport.getYourSelfConfirmError().get().value, SelfConfirmError.class)); 
			}
			identityProcess = Optional.of(IdentityProcessUseSet.createFromJavaType(identityExport.getCompanyId(), identityExport.isUseConfirmByYourself(),
					identityExport.isUseIdentityOfMonth(),yourSelfConfirmError));
		}
		List<ValueExtractAlarmWR> datas = monthlyUnconfirmedService.checkMonthlyUnconfirmeds(employeeID, yearMonth,identityProcess);
		List<ValueExtractAlarmWRPubExport> lstReturn = new ArrayList<>();
		if(!CollectionUtil.isEmpty(datas)) {
			
			for (ValueExtractAlarmWR valueExtractAlarmWR : datas) {
				lstReturn.add(convertToExport(valueExtractAlarmWR));
			}
		}
		return lstReturn;
	}
	
	@Override
	public List<ValueExtractAlarmWRPubExport> checkMonthlyUnconfirmeds(List<String> employeeID, List<YearMonth> yearMonth) {
		Optional<IdentityProcessUseSet> identityProcess = identityProcessRepo.findByKey(AppContexts.user().companyId());
		
		List<ValueExtractAlarmWR> datas = monthlyUnconfirmedService.checkMonthlyUnconfirmeds(employeeID, yearMonth,identityProcess);
		List<ValueExtractAlarmWRPubExport> lstReturn = new ArrayList<>();
		if(!CollectionUtil.isEmpty(datas)) {
			
			for (ValueExtractAlarmWR valueExtractAlarmWR : datas) {
				lstReturn.add(convertToExport(valueExtractAlarmWR));
			}
		}
		return lstReturn;
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
	public Optional<ValueExtractAlarmWRPubExport> checkDeadlineCompensatoryLeaveCom(String employeeID, Closure closing,
			CompensatoryLeaveComSetting compensatoryLeaveComSetting) {
		Boolean data = leaveManagementService.checkDeadlineCompensatoryLeaveCom(employeeID, closing, compensatoryLeaveComSetting);
		if(data) {
			int deadlCheckMonth = compensatoryLeaveComSetting.getCompensatoryAcquisitionUse().getDeadlCheckMonth().value + 1;
			YearMonth currentYearMonth = closing.getClosureMonth().getProcessingYm();
			
			return Optional.of(new ValueExtractAlarmWRPubExport(
					null,
					employeeID,
					GeneralDate.ymd(currentYearMonth.year(), currentYearMonth.month(), 1),
					TextResource.localize("KAL010_100"),
					TextResource.localize("KAL010_278"),
					TextResource.localize("KAL010_279",String.valueOf(deadlCheckMonth)),	
					null,null));
		}
		return Optional.empty();
	}

}
