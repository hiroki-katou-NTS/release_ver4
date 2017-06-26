package nts.uk.ctx.pr.core.infra.repository.rule.employment.allot;

import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.ClassificationAllotSetting;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.ClassificationAllotSettingRespository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotCl;

@Stateless
public class JpaClassificationAllotSettingRepository extends JpaRepository
		implements ClassificationAllotSettingRespository {

	private final String SEL_1 = "SELECT c FROM QstmtStmtAllotCl c WHERE c.qstmtStmtAllotClPK.companyCode = :companyCode "
			+ " AND ( c.paymentDetailCode != '00'" + " OR c.bonusDetailCode != '00')";;

	private final String SEL_LST001 = "SELECT c FROM QstmtStmtAllotCl c WHERE c.qstmtStmtAllotClPK.companyCode = :companyCode "
			+" AND (c.qstmtStmtAllotClPK.historyId = :historyId)"
			+ " AND ( c.paymentDetailCode != '00'" + " OR c.bonusDetailCode != '00')";;

	@Override
	public Optional<ClassificationAllotSetting> find(String companyCode, String histortId, String classificationCode) {
		List<QstmtStmtAllotCl> psettings = this.queryProxy().query(SEL_1, QstmtStmtAllotCl.class)
				.setParameter("companyCode", companyCode).getList();
		if (psettings.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(toDomain(psettings.get(0)));
	}

	@Override
	public List<ClassificationAllotSetting> findAll(String companyCode) {
		return this.queryProxy().query(SEL_1, QstmtStmtAllotCl.class).setParameter("companyCode", companyCode)
				.getList(c -> toDomain(c));
	}

	@Override
	public List<ClassificationAllotSetting> findbyHistoryId(String companyCode, String historyId) {
		return this.queryProxy().query(SEL_LST001, QstmtStmtAllotCl.class).setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId).getList(c -> toDomain(c));

	}

	private ClassificationAllotSetting toDomain(QstmtStmtAllotCl entity) {
		val domain = ClassificationAllotSetting.createFromJavaType(entity.qstmtStmtAllotClPK.companyCode,
				entity.qstmtStmtAllotClPK.historyId, entity.qstmtStmtAllotClPK.classificationCode, entity.bonusDetailCode,
				entity.paymentDetailCode);
		return domain;
	}
}
