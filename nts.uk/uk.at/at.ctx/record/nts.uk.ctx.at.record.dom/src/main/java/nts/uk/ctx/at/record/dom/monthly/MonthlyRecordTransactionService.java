package nts.uk.ctx.at.record.dom.monthly;

import nts.arc.time.YearMonth;

public interface MonthlyRecordTransactionService {

	void updated(String employeeId, YearMonth yearMonth, int closureId, int closureDate, boolean lastOfMonth);

	void updated(String employeeId, YearMonth yearMonth, int closureId, int closureDate, boolean lastOfMonth, long version);
}
