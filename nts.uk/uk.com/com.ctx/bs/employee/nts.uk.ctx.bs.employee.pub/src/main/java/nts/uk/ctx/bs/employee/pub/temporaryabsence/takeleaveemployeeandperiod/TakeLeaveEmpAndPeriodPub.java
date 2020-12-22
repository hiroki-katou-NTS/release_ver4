package nts.uk.ctx.bs.employee.pub.temporaryabsence.takeleaveemployeeandperiod;

import java.util.List;

import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface TakeLeaveEmpAndPeriodPub {
	 public List<TakeLeaveEmpAndPeriodExport> takeLeaveEmpAndPeriodPub(List<String> listEmp,DatePeriod period);
}
