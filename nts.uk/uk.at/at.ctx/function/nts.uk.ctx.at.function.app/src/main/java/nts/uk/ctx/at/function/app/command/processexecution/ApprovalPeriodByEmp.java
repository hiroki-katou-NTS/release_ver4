package nts.uk.ctx.at.function.app.command.processexecution;
/**
 * 承認結果の反映対象期間
 * @author tutk
 *
 */

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalPeriodByEmp {
	/** 社員 */
	private String employeeID;
	/** 期間 */
	private List<DatePeriod> listPeriod = new ArrayList<>();
}
