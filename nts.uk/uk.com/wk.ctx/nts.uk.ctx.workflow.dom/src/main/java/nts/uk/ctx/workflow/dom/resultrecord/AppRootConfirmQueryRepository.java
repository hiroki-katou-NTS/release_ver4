package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.List;

import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.closure.ClosureMonth;

public interface AppRootConfirmQueryRepository {

	AppRootIntermForQuery.List queryIntermDaily(List<String> employeeIDLst, DatePeriod period);
	
	AppRootIntermForQuery.List queryIntermMonthly(List<String> employeeIDLst, DatePeriod period);

	AppRootRecordConfirmForQuery.List queryConfirmDaily(List<String> employeeIds, DatePeriod period);
	
	AppRootRecordConfirmForQuery.List queryConfirmMonthly(List<String> employeeIds, ClosureMonth closureMonth);
}
