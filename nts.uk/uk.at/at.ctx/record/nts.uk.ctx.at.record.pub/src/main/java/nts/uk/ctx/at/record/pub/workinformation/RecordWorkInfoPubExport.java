package nts.uk.ctx.at.record.pub.workinformation;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 
 * @author nampt
 * 
 */
@Data
public class RecordWorkInfoPubExport {

	// 勤務種類コード
	private String workTypeCode;
	
	// 就業時間帯コード
	private String workTimeCode;
	
	// 日別実績の出退勤．出退勤．出勤．打刻 , 勤務NO＝0
	private Integer attendanceStampTimeFirst;
	
	// 日別実績の出退勤．出退勤．退勤．打刻, 勤務NO＝0
	private Integer leaveStampTimeFirst;
	
	// 日別実績の出退勤．出退勤．出勤．打刻 , 勤務NO＝1
	private Integer attendanceStampTimeSecond;
	
	// 日別実績の出退勤．出退勤．退勤．打刻, 勤務NO＝1
	private Integer leaveStampTimeSecond;
	
	/** 日別実績の勤怠時間．実働時間．総労働時間．遅刻時間．遅刻時間 */
	private Integer lateTime1;
	
	/** 日別実績の勤怠時間．実働時間．総労働時間．早退時間．早退時間 */
	private Integer leaveEarlyTime1;
	
	/** 日別実績の勤怠時間．実働時間．総労働時間．遅刻時間．遅刻時間 */
	private Integer lateTime2;
	
	/** 日別実績の勤怠時間．実働時間．総労働時間．早退時間．早退時間 */
	private Integer leaveEarlyTime2;

	/** 日別実績の勤怠時間．実働時間．総労働時間．短時間勤務時間.育児時間 */
	private Integer childCareTime;
	
	/** 日別実績の勤怠時間．実働時間．総労働時間.外出時間.計上用合計時間.合計時間.外出時間 私用 */
	private Integer outingTimePrivate;
	
	/** 日別実績の勤怠時間．実働時間．総労働時間.外出時間.計上用合計時間.合計時間.外出時間 組合 */
	private Integer outingTimeCombine;

	/** 日別実績の勤怠時間．実績時間．総労働時間．所定外時間．残業時間．フレックス時間．フレックス時間.計算フレックス */
	private Integer flexTime;
	
	/** 日別実績の勤怠時間．実績時間．総労働時間．所定外時間．残業時間．残業枠時間．残業時間.計算残業 */
	/** 日別実績の勤怠時間．実績時間．総労働時間．所定外時間．残業時間．残業枠時間．振替時間.計算振替残業 */
	private List<CommonTimeSheet> overtimes = new ArrayList<>();
	
//	private List<CommonTimeSheet> otTranferTimes;
	
	/** 日別実績の勤怠時間．実績時間．総労働時間．所定外時間．休出時間．休出枠時間．休出時間.計算休日出勤 */
	/** 日別実績の勤怠時間．実績時間．総労働時間．所定外時間．休出時間．休出枠時間．振替時間.計算振替 */
	private List<CommonTimeSheet> holidayWorks = new ArrayList<>();
	
//	private List<CommonTimeSheet> holWorkTranferTimes;
	
	/** 計算就業外深夜 */
	/** 休出深夜 */
	
	public RecordWorkInfoPubExport(String workTypeCode, String workTimeCode, Integer attendanceStampTimeFirst,
			Integer leaveStampTimeFirst, Integer attendanceStampTimeSecond, Integer leaveStampTimeSecond, Integer time1, Integer time2,
			Integer time3, Integer time4, Integer time5) {
		super();
		this.workTypeCode = workTypeCode;
		this.workTimeCode = workTimeCode;
		this.attendanceStampTimeFirst = attendanceStampTimeFirst;
		this.leaveStampTimeFirst = leaveStampTimeFirst;
		this.attendanceStampTimeSecond = attendanceStampTimeSecond;
		this.leaveStampTimeSecond = leaveStampTimeSecond;
		this.lateTime1 = time1;
		this.leaveEarlyTime1 = time2;
		this.lateTime2 = time3;
		this.leaveEarlyTime2 = time4;
		this.childCareTime = time5;
	}
	
	public RecordWorkInfoPubExport(String workTypeCode, String workTimeCode) {
		super();
		this.workTypeCode = workTypeCode;
		this.workTimeCode = workTimeCode;
	}
	
}
