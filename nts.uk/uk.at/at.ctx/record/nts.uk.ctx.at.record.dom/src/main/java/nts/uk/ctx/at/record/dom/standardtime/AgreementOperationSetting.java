package nts.uk.ctx.at.record.dom.standardtime;

import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.standardtime.enums.ClosingDateType;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.standardtime.enums.ClosingDateAtr;
import nts.uk.ctx.at.record.dom.standardtime.enums.TimeOverLimitType;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.standardtime.enums.StartingMonthType;
import nts.uk.ctx.at.record.dom.standardtime.enums.TargetSettingAtr;

/**
 * 
 * @author nampt
 *
 */
@Getter
public class AgreementOperationSetting extends AggregateRoot {

	private String companyId;

	private StartingMonthType startingMonth;

	private TimeOverLimitType numberTimesOverLimitType;

	private ClosingDateType closingDateType;

	private ClosingDateAtr closingDateAtr;

	private TargetSettingAtr yearlyWorkTableAtr;

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
		if (this.closingDateAtr == ClosingDateAtr.SAMEDATE){
			aggrPeriod.setPeriod(period);
		}
		if (this.closingDateAtr == ClosingDateAtr.DESIGNATEDATE){
			val startYMStart = GeneralDate.ymd(period.start().year(), period.start().month(), 1);
			val startYMEnd = GeneralDate.ymd(period.start().year(), period.start().month(), 1).addMonths(1).addDays(-1);
			val endYMEnd = GeneralDate.ymd(period.end().year(), period.end().month(), 1).addMonths(1).addDays(-1);
			if (this.closingDateType == ClosingDateType.LASTDAY){
				// 開始月の末締め
				aggrPeriod.setPeriod(new DatePeriod(startYMStart, startYMEnd));
			}
			else {
				// 集計期間の開始月締め開始日～終了月締め日
				int closureDay = this.closingDateType.value + 1;
				GeneralDate closingStart = startYMEnd.addDays(1);
				if (closureDay + 1 <= startYMEnd.day()){
					closingStart = GeneralDate.ymd(startYMEnd.year(), startYMEnd.month(), closureDay + 1);
				}
				GeneralDate closingEnd = endYMEnd;
				if (closureDay <= endYMEnd.day()){
					closingEnd = GeneralDate.ymd(endYMEnd.year(), endYMEnd.month(), closureDay);
				}
				aggrPeriod.setPeriod(new DatePeriod(closingStart, closingEnd));
				if (aggrPeriod.getPeriod().isReversed()){
					// 開始＞終了の時、開始を１か月前にずらす
					val prevYMEnd = GeneralDate.ymd(period.start().year(), period.start().month(), 1).addDays(-1);
					closingStart = prevYMEnd.addDays(1);
					if (closureDay + 1 <= prevYMEnd.day()){
						closingStart = GeneralDate.ymd(prevYMEnd.year(), prevYMEnd.month(), closureDay + 1);
					}
					aggrPeriod.setPeriod(new DatePeriod(closingStart, closingEnd));
				}
			}
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
		
		// 「締め日区分」を取得
		if (this.closingDateAtr == ClosingDateAtr.SAMEDATE){
			
			// 締め期間と同じ集計期間を取得
			val datePeriods = closure.getPeriodByYearMonth(yearMonth);
			if (datePeriods.size() <= 0) return Optional.empty();
			aggrPeriod.setPeriod(datePeriods.get(0));
			return Optional.of(aggrPeriod);
		}
		if (this.closingDateAtr == ClosingDateAtr.DESIGNATEDATE){
			
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
		return Optional.empty();
	}
}
