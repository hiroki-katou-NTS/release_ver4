package nts.uk.ctx.at.record.dom.workinformation;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.enums.NotUseAttribute;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkNo;

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

	private WorkInformation recordWorkInformation;

	private WorkInformation scheduleWorkInformation;

	private CalculationState calculationState;

	// 直行区分
	private NotUseAttribute goStraightAtr;

	// 直帰区分
	private NotUseAttribute backStraightAtr;

	private GeneralDate ymd;

	private List<ScheduleTimeSheet> scheduleTimeSheets;

	public WorkInfoOfDailyPerformance(String employeeId, WorkInformation recordWorkInformation,
			WorkInformation scheduleWorkInformation, CalculationState calculationState, NotUseAttribute goStraightAtr,
			NotUseAttribute backStraightAtr, GeneralDate ymd, List<ScheduleTimeSheet> scheduleTimeSheets) {
		this.employeeId = employeeId;
		this.recordWorkInformation = recordWorkInformation;
		this.scheduleWorkInformation = scheduleWorkInformation;
		this.calculationState = calculationState;
		this.goStraightAtr = goStraightAtr;
		this.backStraightAtr = backStraightAtr;
		this.ymd = ymd;
		this.scheduleTimeSheets = scheduleTimeSheets; 
	}
	
	/**
	 * 勤務予定を実績に移す
	 */
	public void shiftFromScheduleToRecord() {
		recordWorkInformation = scheduleWorkInformation;
	}
	
	/**
	 * 勤務予定の勤務情報と勤務実績の勤務情報が同じかどうか確認する
	 * @param workNo
	 * @param predetermineTimeSheetSetting
	 * @return
	 */
	public boolean isMatchWorkInfomation() {			
		if(this.scheduleWorkInformation.getWorkTypeCode()==this.recordWorkInformation.getWorkTypeCode()&&
				this.scheduleWorkInformation.getWorkTimeCode()==this.recordWorkInformation.getWorkTimeCode()) {
			return true;
		}
		return false;
	}

	/**
	 * 指定された勤務回数の予定時間帯を取得する
	 * @param workNo
	 * @return　予定時間帯
	 */
	public ScheduleTimeSheet getScheduleTimeSheet(WorkNo workNo) {
		List<ScheduleTimeSheet> scheduleTimeSheetList = this.scheduleTimeSheets.stream()
				.filter(ts -> ts.getWorkNo() == workNo).collect(Collectors.toList());
		if(scheduleTimeSheetList.size()>1) {
			throw new RuntimeException("Exist duplicate workNo : " + workNo);
		}	
		return scheduleTimeSheetList.get(0);	
	}
}
