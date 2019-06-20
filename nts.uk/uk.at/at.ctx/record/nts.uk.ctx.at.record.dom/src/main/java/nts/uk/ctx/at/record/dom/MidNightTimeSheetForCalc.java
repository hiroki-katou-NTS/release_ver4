package nts.uk.ctx.at.record.dom;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.val;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ConditionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.SpecBonusPayTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSheetOfDeductionItem;
//import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
//import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 深夜時間帯
 * @author keisuke_hoshina
 *
 */
public class MidNightTimeSheetForCalc extends CalculationTimeSheet{

	public MidNightTimeSheetForCalc(TimeZoneRounding timeSheet, TimeSpanForCalc calculationTimeSheet,List<TimeSheetOfDeductionItem> recorddeductionSheets,List<TimeSheetOfDeductionItem> deductionSheets,
			List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet,Optional<MidNightTimeSheetForCalc> midNighttimeSheet) {
		super(timeSheet, calculationTimeSheet,recorddeductionSheets,deductionSheets,bonusPayTimeSheet,specifiedBonusPayTimeSheet,midNighttimeSheet);
	}
	
	
	public MidNightTimeSheetForCalc replaceTime(TimeSpanForCalc timeSpan) {
		return new MidNightTimeSheetForCalc(new TimeZoneRounding(timeSpan.getStart(), timeSpan.getEnd(), this.timeSheet.getRounding()),
											timeSpan,
											this.recordedTimeSheet,
											this.deductionTimeSheet,
											this.bonusPayTimeSheet,
											this.specBonusPayTimesheet,
											this.midNightTimeSheet);
	}
	
	
	public boolean contains(TimeWithDayAttr baseTime) {
		return ((CalculationTimeSheet)this).getCalcrange().contains(baseTime);
	}
	/**
	 * 終了時間と基準時間の早い方の時間を取得する
	 * @param basePoint　基準時間
	 * @return 時刻が早い方
	 */
	public TimeSpanForCalc decisionNewSpan(TimeSpanForCalc timeSpan,TimeWithDayAttr baseTime,boolean isDateBefore) {
		if(isDateBefore) {
			val endOclock = timeSpan.getEnd().lessThan(baseTime) ? timeSpan.getEnd() : baseTime; 
			return new TimeSpanForCalc(timeSpan.getStart(),endOclock);
		}
		else {
			val startOclock = baseTime.lessThan(timeSpan.getStart()) ? timeSpan.getStart() : baseTime ;
			return new TimeSpanForCalc(startOclock,timeSpan.getEnd());
		}
		
	}
	/**
	 * 再帰中に自分自身を作り直す処理
	 * @param baseTime
	 * @return
	 */
	public Optional<MidNightTimeSheetForCalc> reCreateOwn(TimeWithDayAttr baseTime,boolean isDateBefore) {
		List<TimeSheetOfDeductionItem> deductionTimeSheets = this.recreateDeductionItemBeforeBase(baseTime,isDateBefore,DeductionAtr.Appropriate);
		List<TimeSheetOfDeductionItem> recordTimeSheets = this.recreateDeductionItemBeforeBase(baseTime,isDateBefore,DeductionAtr.Deduction);
		List<BonusPayTimeSheetForCalc>        bonusPayTimeSheet = this.recreateBonusPayListBeforeBase(baseTime,isDateBefore);
		List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet = this.recreateSpecifiedBonusPayListBeforeBase(baseTime, isDateBefore);
		Optional<MidNightTimeSheetForCalc>    midNighttimeSheet = this.recreateMidNightTimeSheetBeforeBase(baseTime,isDateBefore);
		TimeSpanForCalc renewSpan = decisionNewSpan(this.getCalcrange(),baseTime,isDateBefore);
		TimeZoneRounding roundingset = new TimeZoneRounding(renewSpan.getStart(), renewSpan.getEnd(), this.timeSheet.getRounding());
		return Optional.of(new MidNightTimeSheetForCalc(roundingset,renewSpan,deductionTimeSheets,recordTimeSheets,bonusPayTimeSheet,specifiedBonusPayTimeSheet,midNighttimeSheet));
	}
	
	
	
	/**
	 * 深夜時間帯から計算用時間帯への変更
	 * @param midNightTimeSheet 深夜時間帯
	 * @return 計算用深夜時間帯
	 */
	public static MidNightTimeSheetForCalc convertForCalc(MidNightTimeSheet midNightTimeSheet,Optional<WorkTimezoneCommonSet> commonSetting) {
		TimeRoundingSetting timeRoundingSetting = commonSetting.isPresent()?commonSetting.get().getLateNightTimeSet().getRoundingSetting():new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN,Rounding.ROUNDING_DOWN);
		return new MidNightTimeSheetForCalc(new TimeZoneRounding(midNightTimeSheet.getStart(), midNightTimeSheet.getEnd(), timeRoundingSetting),
											new TimeSpanForCalc(midNightTimeSheet.getStart(),midNightTimeSheet.getEnd()), 
											Collections.emptyList(), 
											Collections.emptyList(), 
											Collections.emptyList(), 
											Collections.emptyList(), 
											Optional.empty());
	}
	
	/**
	 * 重複範囲の時間帯を持つ計算用深夜時間帯に作り直す
	 * @param timeSpan　重複を調べたい時間帯
	 * @return　重複範囲の時間帯深夜時間帯
	 */
	public Optional<MidNightTimeSheetForCalc> getDuplicateRangeTimeSheet(TimeSpanForCalc timeSpan) {
		
		val dupTimeSpan = timeSpan.getDuplicatedWith(this.calcrange);
		if(dupTimeSpan.isPresent()) {
			return Optional.of(new MidNightTimeSheetForCalc(new TimeZoneRounding(dupTimeSpan.get().getStart(), dupTimeSpan.get().getEnd(), this.timeSheet.getRounding()), 
											dupTimeSpan.get(), 
											this.recordedTimeSheet, 
											this.deductionTimeSheet, 
											this.bonusPayTimeSheet, 
											this.specBonusPayTimesheet, 
											this.midNightTimeSheet));
		}
		else {
			return Optional.empty();
		}
	}
	
	
	public MidNightTimeSheetForCalc getTerminalMidnightTimeSheet() {
		return this.midNightTimeSheet
				.map(ts -> ts.getTerminalMidnightTimeSheet())
				.orElse(this);
	}
	
	public AttendanceTime testSAIKI(DeductionAtr dedAtr,ConditionAtr conditionAtr) {
		//自分が持つ集計対象の時間帯の合計
		val includeForcsValue = super.forcs(conditionAtr, dedAtr);
		//自分自身が集計対象外の場合、自分自身が持つ集計対象の時間帯の合計時間のみを返す
		return includeForcsValue;
	}
}
