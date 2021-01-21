package nts.uk.query.pub.ccg005.work.information.dto;

import lombok.Builder;
import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
@Builder
public class WorkScheduleExport {
	// 社員ID
	private String sid;

	// 年月日
	private GeneralDate ymd;

	// 直帰区分
	private Integer backStraightAtr;

	// 直行区分
	private Integer goStraightAtr;

	// 出勤時刻
	private Integer attendanceTime;

	// 退勤時刻
	private Integer leaveTime;
}
