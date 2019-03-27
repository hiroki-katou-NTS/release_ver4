package nts.uk.ctx.at.record.app.command.monthlyclosureupdate;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;

/**
 * 
 * @author HungTT
 *
 */
@AllArgsConstructor
@Data
public class MonthlyClosureResponse {
	
	private String monthlyClosureUpdateLogId;
	private List<String> listEmployeeId;
	private Integer closureId;
	private GeneralDateTime startDT;
	private GeneralDateTime endDT;
	private Integer currentMonth;
	private Integer closureDay;
	private Boolean isLastDayOfMonth;
	private GeneralDate periodStart;
	private GeneralDate periodEnd;
	private Integer check;
	private Integer executionStatus;
	private GeneralDateTime currentDT;
}
