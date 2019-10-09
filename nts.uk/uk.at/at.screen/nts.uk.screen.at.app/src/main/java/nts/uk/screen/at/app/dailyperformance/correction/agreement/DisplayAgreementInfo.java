package nts.uk.screen.at.app.dailyperformance.correction.agreement;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.GetExcessTimesYearAdapter;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriodRepository;
import nts.uk.ctx.at.record.dom.standardtime.AgreementOperationSetting;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFun;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.screen.at.app.dailyperformance.correction.finddata.IFindData;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DisplayAgreementInfo {
	
	private static String ERROR = "state-error text-error";
	
	private static String ALARM = "state-alarm text-alarm";
	
	private static String EXCEPTION = "state-exception";
	
	private static final String FORMAT_HH_MM = "%d:%02d";
	

	@Inject
	private IFindData findata;
	
	@Inject
	private AgreementTimeOfManagePeriodRepository agreementTimeOfManagePeriodRepository;
	
	@Inject
	private AgreementOperationSettingRepository agreementOperationSettingRepository;
	
	@Inject
	private GetExcessTimesYearAdapter getExcessTimesYearAdapter;
	
	public AgreementInfomationDto displayAgreementInfo(String companyId, String employeeId, int year, int month){
		
		Optional<DaiPerformanceFun> funOpt = findata.getDailyPerformFun(companyId);
		if(!funOpt.isPresent() || funOpt.get().getDisp36Atr() == 0) return AgreementInfomationDto.builder().showAgreement(false).build();
		
		
		AgreementInfomationDto result = new AgreementInfomationDto();
		
		result.setShowAgreement(true);
		
		Optional<AgreementTimeOfManagePeriod> agreeTimeOpt = agreementTimeOfManagePeriodRepository.find(employeeId, YearMonth.of(year, month));
		
		if (agreeTimeOpt.isPresent()) {
			result.setCssAgree(convertStateCell(agreeTimeOpt.get().getAgreementTime().getStatus().value));
			result.setAgreementTime36(agreeTimeOpt.get().getAgreementTime() != null
					&& agreeTimeOpt.get().getAgreementTime().getAgreementTime() != null
							? convertTime(agreeTimeOpt.get().getAgreementTime().getAgreementTime().v())
							: "0:00");
			result.setMaxTime(agreeTimeOpt.get().getAgreementTime() != null
					&& agreeTimeOpt.get().getAgreementTime().getLimitErrorTime() != null
							? convertTime(agreeTimeOpt.get().getAgreementTime().getLimitErrorTime().v())
							: "0:00");
		}

		Optional<AgreementOperationSetting> agreeOperationOpt = agreementOperationSettingRepository.find(companyId);

		result.setMaxNumber(agreeOperationOpt.isPresent()
				? String.valueOf(agreeOperationOpt.get().getNumberTimesOverLimitType().value)
				: "0");
		result.setExcessFrequency(String.valueOf(getExcessTimesYearAdapter.algorithm(employeeId, new Year(year))));
		if (Integer.parseInt(result.getExcessFrequency()) >= Integer.parseInt(result.getMaxNumber())) {
			result.setCssFrequency(ERROR);
		}

		return result;
	}
	
	private String convertStateCell(int value) {
		if (value == AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR_SP.value
				|| value == AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ALARM_SP.value) {
			return EXCEPTION;
		} else if (value == AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR.value
				|| value == AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ERROR.value) {
			// text error
			return ERROR;
		} else if (value == AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ALARM.value
				|| value == AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ALARM.value) {
			// text alarm
			return ALARM;
		} else {
			return "";
		}
	}
	
	private String convertTime(Integer minute){
		if(minute == null) minute = 0;
		int hours = minute / 60;
		int minutes = Math.abs(minute) % 60;
		return (minute < 0 && hours == 0) ?  "-"+String.format(FORMAT_HH_MM, hours, minutes) : String.format(FORMAT_HH_MM, hours, minutes);
	}
}
