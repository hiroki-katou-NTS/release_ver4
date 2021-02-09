package nts.uk.ctx.at.shared.infra.repository.remainingnumber;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxHistRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxHistoryData;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.annlea.KrcdtAnnLeaMaxHist;
import nts.uk.ctx.at.shared.infra.entity.worktype.KshmtWorkType;
import nts.uk.ctx.at.shared.infra.entity.worktype.KshmtWorkTypePK;

/**
 *
 * @author HungTT
 *
 */

@Stateless
public class JpaAnnualLeaveMaxHistRepository extends JpaRepository implements AnnualLeaveMaxHistRepository {

	@Override
	public void addOrUpdate(AnnualLeaveMaxHistoryData domain) {
		Optional<KrcdtAnnLeaMaxHist> opt = this.queryProxy().find(domain.getEmployeeId(), KrcdtAnnLeaMaxHist.class);
		if (opt.isPresent()) {
			KrcdtAnnLeaMaxHist entity = opt.get();
			if (domain.getHalfdayAnnualLeaveMax().isPresent()) {
				entity.maxTimes = domain.getHalfdayAnnualLeaveMax().get().getMaxTimes().v();
				entity.usedTimes = domain.getHalfdayAnnualLeaveMax().get().getUsedTimes().v();
				entity.remainingTimes = domain.getHalfdayAnnualLeaveMax().get().getRemainingTimes().v();
			}
			if (domain.getTimeAnnualLeaveMax().isPresent()) {
				entity.maxMinutes = domain.getTimeAnnualLeaveMax().get().getMaxMinutes().v();
				entity.usedMinutes = domain.getTimeAnnualLeaveMax().get().getUsedMinutes().v();
				entity.remainingMinutes = domain.getTimeAnnualLeaveMax().get().getRemainingMinutes().v();
			}
			entity.yearMonth = domain.getYearMonth().v();
			entity.closureId = domain.getClosureId().value;
			entity.closeDay = domain.getClosureDate().getClosureDay().v();
			entity.isLastDay = domain.getClosureDate().getLastDayOfMonth() ? 1 : 0;
			this.commandProxy().update(entity);
		} else {
			this.commandProxy().insert(KrcdtAnnLeaMaxHist.fromDomain(domain));
		}
	}

	public Optional<AnnualLeaveMaxHistoryData> find(String employeeId, YearMonth yearMonth){
		Optional<KrcdtAnnLeaMaxHist> data =
				this.queryProxy().find(new KrcdtAnnLeaMaxHistPK(employeeId, yearMonth), KrcdtAnnLeaMaxHist.class);
		if (data.isPresent()) {
			return toDomain(data.get());
		}
		return Optional.empty();
	}

	private Optional<AnnualLeaveMaxHistoryData>toDomain(KrcdtAnnLeaMaxHist entity){
		//to do
		return null;
	}

}
