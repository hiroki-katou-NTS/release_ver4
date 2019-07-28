package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 締め処理期間
 * @author shuichi_ishida
 */
@Getter
public class ClosurePeriod {

	/** 締めID */
	private ClosureId closureId;
	/** 締め日 */
	private ClosureDate closureDate;
	/** 年月 */
	private YearMonth yearMonth;
	/** 締め年月日 */
	private GeneralDate closureYmd;
	/** 集計期間 */
	@Setter
	private List<AggrPeriodEachActualClosure> aggrPeriods;
	
	/**
	 * コンストラクタ
	 */
	public ClosurePeriod(){
	
		this.closureId = ClosureId.RegularEmployee;
		this.closureDate = new ClosureDate(1, true);
		this.yearMonth = YearMonth.of(GeneralDate.today().year(), GeneralDate.today().month());
		this.closureYmd = GeneralDate.today();
		this.aggrPeriods = new ArrayList<>();
	}
	
	/**
	 * ファクトリー
	 * @param closureId 締めID
	 * @param closureDate 締め日
	 * @param yearMonth 年月
	 * @param closureYmd 締め年月日
	 * @param aggrPeriods 集計期間
	 * @return 締め処理期間
	 */
	public static ClosurePeriod of(
			ClosureId closureId, ClosureDate closureDate, YearMonth yearMonth, GeneralDate closureYmd,
			List<AggrPeriodEachActualClosure> aggrPeriods){
		
		ClosurePeriod domain = new ClosurePeriod();
		domain.closureId = closureId;
		domain.closureDate = closureDate;
		domain.yearMonth = yearMonth;
		domain.closureYmd = closureYmd;
		domain.aggrPeriods = aggrPeriods;
		return domain;
	}
	
	/**
	 * ファクトリー
	 * @param aggrPeriod 実締め毎集計期間
	 * @return 締め処理期間
	 */
	public static ClosurePeriod of(AggrPeriodEachActualClosure aggrPeriod){
		
		ClosurePeriod domain = new ClosurePeriod();
		domain.setValue(aggrPeriod);
		domain.aggrPeriods.add(aggrPeriod);
		return domain;
	}
	
	/**
	 * 実締め毎集計期間を追加する
	 * @param aggrPeriod 実締め毎集計期間
	 */
	public void addAggrPeriodEachActualClosure(AggrPeriodEachActualClosure aggrPeriod){
		
		this.aggrPeriods.add(aggrPeriod);
		if (this.equals(aggrPeriod)) this.setValue(aggrPeriod);
	}

	/**
	 * 等しいか確認する
	 * @param aggrPeriod 実締め毎集計期間
	 * @return true:等しい、false:等しくない
	 */
	public boolean equals(AggrPeriodEachActualClosure aggrPeriod){
		
		if (this.closureId.value == aggrPeriod.getClosureId().value &&
			this.closureDate.getClosureDay().equals(aggrPeriod.getClosureDate().getClosureDay()) &&
			this.closureDate.getLastDayOfMonth() == aggrPeriod.getClosureDate().getLastDayOfMonth() &&
			this.yearMonth.equals(aggrPeriod.getYearMonth())){
			return true;
		}
		return false;
	}

	/**
	 * 等しいか確認する
	 * @param target 締め処理期間
	 * @return true:等しい、false:等しくない
	 */
	public boolean equals(ClosurePeriod target){
		
		if (this.closureId.value == target.getClosureId().value &&
			this.closureDate.getClosureDay().equals(target.getClosureDate().getClosureDay()) &&
			this.closureDate.getLastDayOfMonth() == target.getClosureDate().getLastDayOfMonth() &&
			this.yearMonth.equals(target.getYearMonth())){
			return true;
		}
		return false;
	}
	
	/**
	 * 実締め毎集計期間から値をセットする
	 * @param aggrPeriod 実締め毎集計期間
	 */
	public void setValue(AggrPeriodEachActualClosure aggrPeriod){
		
		this.closureId = aggrPeriod.getClosureId();
		this.closureDate = aggrPeriod.getClosureDate();
		this.yearMonth = aggrPeriod.getYearMonth();
		this.closureYmd = aggrPeriod.getOriginalClosurePeriod().end();
	}
	
	/**
	 * 指定した年月日までの期間を除外する
	 * @param ymd 年月日
	 */
	public void excludePeriodByYmd(GeneralDate ymd){

		// 「集計期間」から年月日以前の期間のデータを削除
		this.aggrPeriods.removeIf(c -> {return c.getPeriod().end().beforeOrEquals(ymd);});
		
		for (val aggrPeriods : this.aggrPeriods){
			if (aggrPeriods.getPeriod().contains(ymd)){
				
				// 「集計期間．期間．開始日」　←　年月日 + 1
				aggrPeriods.setPeriod(new DatePeriod(ymd.addDays(1), aggrPeriods.getPeriod().end()));
			}
		}
	}
}
