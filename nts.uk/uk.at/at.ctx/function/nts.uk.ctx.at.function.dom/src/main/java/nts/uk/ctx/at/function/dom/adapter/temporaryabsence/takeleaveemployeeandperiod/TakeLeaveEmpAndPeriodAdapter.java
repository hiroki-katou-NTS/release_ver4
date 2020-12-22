package nts.uk.ctx.at.function.dom.adapter.temporaryabsence.takeleaveemployeeandperiod;

import java.util.List;

import nts.uk.ctx.at.shared.dom.dailyresult.findperiodchangeleavehis.TakeLeaveEmpAndPeriodShared;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface TakeLeaveEmpAndPeriodAdapter {
	 public List<TakeLeaveEmpAndPeriodShared> takeLeaveEmpAndPeriodPub(List<String> listEmp,DatePeriod period);
}
