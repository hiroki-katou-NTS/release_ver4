package nts.uk.ctx.at.request.app.command.application.holidaywork;

import java.util.List;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.command.application.overtime.AppOvertimeDetailCommand;
@Getter
public class CreateHolidayWorkCommand {

	/**
	 * 申請.会社ID
	 */
	private String companyID;

	/**
	 * 申請.申請ID
	 */
	private String appID;

	/**
	 * 申請.申請日
	 */
	private GeneralDate applicationDate;

	/**
	 * 申請.事前事後区分
	 */
	private int prePostAtr;

	/**
	 * 申請.申請者
	 */
	private String applicantSID;

	/**
	 * 申請.申請理由 = 申請.申請定型理由 + \n + 申請.申請理由
	 */
	private String applicationReason;

	/**
	 * 残業申請
	 */
	/**
	 * 残業申請.勤務種類コード
	 */
	private String workTypeCode;
	/**
	 * 残業申請.勤務種類コード
	 */
	//private String workTypeCodeName;
	/**
	 * 残業申請.就業時間帯コード
	 */
	private String siftTypeCode;
	/**
	 * 残業申請.就業時間帯コード
	 */
	//private String siftTypeCodeName;

	/**
	 * 残業申請.勤務開始時刻1
	 */
	private Integer workClockStart1;
	/**
	 * 残業申請.勤務終了時刻1
	 */
	private Integer workClockEnd1;
	/**
	 * goAtr1
	 */
	private Integer goAtr1;
	/**
	 * backAtr1
	 */
	private Integer backAtr1;
	/**
	 * 残業申請.勤務開始時刻2
	 */
	private Integer workClockStart2;
	/**
	 * 残業申請.勤務終了時刻2
	 */
	private Integer workClockEnd2;
	/**
	 * goAtr2
	 */
	private Integer goAtr2;
	/**
	 * backAtr2
	 */
	private Integer backAtr2;
	/**
	 * 休出時間
	 * ATTENDANCE_ID = 2
	 */
	private List<HolidayWorkInputCommand> breakTimes;
	
	/**
	 *  残業時間
	 *  ATTENDANCE_ID = 1
	 */
	private List<HolidayWorkInputCommand> overtimeHours;
	/**
	 * 加給時間
	 * ATTENDANCE_ID = 0
	 */
	private List<HolidayWorkInputCommand> restTime;
	/**
	 * 加給時間
	 * ATTENDANCE_ID = 3
	 */
	private List<HolidayWorkInputCommand> bonusTimes;

	/**
	 * 残業申請.就業時間外深夜時間
	 */
	private int overTimeShiftNight;

	/**
	 * 残業申請.乖離理由 = 残業申請.乖離定型理由 + \n + 残業申請.乖離理由
	 */
	private String divergenceReasonContent;
	/**
	 * 登録時にメールを送信する
	 */
	private boolean sendMail;
	/**
	 * 計算フラグ
	 */
	private int calculateFlag;
	
	/**
	 * 申請ＩＤ（元振休）
	 */
	private String leaveAppID;
	
	/**
	 * uiType
	 */
	private int uiType;
	/**
	 * 時間外時間の詳細
	 */
	private AppOvertimeDetailCommand appOvertimeDetail;
	
	private String appReasonID;
}
