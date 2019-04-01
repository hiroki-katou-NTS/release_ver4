package nts.uk.ctx.at.function.ac.workrecord.erroralarm.condition.monthlycheckcondition.sysfixedcheckcondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.sysfixedcheckcondition.SysFixedCheckConMonAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.approvalmanagement.ApprovalProcessImport;
import nts.uk.ctx.at.function.dom.adapter.workrecord.identificationstatus.identityconfirmprocess.IdentityConfirmProcessImport;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.ErAlConstant;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.enums.SelfConfirmError;
import nts.uk.ctx.at.record.pub.fixedcheckitem.ValueExtractAlarmWRPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.sysfixedcheckcondition.SysFixedCheckConMonPub;
import nts.uk.ctx.at.record.pub.workrecord.identificationstatus.identityconfirmprocess.IdentityConfirmProcessExport;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.workflow.pub.resultrecord.EmpPerformMonthParam;
import nts.uk.ctx.workflow.pub.resultrecord.IntermediateDataPub;
import nts.uk.ctx.workflow.pub.resultrecord.export.AppRootSttMonthExport;
import nts.uk.shr.com.context.AppContexts;
//import nts.uk.ctx.workflow.pub.spr.export.AppRootStateStatusSprExport;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
@Stateless
public class SysFixedCheckConMonAcFinder implements SysFixedCheckConMonAdapter {
	
	@Inject
	private SysFixedCheckConMonPub sysFixedCheckConMonPub;
	
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo;
	
	@Inject
	private IntermediateDataPub intermediateDataPub;
	
	@Inject
	private ApprovalProcessingUseSettingRepository approvalProcessRepo;
	
