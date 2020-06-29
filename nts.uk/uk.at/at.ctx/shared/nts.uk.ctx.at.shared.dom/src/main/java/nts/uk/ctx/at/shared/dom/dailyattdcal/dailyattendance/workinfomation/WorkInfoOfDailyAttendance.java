package nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.workinfomation;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.objecttype.DomainObject;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;

/**
 * 日別勤怠の勤務情報
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared(勤務予定、勤務実績).日の勤怠計算.日別勤怠.勤務情報.日別勤怠の勤務情報
 * @author tutk
 * 
 *
 */
@Getter
@NoArgsConstructor
public class WorkInfoOfDailyAttendance implements DomainObject {

	@Setter
	// 勤務実績の勤務情報
	private WorkInformation recordInfo;
	@Setter
	// 勤務予定の勤務情報
	private WorkInformation scheduleInfo;
	@Setter
	// 計算状態
	private CalculationState calculationState;
	@Setter
	// 直行区分
	private NotUseAttribute goStraightAtr;
	@Setter
	// 直帰区分
	private NotUseAttribute backStraightAtr;
	@Setter
	// 曜日
	private DayOfWeek dayOfWeek;
	// 勤務予定時間帯
	@Setter
	private List<ScheduleTimeSheet> scheduleTimeSheets;
	public WorkInfoOfDailyAttendance(WorkInformation recordInfo, WorkInformation scheduleInfo,
			CalculationState calculationState, NotUseAttribute goStraightAtr, NotUseAttribute backStraightAtr,
			DayOfWeek dayOfWeek, List<ScheduleTimeSheet> scheduleTimeSheets) {
		super();
		this.recordInfo = recordInfo;
		this.scheduleInfo = scheduleInfo;
		this.calculationState = calculationState;
		this.goStraightAtr = goStraightAtr;
		this.backStraightAtr = backStraightAtr;
		this.dayOfWeek = dayOfWeek;
		this.scheduleTimeSheets = scheduleTimeSheets;
	}
	
	/**
	 * 計算ステータスの変更
	 * @param state 計算ステータス
	 */
	public void changeCalcState(CalculationState state) {
		this.setCalculationState(state);
	}
	
	/**
	 * 指定された勤務回数の予定時間帯を取得する
	 * @param workNo
	 * @return　予定時間帯
	 */
	public Optional<ScheduleTimeSheet> getScheduleTimeSheet(WorkNo workNo) {
		return this.scheduleTimeSheets.stream()
				.filter(ts -> ts.getWorkNo().equals(workNo)).findFirst();	
	}
	
}
