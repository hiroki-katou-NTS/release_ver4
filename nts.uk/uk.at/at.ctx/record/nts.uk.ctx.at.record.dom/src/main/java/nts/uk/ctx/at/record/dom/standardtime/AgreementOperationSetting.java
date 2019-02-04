package nts.uk.ctx.at.record.dom.standardtime;

import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.standardtime.enums.ClosingDateType;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.standardtime.enums.ClosingDateAtr;
import nts.uk.ctx.at.record.dom.standardtime.enums.TimeOverLimitType;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.standardtime.enums.StartingMonthType;
import nts.uk.ctx.at.record.dom.standardtime.enums.TargetSettingAtr;

/**
 * ３６協定運用設定
 * @author nampt
 *
 */
@Getter
public class AgreementOperationSetting extends AggregateRoot {

	/**会社ID**/
	private String companyId;

	/** ３６協定起算月 **/
	private StartingMonthType startingMonth;

	/** ３６協定超過上限回数 **/
	private TimeOverLimitType numberTimesOverLimitType;

	/** ３６協定締め日 **/
	private ClosingDateType closingDateType;

	/** ３６協定締め日区分 **/
	private ClosingDateAtr closingDateAtr;

	/** ３６協定対象設定 - 年間勤務表 **/
	private TargetSettingAtr yearlyWorkTableAtr;

	/** ３６協定対象設定 - アラームリスト **/
	private TargetSettingAtr alarmListAtr;

	public AgreementOperationSetting(String companyId, StartingMonthType startingMonth,
			TimeOverLimitType numberTimesOverLimitType, ClosingDateType closingDateType,
			ClosingDateAtr closingDateAtr, TargetSettingAtr yearlyWorkTableAtr, TargetSettingAtr alarmListAtr) {
		super();
		this.companyId = companyId;
		this.startingMonth = startingMonth;
		this.numberTimesOverLimitType = numberTimesOverLimitType;
		this.closingDateType = closingDateType;
		this.closingDateAtr = closingDateAtr;
		this.yearlyWorkTableAtr = yearlyWorkTableAtr;
		this.alarmListAtr = alarmListAtr;
	}

	public static AgreementOperationSetting createFromJavaType(String companyId, int startingMonth,
			int numberTimesOverLimitType, int closingDateType, int closingDateAtr, int yearlyWorkTableAtr,
			int alarmListAtr) {
		return new AgreementOperationSetting(
				companyId,
				EnumAdaptor.valueOf(startingMonth, StartingMonthType.class),
				EnumAdaptor.valueOf(numberTimesOverLimitType, TimeOverLimitType.class),
				EnumAdaptor.valueOf(closingDateType, ClosingDateType.class),
				EnumAdaptor.valueOf(closingDateAtr, ClosingDateAtr.class),
				EnumAdaptor.valueOf(yearlyWorkTableAtr, TargetSettingAtr.class),
				EnumAdaptor.valueOf(alarmListAtr, TargetSettingAtr.class));
	}
	
	/**
	 * 集計期間を取得
	 * @param period 月別実績集計期間
	 * @return 集計期間
	 */
	// 2018.3.19 add shuichu_ishida
	public AggregatePeriod getAggregatePeriod(DatePeriod period){

		AggregatePeriod aggrPeriod = new AggregatePeriod();
		
		// 集計期間を取得
		val endYMStart = GeneralDate.ymd(period.end().year(), period.end().month(), 1);
		val endYMEnd = GeneralDate.ymd(period.end().year(), period.end().month(), 1).addMonths(1).addDays(-1);
		if (this.closingDateType == ClosingDateType.LASTDAY){
			// 終了月の末締め
			aggrPeriod.setPeriod(new DatePeriod(endYMStart, endYMEnd));
		}
		else {
			// 集計期間の終了月の締め期間を求める
			int closureDay = this.closingDateType.value + 1;
			GeneralDate closingEnd = endYMEnd;
			if (closureDay < closingEnd.day()){
				closingEnd = GeneralDate.ymd(endYMEnd.year(), endYMEnd.month(), closureDay);
			}
			GeneralDate closingStart = closingEnd.addMonths(-1).addDays(1);
			aggrPeriod.setPeriod(new DatePeriod(closingStart, closingEnd));
		}
		
		// 年度・年月の取得
		val aggrPeriodEnd = aggrPeriod.getPeriod().end();
		aggrPeriod.setYearMonth(YearMonth.of(aggrPeriodEnd.year(), aggrPeriodEnd.month()));
		int year = aggrPeriodEnd.year();
		if (aggrPeriodEnd.month() < this.startingMonth.value + 1) year--;
		aggrPeriod.setYear(new Year(year));
		
		return aggrPeriod;
	}
	
	/**
	 * 年月から集計期間を取得
	 * @param yearMonth 年月
	 * @param closure 締め
	 * @return 集計期間
	 */
	// 2018.3.25 add shuichu_ishida
	public Optional<AggregatePeriod> getAggregatePeriodByYearMonth(YearMonth yearMonth, Closure closure){

		AggregatePeriod aggrPeriod = new AggregatePeriod();
		aggrPeriod.setYearMonth(yearMonth);
		aggrPeriod.setYear(new Year(yearMonth.year()));	// 期首月　未配慮
		
		// 締め日を指定する場合の集計期間を取得
		val currentStart = GeneralDate.ymd(yearMonth.year(), yearMonth.month(), 1);
		val currentEnd = GeneralDate.ymd(yearMonth.year(), yearMonth.month(), 1).addMonths(1).addDays(-1);
		val prevEnd = currentStart.addDays(-1);
		if (this.closingDateType == ClosingDateType.LASTDAY){
			// 年月の末締め
			aggrPeriod.setPeriod(new DatePeriod(currentStart, currentEnd));
		}
		else {
			// 年月の締め開始日～締め終了日
			int closureDay = this.closingDateType.value + 1;
			GeneralDate closingStart = currentStart;
			if (closureDay + 1 <= prevEnd.day()){
				closingStart = GeneralDate.ymd(prevEnd.year(), prevEnd.month(), closureDay + 1);
			}
			GeneralDate closingEnd = currentEnd;
			if (closureDay <= currentEnd.day()){
				closingEnd = GeneralDate.ymd(currentEnd.year(), currentEnd.month(), closureDay);
			}
			aggrPeriod.setPeriod(new DatePeriod(closingStart, closingEnd));
		}
		return Optional.of(aggrPeriod);
	}
	
	/**
	 * 36協定Dto（年度の設定）
	 * @param source 管理期間の36協定時間
	 * @return 管理期間の36協定時間
	 */
	// 2019.2.5 add shuichi_ishida
	public AgreementTimeOfManagePeriod setYearDto(AgreementTimeOfManagePeriod source){
		
		if (source == null) return null;
		int calcYear = source.getYearMonth().year();
		int checkMonth = source.getYearMonth().month();
		if (checkMonth < this.startingMonth.value + 1) {
			calcYear--;
		}
		return AgreementTimeOfManagePeriod.of(
				source.getEmployeeId(),
				source.getYearMonth(),
				new Year(calcYear),
				source.getAgreementTime(),
				source.getBreakdown());
	}
}
