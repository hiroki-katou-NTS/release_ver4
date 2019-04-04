package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflectDaily_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
@AllArgsConstructor
@Getter
@Setter
public class HolidayWorktimeAppRequestPara {
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
	private ReflectedState_New reflectedState;
	/**
	 * 反映不可理由
	 */
	private ReasonNotReflectDaily_New reasonNotReflect;
	private Integer startTime;
	private Integer endTime;
	/**
	 * 休日出勤申請・休憩時間（１０枠）
	 */
	private Map<Integer, BreakTime> mapBreakTimeFrame;
}

