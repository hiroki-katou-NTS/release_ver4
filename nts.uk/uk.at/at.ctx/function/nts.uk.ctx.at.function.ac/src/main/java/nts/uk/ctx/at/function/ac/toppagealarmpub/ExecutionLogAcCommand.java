package nts.uk.ctx.at.function.ac.toppagealarmpub;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.ExecutionLogAdapterFn;
import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.ExecutionLogErrorDetailFn;
import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.ExecutionLogImportFn;
import nts.uk.ctx.sys.shared.pub.toppagealarmpub.ExecutionLogErrorDetail;
import nts.uk.ctx.sys.shared.pub.toppagealarmpub.ExecutionLogImport;
import nts.uk.ctx.sys.shared.pub.toppagealarmpub.ExecutionLogPub;

@Stateless
public class ExecutionLogAcCommand implements ExecutionLogAdapterFn{
	
	@Inject
	private ExecutionLogPub executionLogPub;
	
	@Override
	public void updateExecuteLog(ExecutionLogImportFn param) {
		executionLogPub.updateExecuteLog(convertToExecutionLogImportFn(param));
	}
	
	private ExecutionLogImport convertToExecutionLogImportFn(ExecutionLogImportFn importFn) {
		return new ExecutionLogImport(
				importFn.getCompanyId(),
				importFn.getExistenceError(),
				importFn.getExecutionContent().value,
				importFn.getFinishDateTime(),
				importFn.getManagerId(),
				importFn.getIsCancelled() == null ? 0 : 
				(!importFn.getIsCancelled().isPresent()?null:importFn.getIsCancelled().get()),
				importFn.getTargerEmployee()==null? new ArrayList<>() : importFn.getTargerEmployee().stream().map(c->convertToExecutionLogErrorDetailFn(c)).collect(Collectors.toList())
				);
	}
	
	private ExecutionLogErrorDetail convertToExecutionLogErrorDetailFn(ExecutionLogErrorDetailFn importFn) {
		return new ExecutionLogErrorDetail(
				importFn.getErrorMessage(),
				importFn.getTargerEmployee()
				);
				
	}
	
	
	
}
