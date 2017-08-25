package nts.uk.ctx.at.request.dom.application.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository {
	
	/**
	 * get All application 
	 * @return list application
	 */
	List<Application> getAllApplication(String companyID);
	/**
	 * get Application by applicationID
	 * @param companyID
	 * @param applicationID
	 * @return a application
	 */
	Optional<Application> getAppById(String companyID,String applicationID);
	/**
	 * get all application by applicationDate
	 * @param companyID
	 * @param applicationDate
	 * @return
	 */
	List<Application> getAllAppByDate(String companyID,BigDecimal applicationDate);
	/**
	 * get all application by applicationType
	 * @param companyID
	 * @param applicationType
	 * @return
	 */
	List<Application> getAllAppByAppType(String companyID,int applicationType);
	
	/**
	 * add application
	 * @param application
	 */
	void addApplication(Application application);
	/**
	 * update application
	 * @param application
	 */
	void updateApplication(Application application);
	/**
	 * delete application
	 * @param companyID
	 * @param applicationID
	 */
	void deleteApplication(String companyID,String applicationID);
	
	void updateById(String companyID,String applicationID );
	
}
