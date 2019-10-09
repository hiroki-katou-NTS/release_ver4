/**
 * 
 */
package nts.uk.ctx.at.auth.infra.repository.initswitchsetting;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.auth.dom.initswitchsetting.InitDisplayPeriodSwitchSet;
import nts.uk.ctx.at.auth.dom.initswitchsetting.InitDisplayPeriodSwitchSetRepo;
import nts.uk.ctx.at.auth.infra.entity.initswitchsetting.KacmtDispPeriodSwitch;

/**
 * @author hieult
 *
 */
@Stateless
public class JpaInitDisplayPeriodSwitchSetRepo extends JpaRepository implements InitDisplayPeriodSwitchSetRepo{

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.auth.dom.initswitchsetting.InitDisplayPeriodSwitchSetRepo#findByKey(java.lang.String, java.lang.String)
	 */
	private static final String GET_BY_KEY = "SELECT e "
			+ " FROM KacmtDispPeriodSwitch e"
			+ " WHERE e.kacmtDispPeriodSwitchPK.companyID = :companyID"
			+ " AND e.kacmtDispPeriodSwitchPK.roleID = :roleID";
			@Override
			
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<InitDisplayPeriodSwitchSet> findByKey(String companyID, String roleID) {
		
		return this.queryProxy().query(GET_BY_KEY, KacmtDispPeriodSwitch.class)
				.setParameter("companyID", companyID)
				.setParameter("roleID", roleID)
				.getSingle(c -> c.toDomain());
	}

}
