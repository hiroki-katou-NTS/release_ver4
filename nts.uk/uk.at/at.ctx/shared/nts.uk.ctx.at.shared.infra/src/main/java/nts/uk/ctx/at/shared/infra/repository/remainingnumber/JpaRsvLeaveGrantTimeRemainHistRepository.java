package nts.uk.ctx.at.shared.infra.repository.remainingnumber;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.ReserveLeaveGrantTimeRemainHistoryData;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.RsvLeaveGrantTimeRemainHistRepository;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.resvlea.empinfo.grantremainingdata.KrcdtReserveLeaveTimeRemainHist;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.resvlea.empinfo.grantremainingdata.KrcdtReserveLeaveTimeRemainHistPK;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class JpaRsvLeaveGrantTimeRemainHistRepository extends JpaRepository
		implements RsvLeaveGrantTimeRemainHistRepository {

	
	@Override
	public void addOrUpdate(ReserveLeaveGrantTimeRemainHistoryData domain, String cid) {
		KrcdtReserveLeaveTimeRemainHistPK leaveTimeRemainHistPK = new KrcdtReserveLeaveTimeRemainHistPK(domain.getEmployeeId(), domain.getGrantProcessDate(), domain.getGrantDate());
		Optional<KrcdtReserveLeaveTimeRemainHist> entityOpt = this.queryProxy().find(leaveTimeRemainHistPK,
				KrcdtReserveLeaveTimeRemainHist.class);
		if (entityOpt.isPresent()) {
			KrcdtReserveLeaveTimeRemainHist entity = entityOpt.get();
			entity.cid = cid;
			entity.deadline = domain.getDeadline();
			entity.expStatus = domain.getExpirationStatus().value;
			entity.registerType = domain.getRegisterType().value;
			entity.grantDays = domain.getDetails().getGrantNumber().v();
			entity.remainingDays = domain.getDetails().getRemainingNumber().v();
			entity.usedDays = domain.getDetails().getUsedNumber().getDays().v();
			entity.overLimitDays = domain.getDetails().getUsedNumber().getOverLimitDays().isPresent()
					? domain.getDetails().getUsedNumber().getOverLimitDays().get().v() : null;
			this.commandProxy().update(entity);
		} else {
			this.commandProxy().insert(KrcdtReserveLeaveTimeRemainHist.fromDomain(domain, cid));
		}
	}

	@Override
	public void deleteAfterDate(String employeeId, GeneralDate date) {
		String sql = "DELETE FROM KrcdtReserveLeaveTimeRemainHist a WHERE a.krcdtReserveLeaveTimeRemainHist.sid = :employeeId and a.krcdtReserveLeaveTimeRemainHist.grantProcessDate > :startDate";
		this.getEntityManager().createQuery(sql).setParameter("employeeId", employeeId).setParameter("startDate", date);
	}

}
