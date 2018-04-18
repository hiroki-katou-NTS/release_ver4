package nts.uk.ctx.at.record.dom.worktime;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import nts.arc.layer.dom.event.DomainEvent;
import nts.arc.time.GeneralDate;

@Builder
@Getter
public class TimeLeaveChangeEvent extends DomainEvent {

	/** 年月日: 年月日 */
	private String employeeId;

	/** 社員ID: 社員ID */
	private GeneralDate targetDate;

	/** 変更された出退勤: 出退勤 */
	private List<TimeLeavingWork> timeLeave;

}
