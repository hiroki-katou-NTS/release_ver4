package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class BreakTime {
	/**
	 * 開始時刻
	 */
	private Integer startTime;
	/**
	 * 終了時刻
	 */
	private Integer endTime;
}
