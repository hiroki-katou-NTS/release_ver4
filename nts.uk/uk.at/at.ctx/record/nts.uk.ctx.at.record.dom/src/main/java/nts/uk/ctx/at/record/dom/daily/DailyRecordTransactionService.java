package nts.uk.ctx.at.record.dom.daily;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface DailyRecordTransactionService {

	void updated(String empId, GeneralDate workingDate);

	void updated(String empId, GeneralDate workingDate, long version);

	void updated(List<String> empId, List<GeneralDate> workingDate);
}
