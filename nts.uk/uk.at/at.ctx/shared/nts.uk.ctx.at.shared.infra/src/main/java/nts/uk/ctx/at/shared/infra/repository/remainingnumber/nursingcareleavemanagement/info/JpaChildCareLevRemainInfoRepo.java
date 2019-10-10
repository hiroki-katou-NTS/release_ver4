package nts.uk.ctx.at.shared.infra.repository.remainingnumber.nursingcareleavemanagement.info;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.ChildCareLeaveRemInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.ChildCareLeaveRemainingInfo;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.nursingcareleave.KrcmtChildCareHDInfo;

@Stateless
public class JpaChildCareLevRemainInfoRepo extends JpaRepository implements ChildCareLeaveRemInfoRepository {

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<ChildCareLeaveRemainingInfo> getChildCareByEmpId(String empId) {
		Optional<KrcmtChildCareHDInfo> entityOpt = this.queryProxy().find(empId, KrcmtChildCareHDInfo.class);
		if (entityOpt.isPresent()) {
			KrcmtChildCareHDInfo entity = entityOpt.get();
			return Optional.of(ChildCareLeaveRemainingInfo.createChildCareLeaveInfo(entity.getSId(), entity.getUseAtr(),
					entity.getUpperLimSetAtr(), entity.getMaxDayThisFiscalYear(), entity.getMaxDayNextFiscalYear()));
		}
		return Optional.empty();
	}

	@Override
	public void add(ChildCareLeaveRemainingInfo obj, String cId) {
		KrcmtChildCareHDInfo entity = new KrcmtChildCareHDInfo(obj.getSId(), cId, obj.isUseClassification() ? 1 : 0,
				obj.getUpperlimitSetting().value,
				obj.getMaxDayForThisFiscalYear().isPresent() ? obj.getMaxDayForThisFiscalYear().get().v() : null,
				obj.getMaxDayForNextFiscalYear().isPresent() ? obj.getMaxDayForNextFiscalYear().get().v() : null);
		this.commandProxy().insert(entity);

	}

	@Override
	public void update(ChildCareLeaveRemainingInfo obj, String cId) {
		Optional<KrcmtChildCareHDInfo> entityOpt = this.queryProxy().find(obj.getSId(), KrcmtChildCareHDInfo.class);
		if (entityOpt.isPresent()) {
			KrcmtChildCareHDInfo entity = entityOpt.get();
			entity.setCId(cId);
			entity.setUseAtr(obj.isUseClassification() ? 1 : 0);
			entity.setUpperLimSetAtr(obj.getUpperlimitSetting().value);
			entity.setMaxDayNextFiscalYear(
					obj.getMaxDayForNextFiscalYear().isPresent() ? obj.getMaxDayForNextFiscalYear().get().v() : null);
			entity.setMaxDayThisFiscalYear(
					obj.getMaxDayForThisFiscalYear().isPresent() ? obj.getMaxDayForThisFiscalYear().get().v() : null);
			this.commandProxy().update(entity);
		}
	}

}
