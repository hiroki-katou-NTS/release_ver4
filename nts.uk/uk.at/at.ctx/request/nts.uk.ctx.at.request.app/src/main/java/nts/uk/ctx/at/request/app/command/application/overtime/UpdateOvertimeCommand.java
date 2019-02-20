package nts.uk.ctx.at.request.app.command.application.overtime;

import java.util.List;

import lombok.Value;
import nts.arc.time.GeneralDate;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Value
public class UpdateOvertimeCommand {
		
	private Long version;
	
	/**
	 * 申請
	 */
	/**
	 * 申請.申請ID
	 */
	private String appID;
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
	 * 残業申請.就業時間帯コード
	 */
	private String siftTypeCode;
	/**
	 * 残業申請.勤務開始時刻1
	 */
	private Integer workClockFrom1;
	/**
	 * 残業申請.勤務終了時刻1
	 */
	private Integer workClockTo1;
	/**
	 * 残業申請.勤務開始時刻2
	 */
	private Integer workClockFrom2;
	/**
	 * 残業申請.勤務終了時刻2
	 */
	private Integer workClockTo2;
	/**
	 * 休出時間
	 * ATTENDANCE_ID = 0
	 */
	private List<OvertimeInputCommand> breakTimes;
	
	/**
	 *  残業時間
	 *  ATTENDANCE_ID = 1
	 */
	private List<OvertimeInputCommand> overtimeHours;
	/**
	 * 加給時間
	 * ATTENDANCE_ID = 2
	 */
	private List<OvertimeInputCommand> restTime;
	/**
	 * 加給時間
	 * ATTENDANCE_ID = 3
	 */
	private List<OvertimeInputCommand> bonusTimes;
	/**
	 * 残業申請.残業区分
	 */
	private int overtimeAtr;

	/**
	 * 残業申請.就業時間外深夜時間
	 */
	private Integer overTimeShiftNight;

	/**
	 * 残業申請.ﾌﾚｯｸｽ超過時間
	 */
	private Integer flexExessTime;

	/**
	 * 残業申請.乖離理由 = 残業申請.乖離定型理由 + \n + 残業申請.乖離理由
	 */
	private String divergenceReasonContent;
	
	private String companyID;

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
	 * 登録時にメールを送信する
	 */
	private boolean sendMail;
	/**
	 * 計算フラグ
	 */
	private int calculateFlag;
	/**
	 * 時間外時間の詳細
	 */
	private AppOvertimeDetailCommand appOvertimeDetail;
	
	private String appReasonID;
}
