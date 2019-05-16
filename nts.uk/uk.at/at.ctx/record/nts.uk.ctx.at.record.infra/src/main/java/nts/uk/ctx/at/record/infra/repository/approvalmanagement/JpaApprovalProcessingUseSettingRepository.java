/**
 * 9:36:42 AM Mar 13, 2018
 */
package nts.uk.ctx.at.record.infra.repository.approvalmanagement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.infra.entity.approvalmanagement.KrcstAppProUseJbSet;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtApprovalProcess;

/**
 * @author hungnm
 *
 */
@Stateless
public class JpaApprovalProcessingUseSettingRepository extends JpaRepository
		implements ApprovalProcessingUseSettingRepository {

	public static final String SEL_USE_JB_SET_BY_CID = "SELECT c FROM KrcstAppProUseJbSet c WHERE c.krcstAppProUseJbSetPK.cId = :companyId";

	private ApprovalProcessingUseSetting fromEntity(Optional<KrcmtApprovalProcess> krcstAppProUseSet,
			List<KrcstAppProUseJbSet> lstKrcstAppProUseJbSet) {
		if (krcstAppProUseSet.isPresent()) {
			ApprovalProcessingUseSetting domain = new ApprovalProcessingUseSetting(krcstAppProUseSet.get().approvalProcessPk.cid,
					krcstAppProUseSet.get().useDailyBossChk == 1,
					krcstAppProUseSet.get().useMonthBossChk == 1,
					lstKrcstAppProUseJbSet.stream().map((entity) -> {
						return entity.krcstAppProUseJbSetPK.jobId;
					}).collect(Collectors.toList())).setSupervisorConfirmErrorAtr(krcstAppProUseSet.get().supervisorConfirmError.intValue());
			return domain;
		} else {
			return null;
		}
	}

	@Override
	@SneakyThrows
	public Optional<ApprovalProcessingUseSetting> findByCompanyId(String companyId) {
		
		Optional<KrcmtApprovalProcess> krcstAppProUseSet;
		{
			String sql = "select * from KRCMT_BOSS_CHECK_SET"
					+ " where CID = ?";
			try (val stmt = this.connection().prepareStatement(sql)) {
				stmt.setString(1, companyId);
				krcstAppProUseSet = new NtsResultSet(stmt.executeQuery())
						.getSingle(rec -> KrcmtApprovalProcess.MAPPER.toEntity(rec));
			}
		}
		
		List<KrcstAppProUseJbSet> lstKrcstAppProUseJbSet;
		{
			String sql = "select * from KRCST_APP_PRO_USE_JB_SET"
					+ " where CID = ?";
			try (val stmt = this.connection().prepareStatement(sql)) {
				stmt.setString(1, companyId);
				lstKrcstAppProUseJbSet = new NtsResultSet(stmt.executeQuery())
						.getList(rec -> KrcstAppProUseJbSet.MAPPER.toEntity(rec));
			}
		}
		
		return Optional.ofNullable(fromEntity(krcstAppProUseSet, lstKrcstAppProUseJbSet));
	}

}
