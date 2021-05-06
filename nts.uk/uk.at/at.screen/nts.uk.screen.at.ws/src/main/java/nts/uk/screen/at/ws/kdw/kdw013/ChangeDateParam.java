package nts.uk.screen.at.ws.kdw.kdw013;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.app.find.monthly.root.common.DatePeriodDto;

/**
 * 
 * @author tutt
 *
 */
@Getter
public class ChangeDateParam {

	// 社員ID
	private String employeeId;

	// 基準日
	private GeneralDate refDate;

	// 表示期間
	private DatePeriodDto displayPeriod;
	
}
