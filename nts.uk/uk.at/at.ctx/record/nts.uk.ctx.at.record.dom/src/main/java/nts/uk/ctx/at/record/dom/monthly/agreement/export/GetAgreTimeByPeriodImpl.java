package nts.uk.ctx.at.record.dom.monthly.agreement.export;

import java.util.*;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriodRepository;
import nts.uk.ctx.at.record.dom.standardtime.AgreementMonthSetting;
import nts.uk.ctx.at.record.dom.standardtime.AgreementYearSetting;
import nts.uk.ctx.at.record.dom.standardtime.BasicAgreementSetting;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitOneYear;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementDomainService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementMonthSettingRepository;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementYearSettingRepository;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemCustom;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
 * 実装：指定期間36協定時間の取得
 * @author shuichu_ishida
 */
@Stateless
public class GetAgreTimeByPeriodImpl implements GetAgreTimeByPeriod {

	/** 管理期間の36協定時間 */
	@Inject
	private AgreementTimeOfManagePeriodRepository agreementTimeOfMngPrdRepo;
	/** 労働条件項目の取得 */
	@Inject
	public WorkingConditionItemRepository workingConditionItem;
	/** ドメインサービス：36協定 */
	@Inject
	public AgreementDomainService agreementDomainService;
	/** 36協定年度設定 */
	@Inject
	private AgreementYearSettingRepository agreementYearSetRepo;
	/** 36協定年月設定 */
	@Inject
	private AgreementMonthSettingRepository agreementMonthSetRepo;
    @Inject
    private ManagedParallelWithContext parallel;
	
