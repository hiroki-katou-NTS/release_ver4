package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.anyitem;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AggregateAnyItem;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.converter.MonthlyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.calculation.Formula;
import nts.uk.ctx.at.record.dom.optitem.calculation.disporder.FormulaDispOrder;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyAmountMonth;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyTimeMonth;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyTimesMonth;

/**
 * 任意項目集計結果
 * @author shuichi_ishida
 */
@Getter
public class AnyItemAggrResult {

	/** 任意項目NO */
	private int optionalItemNo;
	/** 時間 */
	private AnyTimeMonth anyTime;
	/** 回数 */
	private AnyTimesMonth anyTimes;
	/** 金額 */
	private AnyAmountMonth anyAmount;
	
	private AnyItemAggrResult(){
		this.optionalItemNo = 0;
		this.anyTime = null;
		this.anyTimes = null;
		this.anyAmount = null;
	}

	/**
	 * ファクトリー
	 * @param optionalItemNo 任意項目NO
	 * @param anyTime 時間
	 * @param anyTimes 回数
	 * @param anyAmount 金額
	 * @return 任意項目集計結果
	 */
	public static AnyItemAggrResult of(
			int optionalItemNo,
			AnyTimeMonth anyTime,
			AnyTimesMonth anyTimes,
			AnyAmountMonth anyAmount){

		AnyItemAggrResult domain = new AnyItemAggrResult();
		domain.optionalItemNo = optionalItemNo;
		domain.anyTime = anyTime;
		domain.anyTimes = anyTimes;
		domain.anyAmount = anyAmount;
		return domain;
	}

	/**
	 * ファクトリー
	 * @param optionalItemNo 任意項目NO
	 * @param optionalItem 任意項目
	 * @return 任意項目集計結果
	 */
	public static AnyItemAggrResult of(
			int optionalItemNo,
			OptionalItem optionalItem){

		AnyItemAggrResult domain = new AnyItemAggrResult();
		domain.optionalItemNo = optionalItemNo;
		
		// 属性に応じて初期化
		AnyTimeMonth anyTime = null;
		AnyTimesMonth anyTimes = null;
		AnyAmountMonth anyAmount = null;
		switch (optionalItem.getOptionalItemAtr()){
		case TIME:
			anyTime = new AnyTimeMonth(0);
			break;
		case NUMBER:
			anyTimes = new AnyTimesMonth(0.0);
			break;
		case AMOUNT:
			anyAmount = new AnyAmountMonth(0);
			break;
		}
		
		domain.anyTime = anyTime;
		domain.anyTimes = anyTimes;
		domain.anyAmount = anyAmount;
		return domain;
	}
	
	/**
	 * 時間に分を加算する
	 * @param minutes 分
	 */
	public void addTime(int minutes){
		if (this.anyTime == null) this.anyTime = new AnyTimeMonth(0);
		this.anyTime = this.anyTime.addMinutes(minutes);
	}
	
	/**
	 * 回数に加算する
	 * @param times 回数
	 */
	public void addTimes(double times){
		if (this.anyTimes == null) this.anyTimes = new AnyTimesMonth(0.0);
		this.anyTimes = this.anyTimes.addTimes(times);
	}
	
	/**
	 * 金額に加算する
	 * @param amount 金額
	 */
	public void addAmount(int amount){
		if (this.anyAmount == null) this.anyAmount = new AnyAmountMonth(0);
		this.anyAmount = this.anyAmount.addAmount(amount);
	}
	
	/**
	 * 日別実績　縦計処理
	 * @param optionalItemNo 任意項目NO
	 * @param optionalItem 任意項目
	 * @param anyItemTotals 日別実績縦計結果
	 * @return 任意項目集計結果
	 */
	public static AnyItemAggrResult calcFromDailys(
			Integer optionalItemNo,
			OptionalItem optionalItem,
			Map<Integer, AggregateAnyItem> anyItemTotals){
		
		AnyItemAggrResult domain = AnyItemAggrResult.of(optionalItemNo, optionalItem);
		
		// 日別実績　縦計処理
		if (anyItemTotals.containsKey(optionalItemNo)){
			val anyItemTotal = anyItemTotals.get(optionalItemNo);
			if (anyItemTotal.getTime().isPresent()){
				domain.addTime(anyItemTotal.getTime().get().v());
			}
			if (anyItemTotal.getTimes().isPresent()){
				domain.addTimes(anyItemTotal.getTimes().get().v().doubleValue());
			}
			if (anyItemTotal.getAmount().isPresent()){
				domain.addAmount(anyItemTotal.getAmount().get().v());
			}
		}
		
		// 任意項目集計結果を返す
		return domain;
	}
	
	/**
	 * 月別実績　計算処理
	 * @param optionalItemNo 任意項目NO
	 * @param optionalItem 任意項目
	 * @param attendanceTime 月別実績の勤怠時間
	 * @param anyItems 月別実績の任意項目リスト
	 * @param companySets 月別集計で必要な会社別設定
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 任意項目集計結果
	 */
	public static AnyItemAggrResult calcFromMonthly(
			Integer optionalItemNo,
			OptionalItem optionalItem,
			AttendanceTimeOfMonthly attendanceTime,
			List<AnyItemOfMonthly> anyItems,
			MonAggrCompanySettings companySets,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		AnyItemAggrResult domain = AnyItemAggrResult.of(optionalItemNo, optionalItem);
		
		// 月別実績　計算処理
		List<Formula> targetFormulas = companySets.getFormulaList().stream()
				.filter(c -> c.getOptionalItemNo().equals(optionalItem.getOptionalItemNo()))
				.collect(Collectors.toList());
		List<FormulaDispOrder> targetFormulaOrders = companySets.getFormulaOrderList().stream()
				.filter(c -> c.getOptionalItemNo().equals(optionalItem.getOptionalItemNo()))
				.collect(Collectors.toList());
		val monthlyConverter = repositories.getAttendanceItemConverter().createMonthlyConverter();
		MonthlyRecordToAttendanceItemConverter monthlyRecordDto = monthlyConverter.withAttendanceTime(attendanceTime);
		monthlyRecordDto = monthlyRecordDto.withAnyItem(anyItems);
		val calcResult = optionalItem.caluculationFormula(
				companySets.getCompanyId(), optionalItem, targetFormulas, targetFormulaOrders,
				Optional.empty(), Optional.of(monthlyRecordDto));
		if (calcResult != null){
			if (calcResult.getTime().isPresent()){
				domain.addTime(calcResult.getTime().get().intValue());
			}
			if (calcResult.getCount().isPresent()){
				domain.addTimes(calcResult.getCount().get().doubleValue());
			}
			if (calcResult.getMoney().isPresent()){
				domain.addAmount(calcResult.getMoney().get().intValue());
			}
		}
		
		// 任意項目集計結果を返す
		return domain;
	}
}
