package nts.uk.ctx.workflow.dom.resultrecord.service;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface CreateDailyApprover {
	
	/**
	 * 指定社員の中間データを作成する
	 * @param employeeID
	 * @param rootType
	 * @param recordDate
	 */
	public AppRootInstanceContent createDailyApprover(String employeeID, RecordRootType rootType, GeneralDate recordDate, GeneralDate closureStartDate);
	
}
