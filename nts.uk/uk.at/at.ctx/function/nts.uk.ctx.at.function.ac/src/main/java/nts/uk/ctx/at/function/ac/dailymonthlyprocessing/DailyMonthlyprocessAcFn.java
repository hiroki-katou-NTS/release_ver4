package nts.uk.ctx.at.function.ac.dailymonthlyprocessing;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.function.dom.adapter.dailymonthlyprocessing.DailyMonthlyprocessAdapterFn;
import nts.uk.ctx.at.function.dom.adapter.dailymonthlyprocessing.ExeStateOfCalAndSumImportFn;
import nts.uk.ctx.at.record.pub.dailymonthlyprocessing.ExeStateOfCalAndSumExport;
import nts.uk.ctx.at.record.pub.dailymonthlyprocessing.ExecutionLogExportPub;

@Stateless
public class DailyMonthlyprocessAcFn implements DailyMonthlyprocessAdapterFn {

	@Inject
	private ExecutionLogExportPub executionLogExportPub;
	@Override
	public Optional<ExeStateOfCalAndSumImportFn> executionStatus(String empCalAndSumExecLogID) {
		Optional<ExeStateOfCalAndSumExport> exportData =  executionLogExportPub.executionStatus(empCalAndSumExecLogID);
		if(exportData.isPresent()) {
			return Optional.of(EnumAdaptor.valueOf(exportData.get().value, ExeStateOfCalAndSumImportFn.class));
		}
		return Optional.empty();
	}

}
