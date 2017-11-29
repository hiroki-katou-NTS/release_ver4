package nts.uk.screen.at.app.schedule.basicschedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkEmpCombineDto {
	/**
	 * 勤務就業コード
	 */
	private String code;
	/**
	 * 会社ID
	 */
	private String companyId;
	/**
	 * 勤務種類コード
	 */
	private String workTypeCode;
	/**
	 * 就業時間帯コード
	 */
	private String workTimeCode;
	/**
	 * 勤務就業記号
	 */
	private String symbolName;
}
