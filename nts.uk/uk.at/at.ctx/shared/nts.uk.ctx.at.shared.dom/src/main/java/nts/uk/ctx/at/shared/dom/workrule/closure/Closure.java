/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.workrule.closure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class Closure.
 */
//締め
@Getter
public class Closure extends AggregateRoot {

	/** The company id. */
	// 会社ID
	private CompanyId companyId;

	/** The closure id. */
	// 締めＩＤ
	private ClosureId closureId;

	/** The use classification. */
	// 使用区分
	private UseClassification useClassification;

	/** The month. */
	// 当月
	private CurrentMonth closureMonth;

	/** The closure histories. */
	// 締め変更履歴
	@Setter
	private List<ClosureHistory> closureHistories;

	/**
	 * Instantiates a new closure.
	 *
	 * @param memento the memento
	 */
	public Closure(ClosureGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.closureId = memento.getClosureId();
		this.useClassification = memento.getUseClassification();
		this.closureMonth = memento.getClosureMonth();
		this.closureHistories = memento.getClosureHistories();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(ClosureSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setClosureId(this.closureId);
		memento.setUseClassification(this.useClassification);
		memento.setClosureMonth(this.closureMonth);
		memento.setClosureHistories(this.closureHistories);
	}

	/**
	 * Gets the history by base date.
	 *
	 * @param baseDate the base date
	 * @return the history by base date
	 */
	public ClosureHistory getHistoryByBaseDate(GeneralDate baseDate) {
		return this.closureHistories.stream().filter(his -> his.getClosureYMD().beforeOrEquals(baseDate)).findFirst()
				.get();
	}

	/**
	 * 指定した年月が含まれる締め変更履歴を取得する
	 * @param yearMonth 年月
	 * @return 締め変更履歴
	 */
	// 2018.3.11 add shuichi_ishida
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<ClosureHistory> getHistoryByYearMonth(YearMonth yearMonth) {
		for (ClosureHistory closureHistory : this.closureHistories){
			if (closureHistory.getStartYearMonth().lessThanOrEqualTo(yearMonth) &&
				closureHistory.getEndYearMonth().greaterThanOrEqualTo(yearMonth)){
				return Optional.ofNullable(closureHistory);
			}
		}
		return Optional.empty();
	}

	/**
	 * 指定した年月日時点の締め期間を取得する
	 * @param ymd 年月日
	 * @return 締め期間
	 */
	// 2018.3.11 add shuichi_ishida
	public Optional<ClosurePeriod> getClosurePeriodByYmd(GeneralDate ymd) {
		
		ClosurePeriod closurePeriod = null;
		
		YearMonth minYearMonth = YearMonth.of(GeneralDate.min().year(), GeneralDate.min().month());
		YearMonth maxYearMonth = YearMonth.of(GeneralDate.max().year(), GeneralDate.max().month());
		
		// パラメータ「年月日」を年月に変換
		YearMonth yearMonth = YearMonth.of(ymd.year(), ymd.month());
		while (true){
			
			// 指定した年月の期間を全て取得する
			List<DatePeriod> periods = this.getPeriodByYearMonth(yearMonth);
			if (periods.size() <= 0) return Optional.empty();
			int endState = 0;
			if (periods.size() > 1){
				
				// 締め日変更がある月の締め期間を作成する
				val periodBeforeChange = periods.get(0);
				val periodAfterChange = periods.get(1);
				if (ymd.before(periodBeforeChange.start())){
					endState = -1;
				}
				else if (periodBeforeChange.contains(ymd)){
					val closureHistoryOpt = this.getHistoryByYearMonth(yearMonth.addMonths(-1));
					if (!closureHistoryOpt.isPresent()) return Optional.empty();
					closurePeriod = ClosurePeriod.of(
							this.closureId,
							closureHistoryOpt.get().getClosureDate(),
							yearMonth.addMonths(-1),
							periodBeforeChange);
					endState = 0;
				}
				else if (periodAfterChange.end().before(ymd)){
					endState = 1;
				}
				else {
					val closureHistoryOpt = this.getHistoryByYearMonth(yearMonth);
					if (!closureHistoryOpt.isPresent()) return Optional.empty();
					closurePeriod = ClosurePeriod.of(
							this.closureId,
							closureHistoryOpt.get().getClosureDate(),
							yearMonth,
							periodAfterChange);
					endState = 0;
				}
			}
			else {
				
				// 締め日変更がない月の締め期間を作成する
				if (periods.get(0).contains(ymd)){
					val closureHistoryOpt = this.getHistoryByYearMonth(yearMonth);
					if (!closureHistoryOpt.isPresent()) return Optional.empty();
					closurePeriod = ClosurePeriod.of(
							this.closureId,
							closureHistoryOpt.get().getClosureDate(),
							yearMonth,
							periods.get(0));
					endState = 0;
				}
				else if (ymd.before(periods.get(0).start())){
					endState = -1;
				}
				else {
					endState = 1;
				}
			}
			if (endState == 0) break;
			if (endState < 0){
				if (yearMonth.lessThanOrEqualTo(minYearMonth)) break;
				yearMonth = yearMonth.previousMonth();
			}
			if (endState > 0){
				if (yearMonth.greaterThanOrEqualTo(maxYearMonth)) break;
				yearMonth = yearMonth.nextMonth();
			}
		}
		return Optional.ofNullable(closurePeriod);
	}
	
	/**
	 * 指定した年月の期間をすべて取得する
	 * @param yearMonth 年月
	 * @return 期間リスト
	 */
	// 2018.3.11 add shuichi_ishida
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<DatePeriod> getPeriodByYearMonth(YearMonth yearMonth){
		
		List<DatePeriod> returnPeriod = new ArrayList<>();

		val prevMonth = yearMonth.previousMonth();
		val currentMonth = yearMonth;
		val nextMonth = yearMonth.nextMonth();
		
		val prevLastYmd = GeneralDate.ymd(currentMonth.year(), currentMonth.month(), 1).addDays(-1);
		val currentFirstYmd = GeneralDate.ymd(currentMonth.year(), currentMonth.month(), 1);
		val currentLastYmd = GeneralDate.ymd(nextMonth.year(), nextMonth.month(), 1).addDays(-1);
		
		// 「締め変更履歴」取得
		val currentHistoryOpt = this.getHistoryByYearMonth(yearMonth);
		if (!currentHistoryOpt.isPresent()) return returnPeriod;
		val currentHistory = currentHistoryOpt.get();
		
		// 年月　＝　締め変更履歴．開始年月　の時
		if (yearMonth.equals(currentHistory.getStartYearMonth())){
			// 前月の「締め変更履歴」取得
			val prevHistoryOpt = this.getHistoryByYearMonth(prevMonth);
			if (prevHistoryOpt.isPresent()) {
				val prevHistory = prevHistoryOpt.get();
				
				// 前月履歴の締め日を算出
				GeneralDate prevHistoryYmd = prevLastYmd;
				if (!prevHistory.getClosureDate().getLastDayOfMonth()){
					val prevHistoryDay = prevHistory.getClosureDate().getClosureDay().v();
					if (prevHistoryDay < prevLastYmd.day()){
						prevHistoryYmd = GeneralDate.ymd(prevMonth.year(), prevMonth.month(), prevHistoryDay);
					}
				}
				
				// 当月締め日を元にした前月締め日を算出
				GeneralDate currentPrevYmd = prevLastYmd;
				if (!currentHistory.getClosureDate().getLastDayOfMonth()){
					val currentHistoryDay = currentHistory.getClosureDate().getClosureDay().v();
					if (currentHistoryDay < currentPrevYmd.day()){
						currentPrevYmd = GeneralDate.ymd(prevMonth.year(), prevMonth.month(), currentHistoryDay);
					}
				}
				
				// 当月締め日を算出
				GeneralDate endYmd2 = currentLastYmd;
				if (!currentHistory.getClosureDate().getLastDayOfMonth()){
					val currentHistoryDay = currentHistory.getClosureDate().getClosureDay().v();
					if (currentHistoryDay < endYmd2.day()){
						endYmd2 = GeneralDate.ymd(currentMonth.year(), currentMonth.month(), currentHistoryDay);
					}
				}
				
				// 前月締め日が前月履歴の締め日以前になる時
				if (currentPrevYmd.beforeOrEquals(prevHistoryYmd)){
					// 前月履歴の締め日+1　～　当月締め日
					val startYmd2 = prevHistoryYmd.addDays(1);
					returnPeriod.add(new DatePeriod(startYmd2, endYmd2));
				}
				// 前月締め日が前月履歴の締め日より後になる時
				else {
					// 前月履歴の締め日+1　～　前月締め日
					val startYmd1 = prevHistoryYmd.addDays(1);
					val endYmd1 = currentPrevYmd;
					returnPeriod.add(new DatePeriod(startYmd1, endYmd1));
					// 前月締め日+1　～　当月締め日
					val startYmd2 = endYmd1.addDays(1);
					returnPeriod.add(new DatePeriod(startYmd2, endYmd2));
				}
				return returnPeriod;
			}
		}
		
		// 年月　≠　締め変更履歴．開始年月　または　年月-1の「締め変更履歴」がない時
		// 末日とする時
		if (currentHistory.getClosureDate().getLastDayOfMonth()){
			
			// 当月1日　～　当月末日
			returnPeriod.add(new DatePeriod(currentFirstYmd, currentLastYmd));
		}
		// 末日以外の時
		else {
			
			// 前月締め日+1　～　当月締め日
			val closureDay = currentHistory.getClosureDate().getClosureDay().v();
			GeneralDate startYmd = currentFirstYmd;
			GeneralDate endYmd = currentLastYmd;
			if (closureDay + 1 <= prevLastYmd.day()){
				startYmd = GeneralDate.ymd(prevMonth.year(), prevMonth.month(), closureDay + 1); 
			}
			if (closureDay <= currentLastYmd.day()){
				endYmd = GeneralDate.ymd(currentMonth.year(), currentMonth.month(), closureDay);
			}
			returnPeriod.add(new DatePeriod(startYmd, endYmd));
		}
		return returnPeriod;
	}
	
	/**
	 * 当月の締め日を取得する
	 * @return 締め日（日付）
	 */
	// 2018.4.5 add shuichu_ishida
	public Optional<ClosureDate> getClosureDateOfCurrentMonth(){
		
		val currentYm = this.closureMonth.getProcessingYm();
		val closureClassOpt = this.closureMonth.getClosureClassification();
		val maxYm = YearMonth.of(GeneralDate.max().year(), GeneralDate.max().month());
		
		// 「締め日変更区分」をチェック　→　締め変更履歴を取得
		ClosureHistory closureHistory = null;
		if (!closureClassOpt.isPresent()){
			val currentHistoryOpt = this.getHistoryByYearMonth(currentYm);
			if (currentHistoryOpt.isPresent()) closureHistory = currentHistoryOpt.get();
		}
		else {
			val closureClass = closureClassOpt.get();
			if (closureClass == ClosureClassification.ClassificationClosingBefore){
				val currentHistoryOpt = this.getHistoryByYearMonth(currentYm);
				if (currentHistoryOpt.isPresent()) closureHistory = currentHistoryOpt.get();
			}
			else {
				if (currentYm.lessThan(maxYm)){
					val nextHistoryOpt = this.getHistoryByYearMonth(currentYm.addMonths(1));
					if (nextHistoryOpt.isPresent()) closureHistory = nextHistoryOpt.get();
				}
			}
		}
		if (closureHistory == null) return Optional.empty();

		// 締め変更履歴．日付を返す
		return Optional.of(closureHistory.getClosureDate());
	}
	
	/**
	 * 当月を次月へ更新する
	 * Update the current month to the next month
	 */
	public void updateCurrentMonth() {
//		// check ClosureHistory contain CurrentMonth
//		boolean containCurrentMonth = this.closureHistories.stream().anyMatch(history -> {
//			return this.isIntoClosureMonth(history, this.closureMonth.getProcessingYm());
//		});
//		// not contain
//		if (!containCurrentMonth) {
//			return;
//		}
//
//		// get closureMonth in ClosureHistory
//		ClosureHistory currentClosureMonth = this.closureHistories.stream()
//				.filter(history -> this.isIntoClosureMonth(history, this.closureMonth.getProcessingYm())).findFirst()
//				.get();
//
//		// get previous closureMonth in ClosureHistory
//		YearMonth previousYearMonth = YearMonth.of(this.closureMonth.getProcessingYm().v() - 1);
//		ClosureHistory previousClosureMonth = this.closureHistories.stream()
//				.filter(history -> this.isIntoClosureMonth(history, previousYearMonth)).findFirst().get();
//
//		// if closureDate current <= previous closureDate
//		if (currentClosureMonth.getClosureDate().getClosureDay().v() <= previousClosureMonth.getClosureDate()
//				.getClosureDay().v()) {
//			return;
//		}
//
//		// check closureClassification
//		if (!this.getClosureMonth().getClosureClassification().isPresent()) {
//			return;
//		}
//
//		switch (this.getClosureMonth().getClosureClassification().get()) {
//		case ClassificationClosingBefore:
//			this.getClosureMonth().setClosureClassification(Optional.of(ClosureClassification.ClassificationClosingAfter));
//			break;
//		case ClassificationClosingAfter:
//			this.getClosureMonth().setClosureClassification(Optional.of(ClosureClassification.ClassificationClosingBefore));
//			break;
//		}
		
		this.closureMonth.nextMonth();
	}
	
	/**
	 * Into closure month.
	 * Update the current month to the next month
	 *
	 * @param closureHistory the closure history
	 * @param currentMonth the current month
	 * @return true, if successful
	 */
	@SuppressWarnings("unused")
	private boolean isIntoClosureMonth(ClosureHistory closureHistory, YearMonth currentMonth) {
		YearMonth startYearMonth = closureHistory.getStartYearMonth();
		YearMonth endYearMonth = closureHistory.getEndYearMonth();
		return startYearMonth.lessThan(currentMonth) && endYearMonth.greaterThan(currentMonth);
	}
}
