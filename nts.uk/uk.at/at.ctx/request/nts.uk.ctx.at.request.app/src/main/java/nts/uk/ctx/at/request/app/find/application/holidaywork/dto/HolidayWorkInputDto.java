package nts.uk.ctx.at.request.app.find.application.holidaywork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInput;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayWorkInputDto {
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
	private int attendanceType;
	/**
	 * 勤怠項目NO
	 */
	private int frameNo;
	
	/**
	 * 勤怠項目Name
	 */
	private String frameName;
	
	/**
	 * 開始時間
	 */
	private Integer startTime;
	/**
	 * 完了時間
	 */
	private Integer endTime;
	/**
	 * 申請時間
	 */
	private Integer applicationTime;
	
	/**
	 * errorCode
	 */
	private int errorCode;
	
	public static HolidayWorkInputDto fromDomain(HolidayWorkInput holidayWorkInput){
		return new HolidayWorkInputDto(
				holidayWorkInput.getCompanyID(), 
				holidayWorkInput.getAppID(), 
				holidayWorkInput.getAttendanceType().value, 
				holidayWorkInput.getFrameNo(),
				"", 
				holidayWorkInput.getStartTime()== null ? null : holidayWorkInput.getStartTime().v(), 
				holidayWorkInput.getEndTime() == null ? null : holidayWorkInput.getEndTime().v(), 
				holidayWorkInput.getApplicationTime() == null ? null : holidayWorkInput.getApplicationTime().v(),0);
	}
}
