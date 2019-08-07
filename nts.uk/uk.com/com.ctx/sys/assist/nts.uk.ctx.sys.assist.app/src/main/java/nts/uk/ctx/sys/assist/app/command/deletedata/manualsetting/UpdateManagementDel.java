package nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletionRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class UpdateManagementDel {
	
	@Inject
	private ManagementDeletionRepository managementDelRepo;
	
	
	public void upDateNumberErorr(ManagementDeletion managementDeletion){
		try {
			managementDelRepo.update(managementDeletion);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
