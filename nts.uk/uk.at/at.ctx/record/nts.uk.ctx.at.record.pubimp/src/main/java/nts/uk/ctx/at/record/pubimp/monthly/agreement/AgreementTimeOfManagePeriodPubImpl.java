package nts.uk.ctx.at.record.pubimp.monthly.agreement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriodRepository;
import nts.uk.ctx.at.record.dom.standardtime.export.GetAgreementTimeOfMngPeriod;
import nts.uk.ctx.at.record.pub.monthly.agreement.AgreementTimeBreakdown;
import nts.uk.ctx.at.record.pub.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.pub.monthly.agreement.AgreementTimeOfManagePeriodPub;
import nts.uk.ctx.at.record.pub.monthly.agreement.AgreementTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.LimitOneMonth;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
 * 実装：管理期間の36協定時間の取得
 * @author shuichi_ishida
 */
@Stateless
public class AgreementTimeOfManagePeriodPubImpl implements AgreementTimeOfManagePeriodPub {

	/** 管理期間の36協定時間 */
	@Inject
	private AgreementTimeOfManagePeriodRepository agreementTimeRepo;
	/** 管理期間の36協定時間を取得 */
	@Inject
	private GetAgreementTimeOfMngPeriod getAgreementTimeOfMngPeriod;
	
	@Override
	public Optional<AgreementTimeOfManagePeriod> find(String employeeId, YearMonth yearMonth) {
		
		val srcAgreementTimeOpt = this.agreementTimeRepo.find(employeeId, yearMonth);
		if (!srcAgreementTimeOpt.isPresent()) return Optional.empty();
		return Optional.of(this.toPubDomain(srcAgreementTimeOpt.get()));
	}
	
	@Override
	public Map<YearMonth, AttendanceTimeMonth> getTimeByPeriod(String employeeId, YearMonthPeriod period) {
		
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		
		List<YearMonth> ymRange = new ArrayList<>();
		YearMonth startYm = YearMonth.of(period.start().year(), period.start().month());
		YearMonth endYm = period.end();
		while (startYm.lessThanOrEqualTo(endYm)){
			ymRange.add(startYm);
			startYm = startYm.addMonths(1);
		}
		
		val srcAgreementTimeList = this.agreementTimeRepo.findBySidsAndYearMonths(employeeIds, ymRange);
		Map<YearMonth, AttendanceTimeMonth> result = new HashMap<>();
		for (val srcAgreementTime : srcAgreementTimeList){
			result.put(srcAgreementTime.getYearMonth(), srcAgreementTime.getAgreementTime().getAgreementTime());
		}
		
		return result;
	}
	
	@Override
	public List<AgreementTimeOfManagePeriod> findByYear(String employeeId, Year year) {

		val srcAgreementTimeList = this.getAgreementTimeOfMngPeriod.algorithm(employeeId, year);
		
		return srcAgreementTimeList.stream().map(c -> this.toPubDomain(c)).collect(Collectors.toList());
	}
	
	private AgreementTimeOfManagePeriod toPubDomain(
			nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod fromDomain){
		
		val fromAgreementTime = fromDomain.getAgreementTime();
		LimitOneMonth fromLimitErrorTime = null;
		if (fromAgreementTime.getExceptionLimitErrorTime().isPresent()){
			fromLimitErrorTime = new LimitOneMonth(fromAgreementTime.getExceptionLimitErrorTime().get().v());
		}
		LimitOneMonth fromLimitAlarmTime = null;
		if (fromAgreementTime.getExceptionLimitAlarmTime().isPresent()){
			fromLimitAlarmTime = new LimitOneMonth(fromAgreementTime.getExceptionLimitAlarmTime().get().v());
		}
		val fromBreakdown = fromDomain.getBreakdown();
		
		return AgreementTimeOfManagePeriod.of(
				fromDomain.getEmployeeId(),
				fromDomain.getYearMonth(),
				fromDomain.getYear(),
				AgreementTimeOfMonthly.of(
						fromAgreementTime.getAgreementTime(),
						new LimitOneMonth(fromAgreementTime.getLimitErrorTime().v()),
						new LimitOneMonth(fromAgreementTime.getLimitAlarmTime().v()),
						Optional.ofNullable(fromLimitErrorTime),
						Optional.ofNullable(fromLimitAlarmTime),
						fromAgreementTime.getStatus()),
				AgreementTimeBreakdown.of(
						fromBreakdown.getOverTime(),
						fromBreakdown.getTransferOverTime(),
						fromBreakdown.getHolidayWorkTime(),
						fromBreakdown.getTransferTime(),
						fromBreakdown.getFlexLegalTime(),
						fromBreakdown.getFlexIllegalTime(),
						fromBreakdown.getWithinPrescribedPremiumTime(),
						fromBreakdown.getWeeklyPremiumTime(),
						fromBreakdown.getMonthlyPremiumTime()));
	}

	@Override
	public Map<String, Map<YearMonth, AttendanceTimeMonth>> getTimeByPeriod(List<String> employeeIds,
			YearMonthPeriod period) {
		
		List<YearMonth> ymRange = new ArrayList<>();
		YearMonth startYm = YearMonth.of(period.start().year(), period.start().month());
		YearMonth endYm = period.end();
		while (startYm.lessThanOrEqualTo(endYm)){
			ymRange.add(startYm);
			startYm = startYm.addMonths(1);
		}
		
		val srcAgreementTimeList = this.agreementTimeRepo.findBySidsAndYearMonths(employeeIds, ymRange);
		Map<String, Map<YearMonth, AttendanceTimeMonth>> result = new HashMap<>();
		List<String> employeeIdsError = srcAgreementTimeList.stream().map( e->e.getEmployeeId()).distinct().collect(Collectors.toList());
		
		for(String employeeId: employeeIdsError) {			
			Map<YearMonth, AttendanceTimeMonth> mapAttendanceTime = new HashMap<>();
			val agreementTimeList = srcAgreementTimeList.stream().filter( e->e.getEmployeeId().equals(employeeId)).collect(Collectors.toList());
			
			for (val srcAgreementTime : agreementTimeList){
				mapAttendanceTime.put(srcAgreementTime.getYearMonth(), srcAgreementTime.getAgreementTime().getAgreementTime());
			}
			
			result.put(employeeId, mapAttendanceTime);
			
		}

		return result;
	}
}
