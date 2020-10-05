package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.other;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * 
 * @author nampt
 * 代休発生情報
 *
 */
@Getter
public class SubHolOccurrenceInfo {

	//時間
	private AttendanceTime time;
	
	//代休発生日数
	private SubHolOccurrenceDayNumber subHolOccurrenceDayNumber; 
}