	/** 指定期間36協定時間の取得 */
	@Override
	public List<AgreementTimeByPeriod> algorithm(String companyId, String employeeId, GeneralDate criteria,
			Month startMonth, Year year, PeriodAtrOfAgreement periodAtr) {
		
		return internalAlgorithm(companyId, employeeId, criteria, startMonth, year, periodAtr, null);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AgreementTimeByPeriod> algorithm(String companyId, String employeeId, GeneralDate criteria,
			Month startMonth, Year year, PeriodAtrOfAgreement periodAtr, AgeementTimeCommonSetting settingGetter) {
		
		return internalAlgorithm(companyId, employeeId, criteria, startMonth, year, periodAtr, settingGetter);
	}
	
	private List<AgreementTimeByPeriod> internalAlgorithm(String companyId, String employeeId, GeneralDate criteria,
			Month startMonth, Year year, PeriodAtrOfAgreement periodAtr, 
			AgeementTimeCommonSetting settingGetter) {
		
		List<AgreementTimeByPeriod> results = new ArrayList<>();

		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		
		// ループする期間を判断
		int stepMon = 2;
		if (periodAtr == PeriodAtrOfAgreement.THREE_MONTHS) stepMon = 3;
		if (periodAtr == PeriodAtrOfAgreement.ONE_MONTH) stepMon = 1;
		if (periodAtr == PeriodAtrOfAgreement.ONE_YEAR) stepMon = 12;
		YearMonth idxYm = YearMonth.of(year.v(), startMonth.v());
		for (int i = 0; i < 12; i += stepMon){
			List<YearMonth> periodYmList = new ArrayList<>();
			for (int ii = 0; ii < stepMon; ii++){
				periodYmList.add(idxYm.addMonths(i + ii));
			}
			
			// 36協定時間を取得
			val agreementTimeList =
					this.agreementTimeOfMngPrdRepo.findBySidsAndYearMonths(employeeIds, periodYmList);
			if (agreementTimeList.size() == 0) continue;
			
			// 期間をセット
			AgreementTimeByPeriod result = new AgreementTimeByPeriod(
					periodYmList.get(0), periodYmList.get(periodYmList.size() - 1));
			
			// 36協定時間を合計
			for (val agreeemntTime : agreementTimeList){
				result.addMinutesToAgreementTime(agreeemntTime.getAgreementTime().getAgreementTime().v());
			}
			
			Optional<Year> checkYearOpt = Optional.empty();
			Optional<YearMonth> checkYmOpt = Optional.empty();
			if (periodAtr == PeriodAtrOfAgreement.ONE_YEAR) checkYearOpt = Optional.of(year);
			if (periodAtr == PeriodAtrOfAgreement.ONE_MONTH) checkYmOpt = Optional.of(periodYmList.get(0));

			// 状態チェック
			{
				// 「労働条件項目」を取得
				val workingConditionItemOpt = getWorkCondition(employeeId, criteria, settingGetter);
				if (!workingConditionItemOpt.isPresent()){
					return results;
				}

				// 労働制を確認する
				val workingSystem = workingConditionItemOpt.get().getLaborSystem();
				
				// 36協定基本設定を取得する
				val basicAgreementSet = getBasicSetting(companyId, employeeId, criteria, settingGetter, workingSystem);
				
				// 「年度」を確認
				Optional<AgreementYearSetting> yearSetOpt = Optional.empty();
				if (checkYearOpt.isPresent()){
					
					// 36協定年度設定を取得する
					yearSetOpt = this.agreementYearSetRepo.findByKey(employeeId, checkYearOpt.get().v());
				}

				// 「年月」を確認
				Optional<AgreementMonthSetting> monthSetOpt = Optional.empty();
				if (checkYmOpt.isPresent()){
					
					// 36協定年月設定を取得する
					monthSetOpt = this.agreementMonthSetRepo.findByKey(employeeId, checkYmOpt.get());
				}
				
				// 取得した限度時間をセット
				switch (periodAtr){
				case TWO_MONTHS:
					result.setLimitAlarmTime(new LimitOneYear(basicAgreementSet.getAlarmTwoMonths().v()));
					result.setLimitErrorTime(new LimitOneYear(basicAgreementSet.getErrorTwoMonths().v()));
					break;
				case THREE_MONTHS:
					result.setLimitAlarmTime(new LimitOneYear(basicAgreementSet.getAlarmThreeMonths().v()));
					result.setLimitErrorTime(new LimitOneYear(basicAgreementSet.getErrorThreeMonths().v()));
					break;
				case ONE_YEAR:
					result.setLimitAlarmTime(new LimitOneYear(basicAgreementSet.getAlarmOneYear().v()));
					result.setLimitErrorTime(new LimitOneYear(basicAgreementSet.getErrorOneYear().v()));
					if (yearSetOpt.isPresent()){
						val yearSet = yearSetOpt.get();
						result.setExceptionLimitAlarmTime(Optional.of(
								new LimitOneYear(yearSet.getAlarmOneYear().v())));
						result.setExceptionLimitErrorTime(Optional.of(
								new LimitOneYear(yearSet.getErrorOneYear().v())));
					}
					break;
				default:	// ONE_MONTH
					result.setLimitAlarmTime(new LimitOneYear(basicAgreementSet.getAlarmOneMonth().v()));
					result.setLimitErrorTime(new LimitOneYear(basicAgreementSet.getErrorOneMonth().v()));
					if (monthSetOpt.isPresent()){
						val monthSet = monthSetOpt.get();
						result.setExceptionLimitAlarmTime(Optional.of(
								new LimitOneYear(monthSet.getAlarmOneMonth().v())));
						result.setExceptionLimitErrorTime(Optional.of(
								new LimitOneYear(monthSet.getErrorOneMonth().v())));
					}
					break;
				}
				
				// チェック処理
				result.errorCheck();
			}
			
			results.add(result);
		}
		
		// 年間36協定時間を返す
		return results;
	}

	private Optional<WorkingConditionItem> getWorkCondition(String employeeId, GeneralDate criteria,
			AgeementTimeCommonSetting settingGetter) {
		if(settingGetter == null){
			return this.workingConditionItem.getBySidAndStandardDate(employeeId, criteria);
		}
		return settingGetter.getWorkCondition(employeeId, criteria);
	}

	private BasicAgreementSetting getBasicSetting(String companyId,
			String employeeId, GeneralDate criteria, AgeementTimeCommonSetting settingGetter,
			WorkingSystem workingSystem) {
		if(settingGetter == null){
			return agreementDomainService.getBasicSet(companyId, employeeId, criteria, workingSystem);
		}
		return settingGetter.getBasicSet(companyId, employeeId, criteria, workingSystem);
	} 
	
	@Override
	public List<AgreementTimeByEmp> algorithmImprove(String companyId, List<String> employeeIds, GeneralDate criteria,
                                                     Month startMonth, Year year, List<PeriodAtrOfAgreement> periodAtrs,  Map<String, YearMonthPeriod> periodWorking) {
        YearMonth startYm = YearMonth.of(year.v(), startMonth.v());
        List<YearMonth> periodYmAll = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            periodYmAll.add(startYm.addMonths(i));
        }
        // 36協定時間を取得
        
        List<AgreementTimeOfManagePeriod> listAgreementTimeOfManagePeriod = this.agreementTimeOfMngPrdRepo.findBySidsAndYearMonths(employeeIds, periodYmAll).stream().filter(c-> {
        	return periodWorking.get(c.getEmployeeId()).contains(c.getYearMonth());
        }).collect(Collectors.toList());
        
        Map<String, List<AgreementTimeOfManagePeriod>> agreementTimeAll = listAgreementTimeOfManagePeriod.stream().collect(Collectors.groupingBy(AgreementTimeOfManagePeriod::getEmployeeId));

        // 「労働条件項目」を取得
        Map<String, WorkingConditionItemCustom> workingConditionItemAll =
                this.workingConditionItem.getBySidsAndStandardDate(employeeIds, criteria).stream()
                        .collect(Collectors.toMap(WorkingConditionItemCustom::getEmployeeId, x -> x));

        Map<String, AgreementYearSetting> yearSetAll = new HashMap<>();
        if (periodAtrs.contains(PeriodAtrOfAgreement.ONE_YEAR)) {
            // 36協定年度設定を取得する
            yearSetAll = this.agreementYearSetRepo.findByKey(employeeIds, year.v()).stream()
                    .collect(Collectors.toMap(AgreementYearSetting::getEmployeeId, x -> x));
        }

        Map<String, List<AgreementMonthSetting>> monthSetAll = new HashMap<>();
        if (periodAtrs.contains(PeriodAtrOfAgreement.ONE_MONTH)) {
            // 36協定年月設定を取得する
        	
        	List<AgreementMonthSetting> agreementMonthSettings = this.agreementMonthSetRepo.findByKey(employeeIds, periodYmAll).stream().filter(c-> {
            	return periodWorking.get(c.getEmployeeId()).contains(c.getYearMonthValue());
            }).collect(Collectors.toList());
        	
            monthSetAll = agreementMonthSettings.stream().collect(Collectors.groupingBy(AgreementMonthSetting::getEmployeeId));
        }

        Map<String, AgreementYearSetting> finalYearSetAll = yearSetAll;
        Map<String, List<AgreementMonthSetting>> finalMonthSetAll = monthSetAll;
        List<AgreementTimeByEmp> agreementTimes = Collections.synchronizedList(new ArrayList<>());
        this.parallel.forEach(employeeIds, employeeId -> {
            if (!agreementTimeAll.containsKey(employeeId)) return;
            List<AgreementTimeOfManagePeriod> agreementTimeByEmp = agreementTimeAll.get(employeeId);

            // 「労働条件項目」を取得
            if (!workingConditionItemAll.containsKey(employeeId)) return;
            WorkingConditionItemCustom workingConditionItemByEmp = workingConditionItemAll.get(employeeId);

            AgreementYearSetting yearSetByEmp = null;
            if (finalYearSetAll.containsKey(employeeId)) {
                // 36協定年度設定を取得する
                yearSetByEmp = finalYearSetAll.get(employeeId);
            }

            Map<Integer, AgreementMonthSetting> monthSetByEmp = new HashMap<>();
            if (finalMonthSetAll.containsKey(employeeId)) {
                // 36協定年月設定を取得する
                monthSetByEmp = finalMonthSetAll.get(employeeId).stream()
                        .collect(Collectors.toMap(x -> x.getYearMonthValue().v(), x -> x));
            }

            List<AgreementTimeByEmp> results = this.getAgreementTimeByEmp(companyId, employeeId, criteria, periodAtrs,
                    startYm, agreementTimeByEmp, workingConditionItemByEmp, yearSetByEmp, monthSetByEmp);
            agreementTimes.addAll(results);
        });
        // 年間36協定時間を返す
        return new ArrayList<>(agreementTimes);
	}

