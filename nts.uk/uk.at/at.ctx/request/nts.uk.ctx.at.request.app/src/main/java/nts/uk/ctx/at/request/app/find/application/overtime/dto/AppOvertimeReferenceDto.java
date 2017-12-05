package nts.uk.ctx.at.request.app.find.application.overtime.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.dom.application.overtime.service.SiftType;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkTypeOvertime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppOvertimeReferenceDto {
	/**
	 * appDate
	 */
	private String appDateRefer;

	/**
	 * 残業申請時間設定
	 */
	private List<OvertimeInputDto> overTimeInputsRefer;

	/**
	 * workType
	 */
	private WorkTypeOvertime workTypeRefer;
	
	/**
	 * siftType
	 */
	private SiftType siftTypeRefer;

	/**
	 * 勤務時間From1
	 */
	private Integer workClockFrom1Refer;
	/**
	 * 勤務時間To1
	 */
	private Integer workClockTo1Refer;
	/**
	 * 勤務時間From2
	 */
	private Integer workClockFrom2Refer;
	/**
	 * 勤務時間To2
	 */
	private Integer workClockTo2Refer;
	
	/**
	 * フレックス超過時間
	 */
	private Integer flexExessTimeRefer;
	/**
	 * 就業時間外深夜時間
	 */
	private Integer overTimeShiftNightRefer;

}
