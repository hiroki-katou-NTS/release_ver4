package nts.uk.ctx.at.schedule.ac.dailymonthlyprocessing;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.pub.dailymonthlyprocessing.ExeStateOfCalAndSumExport;
import nts.uk.ctx.at.record.pub.dailymonthlyprocessing.ExecutionLogExportPub;
import nts.uk.ctx.at.schedule.dom.adapter.dailymonthlyprocessing.DailyMonthlyprocessAdapterSch;
import nts.uk.ctx.at.schedule.dom.adapter.dailymonthlyprocessing.ExeStateOfCalAndSumImportSch;
@Stateless
public class DailyMonthlyprocessAcSch implements DailyMonthlyprocessAdapterSch {

	@Inject
	private ExecutionLogExportPub executionLogExportPub;
	
	@Override
	public Optional<ExeStateOfCalAndSumImportSch> executionStatus(String empCalAndSumExecLogID) {
		Optional<ExeStateOfCalAndSumExport> exportData =  executionLogExportPub.executionStatus(empCalAndSumExecLogID);
		if(exportData.isPresent()) {
			return Optional.of(EnumAdaptor.valueOf(exportData.get().value, ExeStateOfCalAndSumImportSch.class));
		}
		return Optional.empty();
	}

}
