package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.List;

import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSpanForDailyCalc;

/**
 * 非勤務時間帯
 * @author keisuke_hoshina
 *
 */
public class NonWorkingTimeSheet {

	//所定内休憩時間帯
	List<TimeSpanForDailyCalc> whithinBreakTimeSheet;
	//所定外休憩時間帯
	List<TimeSpanForDailyCalc> excessBreakTimeSheet;
	
	
	/**
	 * Constructor 
	 */
	public NonWorkingTimeSheet(List<TimeSpanForDailyCalc> whithinBreakTimeSheet,
			List<TimeSpanForDailyCalc> excessBreakTimeSheet) {
		super();
		this.whithinBreakTimeSheet = whithinBreakTimeSheet;
		this.excessBreakTimeSheet = excessBreakTimeSheet;
	}
}
