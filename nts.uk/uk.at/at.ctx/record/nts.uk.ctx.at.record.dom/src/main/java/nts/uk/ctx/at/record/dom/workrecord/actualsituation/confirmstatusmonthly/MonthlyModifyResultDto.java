package nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyModifyResultDto {

	/** Attendance items*/
	private List<ItemValue> items;
	
	/** 年月: 年月 */
	private Integer yearMonth;

	/** 社員ID: 社員ID */
	private String employeeId;

	/** 締めID: 締めID */
	private int closureId;
	
	private ClosureDate closureDate;
	
	/** 月別実績の勤怠時間 期間: 期間  */
	private DatePeriod workDatePeriod;
}
