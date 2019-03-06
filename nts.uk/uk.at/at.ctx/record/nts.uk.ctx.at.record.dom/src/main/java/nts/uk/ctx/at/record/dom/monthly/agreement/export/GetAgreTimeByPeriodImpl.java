package nts.uk.ctx.at.record.dom.monthly.agreement.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriodRepository;
import nts.uk.ctx.at.record.dom.standardtime.AgreementMonthSetting;
import nts.uk.ctx.at.record.dom.standardtime.AgreementYearSetting;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitOneYear;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementDomainService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementMonthSettingRepository;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementYearSettingRepository;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeYear;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreMaxAverageTimeMulti;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreTimeYearStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeYear;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.LimitOneMonth;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
 * 実装：指定期間36協定時間の取得
 * @author shuichi_ishida
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
	/** 36協定運用設定の取得 */
	@Inject
	private AgreementOperationSettingRepository agreementOperationSetRepo;
	
	/** 指定期間36協定時間の取得 */
	@Override
	public List<AgreementTimeByPeriod> algorithm(String companyId, String employeeId, GeneralDate criteria,
			Month startMonth, Year year, PeriodAtrOfAgreement periodAtr) {
		
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
				result.addMinutesToAgreementTime(agreeemntTime.getAgreementTime().getAgreementTime().getAgreementTime().v());
			}
			
			Optional<Year> checkYearOpt = Optional.empty();
			Optional<YearMonth> checkYmOpt = Optional.empty();
			if (periodAtr == PeriodAtrOfAgreement.ONE_YEAR) checkYearOpt = Optional.of(year);
			if (periodAtr == PeriodAtrOfAgreement.ONE_MONTH) checkYmOpt = Optional.of(periodYmList.get(0));
			
			// 状態チェック
			{
				// 「労働条件項目」を取得
				val workingConditionItemOpt =
						this.workingConditionItem.getBySidAndStandardDate(employeeId, criteria);
				if (!workingConditionItemOpt.isPresent()){
					return results;
				}
				
				// 労働制を確認する
				val workingSystem = workingConditionItemOpt.get().getLaborSystem();
				
				// 36協定基本設定を取得する
				val basicAgreementSet = this.agreementDomainService.getBasicSet(
						companyId, employeeId, criteria, workingSystem).getBasicAgreementSetting();
				
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
	
	/** 指定月36協定上限月間時間の取得 */
	@Override
	public List<AgreMaxTimeMonthOut> maxTime(String companyId, String employeeId, YearMonthPeriod period) {

		List<AgreMaxTimeMonthOut> results = new ArrayList<>();
		
		// 年月期間を取得　→　年月分ループする
		for (YearMonth procYm = period.start(); procYm.lessThanOrEqualTo(period.end()); procYm = procYm.nextMonth()) {
	
			// 管理期間の36協定時間を取得
			val agreementTimeOfMngPrdOpt = this.agreementTimeOfMngPrdRepo.find(employeeId, procYm);
			if (agreementTimeOfMngPrdOpt.isPresent()) {
				results.add(AgreMaxTimeMonthOut.of(
						agreementTimeOfMngPrdOpt.get().getYearMonth(),
						agreementTimeOfMngPrdOpt.get().getAgreementMaxTime().getAgreementTime()));
			}
		}
		
		// 月別実績の36協定月間上限時間リストを返す
		return results;
	}
	
	/** 指定期間36協定上限複数月平均時間の取得 */
	@Override
	public Optional<AgreMaxAverageTimeMulti> maxAverageTimeMulti(String companyId, String employeeId, GeneralDate criteria,
			YearMonth yearMonth) {
		
		// 「労働条件項目」を取得
		val workingConditionItemOpt =
				this.workingConditionItem.getBySidAndStandardDate(employeeId, criteria);
		if (!workingConditionItemOpt.isPresent()) return Optional.empty();
		
		// 労働制を確認する
		val workingSystem = workingConditionItemOpt.get().getLaborSystem();
		
		// 36協定基本設定を取得する
		val upperAgreementSet = this.agreementDomainService.getBasicSet(
				companyId, employeeId, criteria, workingSystem).getUpperAgreementSetting();
		
		// 上限時間をセット
		int maxMinutes = upperAgreementSet.getUpperMonthAverage().v();
		
		// 指定年月から6ヶ月前を期間とする
		YearMonthPeriod allPeriod = new YearMonthPeriod(yearMonth.addMonths(-5), yearMonth);

		// 管理期間の36協定時間を取得
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		val agreTimeOfMngPeriodList = this.agreementTimeOfMngPrdRepo.findBySidsAndYearMonths(
				employeeIds, allPeriod.yearMonthsBetween());
		Map<YearMonth, AgreementTimeOfManagePeriod> agreTimeOfMngPeriodMap = new HashMap<>();
		for (val agreTimeOfMngPeriod : agreTimeOfMngPeriodList) {
			agreTimeOfMngPeriodMap.putIfAbsent(agreTimeOfMngPeriod.getYearMonth(), agreTimeOfMngPeriod);
		}

		// 36協定上限複数月平均時間を作成する
		AgreMaxAverageTimeMulti result = AgreementTimeOfManagePeriod.calcMaxAverageTimeMulti(
				yearMonth, new LimitOneMonth(maxMinutes), agreTimeOfMngPeriodList);
		
		// 36協定上限複数月平均時間を返す
		return Optional.of(result);
	}
	
	/** 指定年36協定年間時間の取得 */
	@Override
	public Optional<AgreementTimeYear> timeYear(String companyId, String employeeId, GeneralDate criteria, Year year) {
		
		// 「労働条件項目」を取得
		val workingConditionItemOpt =
				this.workingConditionItem.getBySidAndStandardDate(employeeId, criteria);
		if (!workingConditionItemOpt.isPresent()) return Optional.empty();
		
		// 労働制を確認する
		val workingSystem = workingConditionItemOpt.get().getLaborSystem();
		
		// 36協定基本設定を取得する
		val basicAgreementSet = this.agreementDomainService.getBasicSet(
				companyId, employeeId, criteria, workingSystem).getBasicAgreementSetting();
		
		// 上限時間をセット
		int maxMinutes = basicAgreementSet.getLimitOneYear().v();

		// 36協定運用設定の取得
		val agreementOpeSetOpt = this.agreementOperationSetRepo.find(companyId);
		if (!agreementOpeSetOpt.isPresent()) return Optional.empty();
		val agreementOpeSet = agreementOpeSetOpt.get();
		
		// 年度から36協定の年月期間を取得する
		val period = agreementOpeSet.getYearMonthPeriod(year);
		
		// 管理期間の36協定時間を取得
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		val agreementTimeOfMngPrdList = this.agreementTimeOfMngPrdRepo.findBySidsAndYearMonths(
				employeeIds, period.yearMonthsBetween());
		int totalMinutes = 0;
		for (val agreementTimeOfMngPrd : agreementTimeOfMngPrdList) {
			
			// 合計時間を合計する
			totalMinutes += agreementTimeOfMngPrd.getAgreementTime().getBreakdown().getTotalTime().v();
		}
		
		// 36協定年間時間を作成する
		AgreementTimeYear result = AgreementTimeYear.of(
				new nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.LimitOneYear(maxMinutes),
				new AttendanceTimeYear(totalMinutes),
				AgreTimeYearStatusOfMonthly.NORMAL);
		
		// エラーチェック
		result.errorCheck();
		
		// 36協定年間時間を返す
		return Optional.of(result);
	}
}
