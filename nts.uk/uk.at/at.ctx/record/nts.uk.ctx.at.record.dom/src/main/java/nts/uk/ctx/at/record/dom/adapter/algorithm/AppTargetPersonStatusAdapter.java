package nts.uk.ctx.at.record.dom.adapter.algorithm;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.checkconfirm.StateConfirm;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface AppTargetPersonStatusAdapter {
	public List<StateConfirm> appTargetPersonStatus(String employeeID, GeneralDate startDate, GeneralDate endDate,
			int routeType);

	public List<StateConfirm> appTargetPersonStatus(String employeeID, DatePeriod date, Integer routeType);

	public List<StateConfirm> appTargetPersonStatus(List<String> employeeID, DatePeriod date, Integer routeType);
}
