package nts.uk.ctx.at.request.ac.record;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.pub.dailymonthlyprocessing.ExeStateOfCalAndSumExport;
import nts.uk.ctx.at.record.pub.dailymonthlyprocessing.ExecutionLogExportPub;
import nts.uk.ctx.at.record.pub.dailymonthlyprocessing.SetInforReflAprResultExport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExeStateOfCalAndSumImport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionContentImport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionLogRequestImport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionTypeExImport;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.SetInforReflAprResultImport;

@Stateless
public class ExecutionLogRequestImportImpl implements ExecutionLogRequestImport {
	
	@Inject
	private ExecutionLogExportPub execuLog;	
	@Override
	public Optional<SetInforReflAprResultImport> optReflectResult(String empCalAndSumExecLogID, int executionContent) {
		Optional<SetInforReflAprResultExport> optExport = execuLog.optReflectResult(empCalAndSumExecLogID, executionContent);
		if(!optExport.isPresent()) {
			return Optional.empty();
		}
		SetInforReflAprResultExport exportData = optExport.get();
		SetInforReflAprResultImport outputData = new SetInforReflAprResultImport(EnumAdaptor.valueOf(exportData.getExecutionContent().value, 
					ExecutionContentImport.class),
				EnumAdaptor.valueOf(exportData.getExecutionType().value, ExecutionTypeExImport.class),
				exportData.getCalExecutionSetInfoID(),
				exportData.isForciblyReflect());
		return Optional.of(outputData);
	}
	@Override
	public Optional<ExeStateOfCalAndSumImport> executionStatus(String empCalAndSumExecLogID) {
		Optional<ExeStateOfCalAndSumExport> executionStatus = execuLog.executionStatus(empCalAndSumExecLogID);
		if(!executionStatus.isPresent()) {
			return Optional.empty();
		}
		ExeStateOfCalAndSumImport output = EnumAdaptor.valueOf(executionStatus.get().value, ExeStateOfCalAndSumImport.class);
		return Optional.of(output);
	}
	@Override
	public void updateLogInfo(String employeeID, String empCalAndSumExecLogId, int executionContent, int state) {
		execuLog.updateLogInfo(employeeID, empCalAndSumExecLogId, executionContent, state);
	}
	@Override
	public void updateLogInfo(String empCalAndSumExecLogId, int executionContent, int state) {
		execuLog.updateLogInfo(empCalAndSumExecLogId, executionContent, state);
	}
	@Override
	public Boolean isCalWhenLock(String empCalAndSumExecLogId,int executionContent) {
		return execuLog.isCalWhenLock(empCalAndSumExecLogId, executionContent);
	}

	
}
