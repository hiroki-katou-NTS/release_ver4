package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BreakTimeParam {
	/**
	 * 開始時刻
	 */
	private Integer startTime;
	/**
	 * 終了時刻
	 */
	private Integer endTime;
}
