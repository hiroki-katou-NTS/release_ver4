package nts.uk.ctx.workflow.infra.repository.approvermanagement.setting;

import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.ApprovalSetting;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.ApprovalSettingRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.PrincipalApprovalFlg;
import nts.uk.ctx.workflow.infra.entity.approvermanagement.setting.WwfstApprovalSetting;

@Stateless
public class JpaApprovalSettingRepository extends JpaRepository implements ApprovalSettingRepository {
	private final String SQL_FIND = "SELECT c FROM WwfstApprovalSetting c WHERE c.companyId = :companyId";

	@Override
	public Optional<PrincipalApprovalFlg> getPrincipalByCompanyId(String companyId) {
		return this.queryProxy().query(SQL_FIND, WwfstApprovalSetting.class)
				.setParameter("companyId", companyId)
				.getSingle(c-> EnumAdaptor.valueOf(c.principalApprovalFlg, PrincipalApprovalFlg.class));
	}
	
	private WwfstApprovalSetting toEntity(ApprovalSetting domain){
		val entity = new WwfstApprovalSetting();
		entity.companyId = domain.getCompanyId();
		entity.principalApprovalFlg = domain.getPrinFlg().value;
		return entity;
	}
	/**
	 * update approvalSetting
	 * @author yennth
	 */
	@Override
	public void update(ApprovalSetting appro) {
		WwfstApprovalSetting entity = toEntity(appro);
		WwfstApprovalSetting oldEntity = this.queryProxy().find(entity.companyId, WwfstApprovalSetting.class).get();
		oldEntity.principalApprovalFlg = entity.principalApprovalFlg;
		this.commandProxy().update(oldEntity);
	}
	
}
