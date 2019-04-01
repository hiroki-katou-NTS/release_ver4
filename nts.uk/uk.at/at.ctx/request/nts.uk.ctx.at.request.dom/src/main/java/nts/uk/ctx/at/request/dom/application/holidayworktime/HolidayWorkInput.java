package nts.uk.ctx.at.request.dom.application.holidayworktime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.request.dom.application.holidayworktime.primitivevalue.HolidayAppPrimitiveTime;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.primitivevalue.OvertimeAppPrimitiveTime;

/**
 * @author loivt
 * 休日出勤申請時間設定
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayWorkInput {

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
	private AttendanceType attendanceType;
	/**
	 * 勤怠項目NO
	 */
	private int frameNo;
	/**
	 * 開始時間
	 */
	private HolidayAppPrimitiveTime startTime;
	/**
	 * 完了時間
	 */
	private HolidayAppPrimitiveTime endTime;
	/**
	 * 申請時間
	 */
	private OvertimeAppPrimitiveTime applicationTime;
	
	
	public static HolidayWorkInput createSimpleFromJavaType(String companyID, String appID, int attendanceID, int frameNo, Integer startTime, Integer endTime, Integer applicationTime){
		return new HolidayWorkInput(companyID,
				appID,
				EnumAdaptor.valueOf(attendanceID, AttendanceType.class),
				frameNo,
				startTime == null ? null :new HolidayAppPrimitiveTime(startTime),
				endTime == null ? null : new HolidayAppPrimitiveTime(endTime),
				applicationTime == null ? null : new OvertimeAppPrimitiveTime(applicationTime));
	}
	
	public Integer getApptime(){
		return applicationTime == null ? null : applicationTime.v();
	}

}
