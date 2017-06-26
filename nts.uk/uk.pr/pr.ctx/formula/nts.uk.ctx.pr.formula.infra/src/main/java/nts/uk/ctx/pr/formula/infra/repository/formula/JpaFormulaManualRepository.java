package nts.uk.ctx.pr.formula.infra.repository.formula;

import java.math.BigDecimal;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.formula.dom.formula.FormulaManual;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaManualRepository;
import nts.uk.ctx.pr.formula.infra.entity.formula.QcfmtFormulaManual;
import nts.uk.ctx.pr.formula.infra.entity.formula.QcfmtFormulaManualPK;

@Stateless
public class JpaFormulaManualRepository extends JpaRepository implements FormulaManualRepository {

	private static final String DEL_FORMULA_MANUAL_BY_KEY;
	
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM QcfmtFormulaManual a ");
		builderString.append("WHERE a.qcfmtFormulaManualPK.companyCode = :companyCode ");
		builderString.append("AND a.qcfmtFormulaManualPK.formulaCode = :formulaCode ");
		builderString.append("AND a.qcfmtFormulaManualPK.historyId = :historyId ");
		DEL_FORMULA_MANUAL_BY_KEY = builderString.toString();
	}
	
	@Override
	public Optional<FormulaManual> findByPriKey(String companyCode, FormulaCode formulaCode, String historyId) {
		return this.queryProxy()
				.find(new QcfmtFormulaManualPK(companyCode, formulaCode.v(), historyId), QcfmtFormulaManual.class)
				.map(f -> toDomain(f));
	}

	@Override
	public void add(FormulaManual formulaManual) {
		this.commandProxy().insert(toEntity(formulaManual));
	}

	@Override
	public void update(FormulaManual formulaManual) {
		this.commandProxy().update(toEntity(formulaManual));
	}

	@Override
	public void remove(String companyCode, FormulaCode formulaCode, String historyId) {
		this.getEntityManager().createQuery(DEL_FORMULA_MANUAL_BY_KEY)
		.setParameter("companyCode", companyCode)
		.setParameter("formulaCode", formulaCode.v())
		.setParameter("historyId", historyId).executeUpdate();
	}

	private FormulaManual toDomain(QcfmtFormulaManual qcfmtFormulaManual) {
		FormulaManual formulaManual = FormulaManual.createFromJavaType(
				qcfmtFormulaManual.qcfmtFormulaManualPK.companyCode,
				qcfmtFormulaManual.qcfmtFormulaManualPK.formulaCode, qcfmtFormulaManual.qcfmtFormulaManualPK.historyId,
				qcfmtFormulaManual.formulaContent, qcfmtFormulaManual.referMonthAtr, qcfmtFormulaManual.roundAtr,
				qcfmtFormulaManual.roundDigit);

		return formulaManual;
	}  

	private QcfmtFormulaManual toEntity(FormulaManual formulaManual) {

		val entity = new QcfmtFormulaManual();

		entity.qcfmtFormulaManualPK = new QcfmtFormulaManualPK();
		entity.qcfmtFormulaManualPK.companyCode = formulaManual.getCompanyCode();
		entity.qcfmtFormulaManualPK.formulaCode = formulaManual.getFormulaCode().v();
		entity.qcfmtFormulaManualPK.historyId = formulaManual.getHistoryId();
		entity.formulaContent = formulaManual.getFormulaContent().v();
		entity.referMonthAtr = new BigDecimal(formulaManual.getReferenceMonthAtr().value);
		entity.roundAtr = new BigDecimal(formulaManual.getRoundAtr().value);
		entity.roundDigit = new BigDecimal(formulaManual.getRoundDigit().value);

		return entity;
	}

}
