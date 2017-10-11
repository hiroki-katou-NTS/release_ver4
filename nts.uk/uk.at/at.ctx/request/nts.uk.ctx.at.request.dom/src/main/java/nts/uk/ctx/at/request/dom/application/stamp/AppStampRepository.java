package nts.uk.ctx.at.request.dom.application.stamp;

import nts.arc.time.GeneralDate;
/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface AppStampRepository {
	
	public AppStamp findByAppID(String companyID, String appID);
	
	public AppStamp findByAppDate(String companyID, GeneralDate appDate, StampRequestMode stampRequestMode, String employeeID);
	
	public void addStamp(AppStamp appStamp);
	
	public void updateStamp(AppStamp appStamp);
	
}
