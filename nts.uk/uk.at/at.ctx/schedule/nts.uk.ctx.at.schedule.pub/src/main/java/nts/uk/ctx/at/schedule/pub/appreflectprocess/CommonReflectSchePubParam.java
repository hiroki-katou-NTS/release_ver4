package nts.uk.ctx.at.schedule.pub.appreflectprocess;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@AllArgsConstructor
@Setter
@Getter
public class CommonReflectSchePubParam {
	/**	社員ID */
	private String employeeId;
	/**	年月日 */
	private GeneralDate datePara;
	/**
	 * 勤務種類コード
	 */
	private String worktypeCode;
	private String workTimeCode;
	private Integer startTime;
	private Integer endTime;
}
