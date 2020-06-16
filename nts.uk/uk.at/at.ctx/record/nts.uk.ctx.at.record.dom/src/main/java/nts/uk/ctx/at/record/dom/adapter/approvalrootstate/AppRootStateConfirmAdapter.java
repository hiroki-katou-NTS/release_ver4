package nts.uk.ctx.at.record.dom.adapter.approvalrootstate;

import nts.arc.time.GeneralDate;

public interface AppRootStateConfirmAdapter {
	
	AppRootStateConfirmImport appRootStateConfirmFinder(String companyID, String employeeID, 
			Integer confirmAtr, Integer appType, GeneralDate date);

	void clearAppRootstate(String rootId);
	
}
