package nts.uk.ctx.at.record.dom.daily;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Value;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.worktime.CommomSetting.PredetermineTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.CommomSetting.TimeSheetWithUseAtr;

/**
 * 日別実績の勤務情報
 * @author keisuke_hoshina
 *
 */
@Value
public class WorkInformationOfDaily {
	private String syainID;
	private List<ScheduleTimeSheet> workScheduleTimeSheet;
	private WorkInformation recordWorkInformation;
	private WorkInformation scheduleWorkInformation;
	

	/**
	 * 勤務予定の勤務情報と勤務実績の勤務情報が同じかどうか確認する
	 * @param workNo
	 * @param predetermineTimeSheetSetting
	 * @return
	 */
	public boolean isMatchWorkInfomation() {			
		if(this.scheduleWorkInformation.getWorkTypeCode()==this.recordWorkInformation.getWorkTypeCode()&&
				this.scheduleWorkInformation.getSiftCode()==this.recordWorkInformation.getSiftCode()) {
			return true;
		}
		return false;
	}

	
	/**
	 * 指定された勤務回数の予定時間帯を取得する
	 * @param workNo
	 * @return　予定時間帯
	 */
	public ScheduleTimeSheet getScheduleTimeSheet(int workNo) {
		List<ScheduleTimeSheet> scheduleTimeSheetList = this.workScheduleTimeSheet.stream()
				.filter(ts -> ts.getWorkNo() == workNo).collect(Collectors.toList());
		if(scheduleTimeSheetList.size()>1) {
			throw new RuntimeException("Exist duplicate workNo : " + workNo);
		}	
		return scheduleTimeSheetList.get(0);	
	}
	
}
