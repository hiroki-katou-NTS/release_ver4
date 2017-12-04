package nts.uk.ctx.at.record.dom.daily.holidayworktime;

import lombok.Value;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;

/**
 * 休出深夜時間
 * @author keisuke_hoshina
 *
 */
@Value
public class HolidayWorkMidNightTime {
	private TimeWithCalculation time;
	private StaturoryAtrOfHolidayWork statutoryAtr;
}
