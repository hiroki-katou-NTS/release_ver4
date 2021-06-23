package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.ScheduleRecordClassifi;

/**
 * @author ThanhNX
 *
 *         日別勤怠の何が変更されたか
 */
@Data
@AllArgsConstructor
public class ChangeDailyAttendance {

	/** 勤務情報 */
	public boolean workInfo;

	/** 出退勤 */
	public boolean attendance;

	/** 計算区分 */
	public boolean calcCategory;
	
	/** 勤務予定から移送した値も補正する */
	public boolean correctValCopyFromSche;
	
	/** 予定実績区分*/
	public ScheduleRecordClassifi classification;
	
	/** 直行直帰区分*/
	public boolean directBounceClassifi; 

	/** 始業、終業時刻を補正しない */
	public boolean noCorrectStartEndWork;
}
