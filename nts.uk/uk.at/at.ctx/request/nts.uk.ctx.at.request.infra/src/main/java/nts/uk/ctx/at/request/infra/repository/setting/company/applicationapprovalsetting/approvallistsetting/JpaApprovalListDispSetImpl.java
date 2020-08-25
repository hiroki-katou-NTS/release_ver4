package nts.uk.ctx.at.request.infra.repository.setting.company.applicationapprovalsetting.approvallistsetting;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.uk.ctx.at.request.dom.setting.DisplayAtr;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.approvallistsetting.ApprovalListDispSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.approvallistsetting.ApprovalListDisplaySetting;
import nts.uk.ctx.at.request.infra.entity.setting.company.applicationapprovalsetting.approvallistsetting.KrqmtApproval;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.approvallistsetting.WeekNumberDays;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * refactor 4
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaApprovalListDispSetImpl extends JpaRepository implements ApprovalListDispSetRepository {

	@Override
	public Optional<ApprovalListDisplaySetting> findByCID(String companyID) {
		return this.queryProxy().find(companyID, KrqmtApproval.class).map(KrqmtApproval::toDomain);
	}

	@Override
	public void save(ApprovalListDisplaySetting domain) {
		Optional<KrqmtApproval> optEntity = this.queryProxy().find(domain.getCompanyID(), KrqmtApproval.class);
		if (optEntity.isPresent()) {
			KrqmtApproval entity = optEntity.get();
			entity.setAppReasonDisAtr(domain.getAppReasonDisAtr().value);
			entity.setAdvanceExcessMessDisAtr(domain.getAdvanceExcessMessDisAtr().value);
			entity.setActualExcessMessDisAtr(domain.getActualExcessMessDisAtr().value);
			entity.setWarningDateDisAtr(domain.getWarningDateDisAtr().v());
			entity.setDisplayWorkPlaceName(domain.getDisplayWorkPlaceName().value);
			this.commandProxy().update(entity);
		} else {
			KrqmtApproval entity = new KrqmtApproval(
					domain.getCompanyID(),
					domain.getAppReasonDisAtr().value,
					domain.getAdvanceExcessMessDisAtr().value,
					domain.getActualExcessMessDisAtr().value,
					domain.getWarningDateDisAtr().v(),
					domain.getDisplayWorkPlaceName().value
			);
			this.commandProxy().insert(entity);
		}
	}

}
