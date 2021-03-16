package nts.uk.ctx.at.request.dom.application.stamp;

import java.util.Optional;

import nts.uk.ctx.at.request.dom.application.Application;

public interface AppRecordImageRepository {
	
	public Optional<AppRecordImage> findByAppID(String companyID, String appID);

	public void addStamp(AppRecordImage appStamp);

	public void updateStamp(AppRecordImage appStamp);

	public void delete(String companyID, String appID);
	
	public Optional<AppRecordImage> findByAppID(String companyID, String appID, Application app);
}
