package nts.uk.ctx.at.record.ac.approvalrootstate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.approvalrootstate.AppRootStateConfirmAdapter;
import nts.uk.ctx.at.record.dom.adapter.approvalrootstate.AppRootStateConfirmImport;
import nts.uk.ctx.workflow.pub.service.ApprovalRootStatePub;
import nts.uk.ctx.workflow.pub.service.export.AppRootStateConfirmExport;

@Stateless
public class AppRootStateConfirmAdapterImpl implements AppRootStateConfirmAdapter {

	@Inject
	private ApprovalRootStatePub approvalRootStatePub;

	@Override
	public AppRootStateConfirmImport appRootStateConfirmFinder(String companyID, String employeeID, Integer confirmAtr,
			Integer appType, GeneralDate date) {
		AppRootStateConfirmExport appRootStateConfirmExport = this.approvalRootStatePub.getApprovalRootState(companyID,
				employeeID, confirmAtr, appType, date);
		return new AppRootStateConfirmImport(appRootStateConfirmExport.getIsError(),
				appRootStateConfirmExport.getRootStateID(), appRootStateConfirmExport.getErrorMsg());
	}

	@Override
	public void clearAppRootstate(String rootId) {
		approvalRootStatePub.cleanApprovalRootState(rootId, 1);
	}

}
