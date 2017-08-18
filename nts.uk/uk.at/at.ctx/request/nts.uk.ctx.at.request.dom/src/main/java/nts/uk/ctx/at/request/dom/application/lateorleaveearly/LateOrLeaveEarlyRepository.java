package nts.uk.ctx.at.request.dom.application.lateorleaveearly;

import java.util.Optional;

/**
 * 
 * @author hieult
 *
 */
public interface LateOrLeaveEarlyRepository {

	/**
	 * Find by Code
	 * @param companyID
	 * @param appID
	 * @return
	 */
	Optional<LateOrLeaveEarly> findByCode(String companyID, String appID);
	
	/**
	 * Add 
	 * @param lateOrLeaveEarly
	 */
	void add (LateOrLeaveEarly lateOrLeaveEarly);
	
	/**
	 * Update 
	 * @param lateOrLeaveEarly
	 */
	void update (LateOrLeaveEarly lateOrLeaveEarly);
	
	/**
	 * Remove
	 * @param companyID
	 * @param appID
	 */
	void remove (String companyID , String appID);
}
