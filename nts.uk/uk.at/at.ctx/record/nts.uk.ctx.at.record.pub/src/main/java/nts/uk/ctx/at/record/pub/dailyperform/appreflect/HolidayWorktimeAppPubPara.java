package nts.uk.ctx.at.record.pub.dailyperform.appreflect;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class HolidayWorktimeAppPubPara {
	/**
	 * 勤務種類
	 */
	private String workTypeCode;
	/**
	 * 就業時間帯
	 */
	private String workTimeCode;
	/**
	 * 休日出勤申請．休出時間（10枠）
	 */
	private Map<Integer, Integer> mapWorkTimeFrame;
	/**
	 * 外深夜時間
	 */
	private Integer nightTime;
	/**
	 * 反映状態
	 */
	private ReflectedStatePubRecord reflectedState;
	/**
	 * 反映不可理由
	 */
	private ReasonNotReflectDailyPubRecord reasonNotReflect;
	
	private Integer startTime;
	private Integer endTime;
	
	/**
	 * 休日出勤申請・休憩時間（１０枠）
	 */
	private Map<Integer, BreakTimePubParam> mapBreakTimeFrame;
}
