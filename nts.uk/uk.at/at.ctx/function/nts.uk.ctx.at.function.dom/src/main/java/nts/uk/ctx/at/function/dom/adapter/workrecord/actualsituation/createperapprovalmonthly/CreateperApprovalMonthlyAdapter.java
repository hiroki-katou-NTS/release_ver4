package nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovalmonthly;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface CreateperApprovalMonthlyAdapter {
	public boolean createperApprovalMonthly(String companyId,String executionId,List<String> employeeIDs,int processExecType,GeneralDate startDateClosure);
}
