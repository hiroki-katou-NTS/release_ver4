package nts.uk.ctx.at.shared.infra.repository.remainingnumber.nursingcareleavemanagement.data;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.ChildCareLeaveRemaiDataRepo;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.ChildCareLeaveRemainingData;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.nursingcareleave.KrcmtChildCareHDData;

@Stateless
public class JpaChildCareLevRemainDataRepo extends JpaRepository implements ChildCareLeaveRemaiDataRepo {

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<ChildCareLeaveRemainingData> getChildCareByEmpId(String empId) {
		Optional<KrcmtChildCareHDData> entity = this.queryProxy().find(empId, KrcmtChildCareHDData.class);
		if (entity.isPresent()) {
			return Optional.of(ChildCareLeaveRemainingData.getChildCareHDRemaining(entity.get().getSId(),
					entity.get().getUserDay()));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void add(ChildCareLeaveRemainingData obj, String cId) {
		KrcmtChildCareHDData childCare = new KrcmtChildCareHDData(obj.getSId(), cId, obj.getNumOfUsedDay().v());
		this.commandProxy().insert(childCare);
	}

	@Override
	public void update(ChildCareLeaveRemainingData obj, String cId) {
		Optional<KrcmtChildCareHDData> entityOpt = this.queryProxy().find(obj.getSId(), KrcmtChildCareHDData.class);
		if (entityOpt.isPresent()) {
			KrcmtChildCareHDData entity = entityOpt.get();
			entity.setCId(cId);
			entity.setUserDay(obj.getNumOfUsedDay().v());
			this.commandProxy().update(entity);
		}
	}

}
