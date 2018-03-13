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
public class PreAppOvertimeDto {
	/**
	 * appDate
	 */
	private String appDatePre;

	/**
	 * 残業申請時間設定
	 */
	private List<OvertimeInputDto> overTimeInputsPre;

	/**
	 * workType
	 */
	private WorkTypeOvertime workTypePre;
	
	/**
	 * siftType
	 */
	private SiftType siftTypePre;
	
	private String workClockFromTo1Pre;
	
	private String workClockFromTo2Pre;
	
	/**
	 * フレックス超過時間
	 */
	private Integer flexExessTimePre;
	/**
	 * 就業時間外深夜時間
	 */
	private Integer overTimeShiftNightPre;

}
