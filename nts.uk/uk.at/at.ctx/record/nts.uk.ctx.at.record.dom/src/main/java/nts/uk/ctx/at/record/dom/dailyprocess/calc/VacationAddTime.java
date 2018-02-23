package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * 休暇加算時間
 * @author ken_takasu
 *
 */
@AllArgsConstructor
@Value
public class VacationAddTime {
	
	private AttendanceTime AnnualLeave;
	private AttendanceTime AccumulationVacation;
	private AttendanceTime SpecialHoliday;
	
	
	/**
	 * 引数として渡した休暇加算時間を加算した休暇加算時間を返す
	 * @author ken_takasu
	 * @return
	 */
	public VacationAddTime addVacationAddTime(VacationAddTime vacationAddTime) {
		return new VacationAddTime(new AttendanceTime(this.AnnualLeave.valueAsMinutes() + vacationAddTime.AnnualLeave.valueAsMinutes()),
									new AttendanceTime(this.AccumulationVacation.valueAsMinutes() + vacationAddTime.AccumulationVacation.valueAsMinutes()),
									new AttendanceTime(this.SpecialHoliday.valueAsMinutes() + vacationAddTime.SpecialHoliday.valueAsMinutes()));
	}
	
	
	/**
	 * 休暇加算時間の合計時間を返す処理
	 * @return
	 */
	public int calcTotaladdVacationAddTime() {
		return this.AnnualLeave.valueAsMinutes() + this.AccumulationVacation.valueAsMinutes() + this.SpecialHoliday.valueAsMinutes();
	}
	
}
