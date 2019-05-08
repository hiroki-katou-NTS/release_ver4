package nts.uk.ctx.at.schedule.dom.adapter.dailymonthlyprocessing;

import java.util.Optional;


public interface DailyMonthlyprocessAdapterSch {
	Optional<ExeStateOfCalAndSumImportSch> executionStatus(String empCalAndSumExecLogID);
}
