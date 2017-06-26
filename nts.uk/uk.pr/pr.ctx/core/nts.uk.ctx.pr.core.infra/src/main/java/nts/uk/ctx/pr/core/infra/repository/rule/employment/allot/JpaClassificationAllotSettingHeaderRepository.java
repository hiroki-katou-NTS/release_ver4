package nts.uk.ctx.pr.core.infra.repository.rule.employment.allot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.ClassificationAllotSettingHeader;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.ClassificationAllotSettingHeaderRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotHCl;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotHClPK;

@Stateless
public class JpaClassificationAllotSettingHeaderRepository extends JpaRepository
		implements ClassificationAllotSettingHeaderRepository {

	private final String SEL = " SELECT c FROM QstmtStmtAllotHCl c WHERE c.qstmtStmtAllotHClPK.companyCode = :companyCode ";

	// public Optional<ClassificationAllotSettingHeader> find(String
	// companyCode, String histortId) {
	// List<QstmtStmtAllotHCl> psettings = this.queryProxy().query(SEL_1,
	// QstmtStmtAllotHCl.class)
	// .setParameter("companyCode", companyCode).getList();
	// if (psettings.isEmpty()) {
	// return Optional.empty();
	// }
	// return Optional.of(toDomain(psettings.get(0)));
	// }

	private ClassificationAllotSettingHeader toDomain(QstmtStmtAllotHCl entity) {
		val domain = ClassificationAllotSettingHeader.createFromJavaType(entity.qstmtStmtAllotHClPK.companyCode,
				entity.qstmtStmtAllotHClPK.historyId, entity.startDateYM, entity.endDateYM);
		return domain;
	}

	@Override
	public List<ClassificationAllotSettingHeader> findAll(String companyCode) {
		List<QstmtStmtAllotHCl> resultList = this.queryProxy().query(SEL, QstmtStmtAllotHCl.class)
				.setParameter("companyCode", companyCode).getList();
		return !resultList.isEmpty() ? resultList.stream().map(e -> {
			return toDomain(e);
		}).collect(Collectors.toList()) : new ArrayList<>();
	}

	@Override
	public void update(ClassificationAllotSettingHeader classificationAllotSettingHeader) {
		this.commandProxy().update(toEntity(classificationAllotSettingHeader));
	}

	private QstmtStmtAllotHCl toEntity(ClassificationAllotSettingHeader domain) {
		QstmtStmtAllotHClPK key = new QstmtStmtAllotHClPK(domain.getCompanyCode().v(), domain.getHistoryId());
		QstmtStmtAllotHCl entity = new QstmtStmtAllotHCl(key, domain.getStartDateYM(),domain.getEndDateYM());
		return entity;
	}

}
