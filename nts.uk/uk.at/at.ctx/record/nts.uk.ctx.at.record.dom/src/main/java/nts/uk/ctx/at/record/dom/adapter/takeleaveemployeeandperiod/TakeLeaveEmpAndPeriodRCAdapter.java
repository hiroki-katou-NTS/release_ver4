package nts.uk.ctx.at.record.dom.adapter.takeleaveemployeeandperiod;

import java.util.List;

import nts.uk.ctx.at.shared.dom.dailyresult.findperiodchangeleavehis.TakeLeaveEmpAndPeriodShared;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface TakeLeaveEmpAndPeriodRCAdapter {
	 public List<TakeLeaveEmpAndPeriodShared> takeLeaveEmpAndPeriodPub(List<String> listEmp,DatePeriod period);
}
