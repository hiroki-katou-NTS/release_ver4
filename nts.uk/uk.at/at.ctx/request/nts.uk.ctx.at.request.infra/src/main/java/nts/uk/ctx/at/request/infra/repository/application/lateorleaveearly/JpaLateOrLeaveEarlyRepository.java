package nts.uk.ctx.at.request.infra.repository.application.lateorleaveearly;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarly;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarlyRepository;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.infra.entity.application.lateorleaveearly.KrqdtAppLateOrLeave;
import nts.uk.ctx.at.request.infra.entity.application.lateorleaveearly.KrqdtAppLateOrLeavePK;

@Stateless
public class JpaLateOrLeaveEarlyRepository extends JpaRepository implements LateOrLeaveEarlyRepository {
	
	//private final String SELECT= "SELECT c FROM KrqdtAppLateOrLeave c";
	//private final String SELECT_ALL_BY_COMPANY = SELECT + " WHERE c.KrqdtAppLateOrLeavePK.companyID = :companyID";
	private final String SELECT_SINGLE = "SELECT c"
			+ " FROM KrqdtAppLateOrLeave c"
			+ " WHERE c.krqdtAppLateOrLeavePK.appID = :appID AND c.krqdtAppLateOrLeavePK.companyID = :companyID";
	@Override
	public Optional<LateOrLeaveEarly> findByCode(String companyID, String appID) {
		return this.queryProxy()
				.query(SELECT_SINGLE, KrqdtAppLateOrLeave.class)
				.setParameter("companyID", companyID)
				.setParameter("appID", appID)
				.getSingle(c -> c.toDomain());
	}
	/**
	 * Add
	 * @param lateOrLeaveEarly
	 * @return  
	 */
	@Override
	public void add(LateOrLeaveEarly lateOrLeaveEarly) {
		this.commandProxy().insert(KrqdtAppLateOrLeave.toEntity(lateOrLeaveEarly));
		
	}
	/**
	 * Update
	 * @param lateOrLeaveEarly
	 * @return 
	 */	
	@Override
	public void update(LateOrLeaveEarly lateOrLeaveEarly) {
		KrqdtAppLateOrLeave newEntity = KrqdtAppLateOrLeave.toEntity(lateOrLeaveEarly);
		KrqdtAppLateOrLeave updateEntity = this.queryProxy().find(newEntity.krqdtAppLateOrLeavePK, KrqdtAppLateOrLeave.class).get();
		updateEntity.actualCancelAtr = newEntity.actualCancelAtr;
		updateEntity.early1 = newEntity.early1;
		updateEntity.earlyTime1 = newEntity.earlyTime1;
		updateEntity.late1 = newEntity.late1;
		updateEntity.lateTime1 = newEntity.lateTime1;
		updateEntity.early2 = newEntity.early2;
		updateEntity.earlyTime2 = newEntity.earlyTime2;
		updateEntity.late2 = newEntity.late2;
		updateEntity.lateTime2 = newEntity.lateTime2;
		updateEntity.version = newEntity.version;
		updateEntity.version = newEntity.version;
		this.commandProxy().update(updateEntity);
		
	}

	@Override
	public void remove(String companyID, String appID) {
		this.commandProxy().remove(KrqdtAppLateOrLeave.class, new KrqdtAppLateOrLeavePK(companyID, appID));
		this.getEntityManager().flush();
		
	}
	
	@Override
	public ApplicationReason findApplicationReason(String companyID, ApplicationType applicationType) {
		// TODO Auto-generated method stub
		return null;
	};
	

}
