package nts.uk.ctx.at.shared.dom.dailyattdcal.dailycalprocess.calculation.other.vacationusetime;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * 日別実績の特別休暇
 * @author keisuke_hoshina
 *
 */
@Getter
public class SpecialHolidayOfDaily {
	//使用時間
	private AttendanceTime useTime;
	//時間消化使用時間
	private AttendanceTime digestionUseTime;

	/**
	 * Constructor 
	 */
	public SpecialHolidayOfDaily(AttendanceTime useTime, AttendanceTime digestionUseTime) {
		super();
		this.useTime = useTime;
		this.digestionUseTime = digestionUseTime;
	}
}