	@Override
	public Optional<ValueExtractAlarm> checkAgreement(String employeeID, int yearMonth,int closureId,ClosureDate closureDate) {
		Optional<ValueExtractAlarmWRPubExport> data = sysFixedCheckConMonPub.checkAgreement(employeeID, yearMonth,closureId,closureDate);
		if(data.isPresent()) {
			return Optional.of(convertToExport(data.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<ValueExtractAlarm> checkMonthlyUnconfirmed(String employeeID, int yearMonth) {
		Optional<ValueExtractAlarmWRPubExport> data = sysFixedCheckConMonPub.checkMonthlyUnconfirmed(employeeID, yearMonth);
		if(data.isPresent()) {
			return Optional.of(convertToExport(data.get()));
		}
		return Optional.empty();
	}
	
	@Override
	public List<ValueExtractAlarm> checkMonthlyUnconfirmeds(String employeeID, int yearMonth,IdentityConfirmProcessImport identityConfirmProcessImport) {
		IdentityConfirmProcessExport export= convertIdentityToExport(identityConfirmProcessImport);
		List<ValueExtractAlarmWRPubExport> datas = sysFixedCheckConMonPub.checkMonthlyUnconfirmeds(employeeID, yearMonth,export);
		List<ValueExtractAlarm> lstReturn = new ArrayList<>();
		if(!CollectionUtil.isEmpty(datas)) {
			for (ValueExtractAlarmWRPubExport obj : datas) {
				lstReturn.add(convertToExport(obj));
			}
		}
		return lstReturn;
	}
	
	@Override
	public List<ValueExtractAlarm> checkMonthlyUnconfirmeds(List<String> employeeID, List<YearMonth> yearMonth) {
		List<ValueExtractAlarmWRPubExport> datas = sysFixedCheckConMonPub.checkMonthlyUnconfirmeds(employeeID, yearMonth);
		List<ValueExtractAlarm> lstReturn = new ArrayList<>();
		if(!CollectionUtil.isEmpty(datas)) {
			for (ValueExtractAlarmWRPubExport obj : datas) {
				lstReturn.add(convertToExport(obj));
			}
		}
		return lstReturn;
	}

	private ValueExtractAlarm convertToExport(ValueExtractAlarmWRPubExport export) {
		return new ValueExtractAlarm(
				export.getWorkplaceID().orElse(null),
				export.getEmployeeID(),
				export.getAlarmValueDate().toString(ErAlConstant.YM_FORMAT),
				export.getClassification(),
				export.getAlarmItem(),
				export.getAlarmValueMessage(),
				export.getComment().orElse(null)
				);
	}
	
	private IdentityConfirmProcessExport convertIdentityToExport(IdentityConfirmProcessImport imp) {
		Optional<SelfConfirmError> yourSelfConfirmError = Optional.empty();
		if(imp.getYourSelfConfirmError().isPresent()){
			yourSelfConfirmError = Optional.of(EnumAdaptor.valueOf(imp.getYourSelfConfirmError().get().value, SelfConfirmError.class));
		}
		return new IdentityConfirmProcessExport(
				imp.getCompanyId(),
				imp.isUseConfirmByYourself(),
				imp.isUseIdentityOfMonth(),
				yourSelfConfirmError
				);
	}
	@Override
	public Optional<ValueExtractAlarm> checkDeadlineCompensatoryLeaveCom(String employeeID, Closure closing,
			CompensatoryLeaveComSetting compensatoryLeaveComSetting) {
		
		Optional<ValueExtractAlarmWRPubExport> data = sysFixedCheckConMonPub.checkDeadlineCompensatoryLeaveCom(employeeID, closing, compensatoryLeaveComSetting);
		if(data.isPresent()) {
			return Optional.of(convertToExport(data.get()));
		}
		return Optional.empty();
	}

	@Override
	public List<ValueExtractAlarm> checkMonthlyUnconfirmedsAdmin(String employeeId, YearMonth yearMonth,
			ApprovalProcessImport approvalProcessImport) {
		List<ValueExtractAlarm> valueExtractAlarms = new ArrayList<ValueExtractAlarm>();
		GeneralDate date = GeneralDate.ymd(yearMonth.year(), yearMonth.month(), 1);
		Integer approved = 2;// 2:承認済 ; 1:承認中 ; 0:未承認
		//INPUT.承認処理の利用設定.月の承認者確認を利用する＝true
		if (approvalProcessImport.getUseMonthApproverConfirm() != true) {
			return Collections.emptyList();
		}
		List<AttendanceTimeOfMonthly> attendanceTimeOfMonthlys = new ArrayList<>();
		attendanceTimeOfMonthlys = attendanceTimeOfMonthlyRepo.findByYearMonthOrderByStartYmd(employeeId,yearMonth);
		for (AttendanceTimeOfMonthly attendanceTimeOfMonthly : attendanceTimeOfMonthlys) {
			//ドメインモデル「月別実績の勤怠時間」を取得する
			List<EmpPerformMonthParam> empPerformMonthParams = new ArrayList<EmpPerformMonthParam>();
			empPerformMonthParams.add(new EmpPerformMonthParam(yearMonth, 
					attendanceTimeOfMonthly.getClosureId().value,
					attendanceTimeOfMonthly.getClosureDate(),
					attendanceTimeOfMonthly.getDatePeriod().end(),
					employeeId));
			//No.533
			List<AppRootSttMonthExport> appRootStateStatusSprExports = intermediateDataPub
					.getAppRootStatusByEmpsMonth(empPerformMonthParams).getAppRootSttMonthExportLst();
			String classification=TextResource.localize("KAL010_100");
			String alarmItem=TextResource.localize("KAL010_128");
			String alarmValueMessage=TextResource.localize("KAL010_129");
			if (CollectionUtil.isEmpty(appRootStateStatusSprExports)) {
				// Create Alarm message
				valueExtractAlarms.add(new ValueExtractAlarm(null, employeeId.toString(), date.toString(ErAlConstant.DATE_FORMAT),
						classification, alarmItem, alarmValueMessage, null));
			} else {
				
				// 承認ルート状況.承認状況!＝承認済 
				if (appRootStateStatusSprExports.get(0).getDailyConfirmAtr() != approved) {
					valueExtractAlarms.add(new ValueExtractAlarm(null, employeeId.toString(), date.toString(ErAlConstant.DATE_FORMAT),
							classification, alarmItem, alarmValueMessage, null));
				}

			}
			
		}
		return valueExtractAlarms;
	}
	
	@Override
	public List<ValueExtractAlarm> checkMonthlyUnconfirmedsAdmin(List<String> employeeID, List<YearMonth> yearMonth) {
		List<ValueExtractAlarm> valueExtractAlarms = new ArrayList<ValueExtractAlarm>();

		//INPUT.承認処理の利用設定.月の承認者確認を利用する＝true
		approvalProcessRepo.findByCompanyId(AppContexts.user().companyId()).ifPresent(ap -> {
			if(ap.getUseMonthApproverConfirm() != null && ap.getUseMonthApproverConfirm() == true){
				List<AttendanceTimeOfMonthly> attendanceTimeOfMonthlys = attendanceTimeOfMonthlyRepo.findBySidsAndYearMonths(employeeID, yearMonth);
				
				String classification=TextResource.localize("KAL010_100");
				String alarmItem=TextResource.localize("KAL010_128");
				String alarmValueMessage=TextResource.localize("KAL010_129");
				//No.533
				/** TODO: need response */
				List<AppRootSttMonthExport> appRootStates = intermediateDataPub.getAppRootStatusByEmpsMonth(attendanceTimeOfMonthlys.stream().map(atm -> {
					return new EmpPerformMonthParam(atm.getYearMonth(), 
							atm.getClosureId().value,
							atm.getClosureDate(),
							atm.getDatePeriod().end(),
							atm.getEmployeeId());
				}).collect(Collectors.toList())).getAppRootSttMonthExportLst();
				
				attendanceTimeOfMonthlys.stream().forEach(c -> {
					Optional<AppRootSttMonthExport> appSttEx = appRootStates.stream().filter(ars -> {
						return ars.getClosureID() == c.getClosureId().value && ars.getEmployeeID().equals(c.getEmployeeId())
								&& ars.getYearMonth().equals(c.getYearMonth()) && ars.getClosureDate().equals(c.getClosureDate());
					}).findFirst();
					if(!appSttEx.isPresent() || (appSttEx.isPresent() && appSttEx.get().getDailyConfirmAtr() != 2)){
						GeneralDate current = GeneralDate.ymd(c.getYearMonth().year(), c.getYearMonth().month(), 1);
						// 2:承認済 ; 1:承認中 ; 0:未承認
						valueExtractAlarms.add(new ValueExtractAlarm(null, c.getEmployeeId(), current.toString(ErAlConstant.YM_FORMAT),
								classification, alarmItem, alarmValueMessage, null));
					} 
				});
			}
		});
		return valueExtractAlarms;
	}
}
