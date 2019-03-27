package nts.uk.ctx.at.request.dom.application.appabsence;

import java.util.List;
import java.util.Optional;

public interface AppAbsenceRepository {

	/**
	 * @param companyID
	 * @param appId
	 * @return
	 */
	public Optional<AppAbsence> getAbsenceById(String companyID, String appId);
	
	public List<AppAbsence> getAbsenceByIds(String companyID, List<String> appId);
	
	/**
	 * insertAbsence
	 * @param appAbsence
	 */
	public void insertAbsence(AppAbsence appAbsence);
	
	/**
	 * insertAbsence
	 * @param appAbsence
	 */
	public void updateAbsence(AppAbsence appAbsence);
	
	/**
	 * get Application and AppAbsence
	 * @param companyID
	 * @param appId
	 * @return
	 */
	public Optional<AppAbsence> getAbsenceByAppId(String companyID, String appId);
	
	/**
	 * delete
	 * @param companyID
	 * @param appID
	 */
	public void delete(String companyID, String appID);
}
