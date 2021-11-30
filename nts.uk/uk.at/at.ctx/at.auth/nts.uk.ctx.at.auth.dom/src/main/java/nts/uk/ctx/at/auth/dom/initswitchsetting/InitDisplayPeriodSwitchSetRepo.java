/**
 * 
 */
package nts.uk.ctx.at.auth.dom.initswitchsetting;

import java.util.Optional;

/**
 * @author hieult
 *
 */
public interface InitDisplayPeriodSwitchSetRepo {
	
	Optional<InitDisplayPeriodSwitchSet> findByKey (String companyID , String roleID);
	
	void save (InitDisplayPeriodSwitchSet domain);
	
	void deleteByRoleAndCompany (String cid, String roleID);
}
