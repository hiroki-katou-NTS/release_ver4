package nts.uk.ctx.at.record.dom.workinformation;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.enums.NotUseAttribute;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;

/**
 * 
 * @author nampt
 * 日別実績の勤務情報 - root
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class WorkInfoOfDailyPerformance extends AggregateRoot {

	private String employeeId;

	private WorkInformation recordInfo;

	private WorkInformation scheduleInfo;

	private CalculationState calculationState;

	// 直行区分
	private NotUseAttribute goStraightAtr;

	// 直帰区分
	private NotUseAttribute backStraightAtr;

	private GeneralDate ymd;
	
	private DayOfWeek dayOfWeek;

	private List<ScheduleTimeSheet> scheduleTimeSheets;

	public WorkInfoOfDailyPerformance(String employeeId, WorkInformation recordWorkInformation,
			WorkInformation scheduleWorkInformation, CalculationState calculationState, NotUseAttribute goStraightAtr,
			NotUseAttribute backStraightAtr, GeneralDate ymd, List<ScheduleTimeSheet> scheduleTimeSheets) {
		this.employeeId = employeeId;
		this.recordInfo = recordWorkInformation;
		this.scheduleInfo = scheduleWorkInformation;
		this.calculationState = calculationState;
		this.goStraightAtr = goStraightAtr;
		this.backStraightAtr = backStraightAtr;
		this.ymd = ymd;
		this.scheduleTimeSheets = scheduleTimeSheets;
		//一時対応
		this.dayOfWeek = DayOfWeek.MONDAY;
	} 
	
	/**
	 * 勤務予定を実績に移す
	 */
	public void shiftFromScheduleToRecord() {
		recordInfo = scheduleInfo;
	}
	
	/** <<Event>> 実績の就業時間帯が変更されたを発行する */
	public void workTimeChanged() {
		WorkInfoChangeEvent.builder().employeeId(employeeId).targetDate(ymd)
				.newWorkTimeCode(recordInfo == null ? null : recordInfo.getWorkTimeCode()).build().toBePublished();
	}
	
	/** <<Event>> 実績の勤務種類が変更されたを発行する */
	public void workTypeChanged() {
		WorkInfoChangeEvent.builder().employeeId(employeeId).targetDate(ymd)
				.newWorkTypeCode(recordInfo == null ? null : recordInfo.getWorkTypeCode()).build().toBePublished();
	}

	/** <<Event>> 実績の就業時間帯が変更されたを発行する */
	/** <<Event>> 実績の勤務種類が変更されたを発行する */
	public void workInfoChanged() {
		WorkInfoChangeEvent.builder().employeeId(employeeId).targetDate(ymd)
				.newWorkTypeCode(recordInfo == null ? null : recordInfo.getWorkTypeCode())
				.newWorkTimeCode(recordInfo == null ? null : recordInfo.getWorkTimeCode())
				.build().toBePublished();
	}

	/**
	 * 勤務予定の勤務情報と勤務実績の勤務情報が同じかどうか確認する
	 * @param workNo
	 * @param predetermineTimeSheetSetting
	 * @return
	 */
	public boolean isMatchWorkInfomation() {			
		if(this.scheduleInfo.getWorkTypeCode()==this.recordInfo.getWorkTypeCode()&&
				this.scheduleInfo.getWorkTimeCode()==this.recordInfo.getWorkTimeCode()) {
			return true;
		}
		return false;
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

	public WorkInfoOfDailyPerformance(String employeeId, WorkInformation recordWorkInformation,
			WorkInformation scheduleWorkInformation, CalculationState calculationState, NotUseAttribute goStraightAtr,
			NotUseAttribute backStraightAtr, GeneralDate ymd, DayOfWeek dayOfWeek,
			List<ScheduleTimeSheet> scheduleTimeSheets) {
		super();
		this.employeeId = employeeId;
		this.recordInfo = recordWorkInformation;
		this.scheduleInfo = scheduleWorkInformation;
		this.calculationState = calculationState;
		this.goStraightAtr = goStraightAtr;
		this.backStraightAtr = backStraightAtr;
		this.ymd = ymd;
		this.dayOfWeek = dayOfWeek;
		this.scheduleTimeSheets = scheduleTimeSheets;
	}
	
	/**
	 * 計算ステータスの変更
	 * @param state 計算ステータス
	 */
	public void changeCalcState(CalculationState state) {
		this.calculationState = state;
	}
}
