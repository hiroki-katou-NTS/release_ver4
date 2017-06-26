package nts.uk.ctx.pr.core.infra.repository.rule.employment.allot;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.EmployeeAllotSettingHeader;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.EmployeeAllotSettingHeaderRespository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotHEm;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotHEmPK;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JpaEmployeeAllotSettingHeaderRespository extends JpaRepository
		implements EmployeeAllotSettingHeaderRespository {

	private final String SEL_1 = "SELECT c FROM QstmtStmtAllotHEm c WHERE c.QstmtStmtAllotHEmPK.companyCode = :companyCode ";
	private final String MAX_END = "SELECT MAX(d.endYm) FROM QstmtStmtAllotHEm d"
			+ " WHERE d.QstmtStmtAllotHEmPK.companyCode = :companyCode";

	@Override
	public Optional<EmployeeAllotSettingHeader> find(String companyCode) {
		// TODO Auto-generated method stub
		List<QstmtStmtAllotHEm> hemsettings = this.queryProxy().query(SEL_1, QstmtStmtAllotHEm.class)
				.setParameter("companyCode", companyCode).getList();
		if (hemsettings.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(toDomain(hemsettings.get(0)));
	}

	private final EmployeeAllotSettingHeader toDomain(QstmtStmtAllotHEm entity) {
		// return null;
		val domain = EmployeeAllotSettingHeader.createFromJavaType(entity.QstmtStmtAllotHEmPK.companyCode,
				entity.QstmtStmtAllotHEmPK.histId, entity.startYm, entity.endYm);
		return domain;
	}

	private static QstmtStmtAllotHEm toEntity(EmployeeAllotSettingHeader employeeAllotSettingHeader) {
		return new QstmtStmtAllotHEm(
				new QstmtStmtAllotHEmPK(AppContexts.user().companyCode(), employeeAllotSettingHeader.getHistoryId()),
				employeeAllotSettingHeader.getStartYm().v(), employeeAllotSettingHeader.getEndYm().v());

	}

	@Override
	public List<EmployeeAllotSettingHeader> findAll(String companyCode) {
		return this.queryProxy().query(SEL_1, QstmtStmtAllotHEm.class).setParameter("companyCode", companyCode)
				.getList(c -> toDomain(c));
	}

	@Override
	public Optional<Integer> findMaxEnd(String companyCode) {
		Optional<Integer> result = this.queryProxy().query(MAX_END, Integer.class).setParameter("companyCode", companyCode)
				.getSingle();
		System.out.println(result+ "------------");
		return result;
	}

}
