package nts.uk.ctx.at.schedule.dom.appreflectprocess.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
@AllArgsConstructor
@Setter
@Getter
public class CommonReflectParamSche {
	/**	社員ID */
	private String employeeId;
	/**	年月日 */
	private GeneralDate appDate;
	/**
	 * 勤務種類コード
	 */
	private String worktypeCode;
	/**
	 * 就業時間帯コード
	 */
	private String workTimeCode;
	
	private Integer startTime;
	
	private Integer endTime;

}
