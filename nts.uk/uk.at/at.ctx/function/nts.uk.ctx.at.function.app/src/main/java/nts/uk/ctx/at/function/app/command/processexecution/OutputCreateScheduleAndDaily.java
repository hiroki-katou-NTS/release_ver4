package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OutputCreateScheduleAndDaily {
	private boolean checkStop;

	private List<ApprovalPeriodByEmp> listApprovalPeriodByEmp = new ArrayList<>();

	public OutputCreateScheduleAndDaily(boolean checkStop, List<ApprovalPeriodByEmp> listApprovalPeriodByEmp) {
		super();
		this.checkStop = checkStop;
		this.listApprovalPeriodByEmp = listApprovalPeriodByEmp;
	}

}
