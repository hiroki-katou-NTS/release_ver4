package nts.uk.ctx.at.request.infra.repository.application.lateorleaveearly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarly;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarlyRepository;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.infra.entity.application.lateorleaveearly.KrqdtAppLateOrLeave;
import nts.uk.ctx.at.request.infra.entity.application.lateorleaveearly.KrqdtAppLateOrLeavePK;

@Stateless
public class JpaLateOrLeaveEarlyRepository extends JpaRepository implements LateOrLeaveEarlyRepository {

	// private final String SELECT= "SELECT c FROM KrqdtAppLateOrLeave c";
	// private final String SELECT_ALL_BY_COMPANY = SELECT + " WHERE
	// c.KrqdtAppLateOrLeavePK.companyID = :companyID";
	private static final String SELECT_SINGLE = "SELECT c" + " FROM KrqdtAppLateOrLeave c"
			+ " WHERE c.krqdtAppLateOrLeavePK.appID = :appID AND c.krqdtAppLateOrLeavePK.companyID = :companyID";
	private static final String SELECT_LIST_CANCEL_ATR = "SELECT c FROM KrqdtAppLateOrLeave c "
			+ "WHERE c.krqdtAppLateOrLeavePK.appID IN :listAppID AND c.actualCancelAtr = :actualCancelAtr";

	@Override
	public Optional<LateOrLeaveEarly> findByCode(String companyID, String appID) {
		return this.queryProxy().query(SELECT_SINGLE, KrqdtAppLateOrLeave.class).setParameter("companyID", companyID)
				.setParameter("appID", appID).getSingle(c -> c.toDomain());
	}

	/**
	 * Add
	 * 
	 * @param lateOrLeaveEarly
	 * @return
	 */
	@Override
	public void add(LateOrLeaveEarly lateOrLeaveEarly) {
		this.commandProxy().insert(KrqdtAppLateOrLeave.toEntity(lateOrLeaveEarly));

	}

	/**
	 * Update
	 * 
	 * @param lateOrLeaveEarly
	 * @return
	 */
	@Override
	public void update(LateOrLeaveEarly lateOrLeaveEarly) {
		this.commandProxy().update(KrqdtAppLateOrLeave.toEntity(lateOrLeaveEarly));

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
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<LateOrLeaveEarly> findByActualCancelAtr(List<String> listAppID, Integer actualCancelAtr) {
		List<LateOrLeaveEarly> resultList = new ArrayList<>();
		CollectionUtil.split(listAppID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(SELECT_LIST_CANCEL_ATR, KrqdtAppLateOrLeave.class)
								  .setParameter("listAppID", subList)
								  .setParameter("actualCancelAtr", actualCancelAtr)
								  .getList(c -> c.toDomain()));
		});
		return resultList;
	};

}
