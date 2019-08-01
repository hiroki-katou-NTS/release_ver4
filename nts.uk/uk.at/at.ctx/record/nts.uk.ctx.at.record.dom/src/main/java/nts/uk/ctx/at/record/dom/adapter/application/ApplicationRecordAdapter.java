package nts.uk.ctx.at.record.dom.adapter.application;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface ApplicationRecordAdapter {
	
	public List<ApplicationRecordImport> getApplicationBySID(List<String> employeeID, GeneralDate startDate, GeneralDate endDate);

}
