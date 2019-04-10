package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 休日出勤申請parameter
 * @author do_dt
 *
 */
@AllArgsConstructor
@Getter
@Setter
public class HolidayWorktimeAppPara {
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
	 * 
	 */
	private Integer startTime;
	
	private Integer endTime;
	/**
	 * 休日出勤申請・休憩時間（１０枠）
	 */
	private Map<Integer, BreakTimeParam> mapBreakTimeFrame;
}
