package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReasonNotReflectRecord;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReflectedStateRecord;
@AllArgsConstructor
@Getter
@Setter
public class OvertimeAppParameter {
	/**
	 * 反映状態
	 */
	private ReflectedStateRecord reflectedState;
	/**
	 * 反映不可理由
	 */
	private ReasonNotReflectRecord reasonNotReflect;
	/**
	 * 勤務種類コード
	 */
	private String workTypeCode;
			
	/**就業時間帯コード	 */
	private String workTimeCode;
	/**
	 * 開始時刻１
	 */
	private Integer startTime1;
	/**
	 * 終了時刻１
	 */
	private Integer endTime1;
	/**
	 * 開始時刻２
	 */
	private Integer startTime2;
	/**
	 * 終了時刻２
	 */
	private Integer endTime2;
	/**
	 * 残業申請．残業時間（10枠）
	 */
	private Map<Integer, Integer> mapOvertimeFrame;
	
	/**
	 * 就業時間外深夜時間
	 */
	private Integer overTimeShiftNight;
	/**
	 * フレックス超過時間
	 */
	private Integer flexExessTime;

}
