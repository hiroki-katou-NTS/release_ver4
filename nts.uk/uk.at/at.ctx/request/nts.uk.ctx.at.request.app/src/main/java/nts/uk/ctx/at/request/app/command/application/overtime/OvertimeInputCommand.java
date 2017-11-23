package nts.uk.ctx.at.request.app.command.application.overtime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeInputCommand {
	/**
	 * 会社ID
	 * companyID
	 */
	private String companyID;
	/**
	 * 申請ID
	 */
	private String appID;
	/**
	 * 勤怠種類
	 */
	private int attendanceID;
	/**
	 * 勤怠項目NO
	 */
	private int frameNo;
	/**
	 * timeItemTypeAtr
	 */
	private int timeItemTypeAtr;
	
	/**
	 * 勤怠項目Name
	 */
	private String attendanceName;
	
	/**
	 * 開始時間
	 */
	private int startTime;
	/**
	 * 完了時間
	 */
	private int endTime;
	/**
	 * 申請時間
	 */
	private int applicationTime;
	
	public OverTimeInput convertToDomain(){
		return OverTimeInput.createSimpleFromJavaType(
				this.companyID, 
				this.appID, 
				this.attendanceID, 
				this.frameNo, 
				this.startTime, 
				this.endTime, 
				this.applicationTime, 
				this.timeItemTypeAtr);
	}
}
