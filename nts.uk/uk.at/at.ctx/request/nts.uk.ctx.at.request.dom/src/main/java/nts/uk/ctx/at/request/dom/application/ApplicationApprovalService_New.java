package nts.uk.ctx.at.request.dom.application;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface ApplicationApprovalService_New {
	
	public void insert(Application_New application);
	
	public void delete(String companyID, String appID, Long version, ApplicationType appType);
	
}
