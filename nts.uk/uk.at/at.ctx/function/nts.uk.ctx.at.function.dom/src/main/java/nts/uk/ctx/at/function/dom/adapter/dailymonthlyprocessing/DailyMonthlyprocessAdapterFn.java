package nts.uk.ctx.at.function.dom.adapter.dailymonthlyprocessing;

import java.util.Optional;

public interface DailyMonthlyprocessAdapterFn {
	Optional<ExeStateOfCalAndSumImportFn> executionStatus(String empCalAndSumExecLogID);
}