	private List<AgreementTimeByEmp> getAgreementTimeByEmp(String companyId, String employeeId, GeneralDate criteria,
															  List<PeriodAtrOfAgreement> periodAtrs, YearMonth startYm,
															  List<AgreementTimeOfManagePeriod> agreementTimeByEmp,
															  WorkingConditionItemCustom workingConditionItemByEmp,
															  AgreementYearSetting yearSetByEmp,
															  Map<Integer, AgreementMonthSetting> monthSetByEmp){
		List<AgreementTimeByEmp> agreementTimes = new ArrayList<>();
		
		// 労働制を確認する
		val workingSystem = workingConditionItemByEmp.getLaborSystem();

		// 36協定基本設定を取得する
		val basicAgreementSet = this.agreementDomainService.getBasicSet(
				companyId, employeeId, criteria, workingSystem);
		
		for (PeriodAtrOfAgreement periodAtr : periodAtrs) {
			// ループする期間を判断
			int stepMon = 2;
			if (periodAtr == PeriodAtrOfAgreement.THREE_MONTHS) stepMon = 3;
			if (periodAtr == PeriodAtrOfAgreement.ONE_MONTH) stepMon = 1;
			if (periodAtr == PeriodAtrOfAgreement.ONE_YEAR) stepMon = 12;

			for (int i = 0; i < 12; i += stepMon) {
				List<YearMonth> periodYmList = new ArrayList<>();
				for (int ii = 0; ii < stepMon; ii++) {
					periodYmList.add(startYm.addMonths(i + ii));
				}

				// 36協定時間を取得
				val agreementTimeList = agreementTimeByEmp.stream().filter(x -> periodYmList.contains(x.getYearMonth()))
						.collect(Collectors.toList());

				if (agreementTimeList.size() == 0) continue;

				// 期間をセット
				AgreementTimeByPeriod result = new AgreementTimeByPeriod(
						periodYmList.get(0), periodYmList.get(periodYmList.size() - 1));

				// 36協定時間を合計
				for (val agreeemntTime : agreementTimeList) {
					result.addMinutesToAgreementTime(agreeemntTime.getAgreementTime().getAgreementTime().v());
				}				

				// 取得した限度時間をセット
				switch (periodAtr) {
					case TWO_MONTHS:
						result.setLimitAlarmTime(new LimitOneYear(basicAgreementSet.getAlarmTwoMonths().v()));
						result.setLimitErrorTime(new LimitOneYear(basicAgreementSet.getErrorTwoMonths().v()));
						break;
					case THREE_MONTHS:
						result.setLimitAlarmTime(new LimitOneYear(basicAgreementSet.getAlarmThreeMonths().v()));
						result.setLimitErrorTime(new LimitOneYear(basicAgreementSet.getErrorThreeMonths().v()));
						break;
					case ONE_YEAR:
						result.setLimitAlarmTime(new LimitOneYear(basicAgreementSet.getAlarmOneYear().v()));
						result.setLimitErrorTime(new LimitOneYear(basicAgreementSet.getErrorOneYear().v()));
						if (yearSetByEmp != null) {
							result.setExceptionLimitAlarmTime(Optional.of(
									new LimitOneYear(yearSetByEmp.getAlarmOneYear().v())));
							result.setExceptionLimitErrorTime(Optional.of(
									new LimitOneYear(yearSetByEmp.getErrorOneYear().v())));
						}
						break;
					default:    // ONE_MONTH
						result.setLimitAlarmTime(new LimitOneYear(basicAgreementSet.getAlarmOneMonth().v()));
						result.setLimitErrorTime(new LimitOneYear(basicAgreementSet.getErrorOneMonth().v()));
						int month = startYm.addMonths(i + 1).v();
						if (monthSetByEmp.containsKey(month)) {
							val monthSet = monthSetByEmp.get(month);
							result.setExceptionLimitAlarmTime(Optional.of(
									new LimitOneYear(monthSet.getAlarmOneMonth().v())));
							result.setExceptionLimitErrorTime(Optional.of(
									new LimitOneYear(monthSet.getErrorOneMonth().v())));
						}
						break;
				}
				// チェック処理
				result.errorCheck();
				AgreementTimeByEmp agreementTime = new AgreementTimeByEmp(employeeId, periodAtr, result);
				agreementTimes.add(agreementTime);
			}
		}
		return agreementTimes;
	}
}
