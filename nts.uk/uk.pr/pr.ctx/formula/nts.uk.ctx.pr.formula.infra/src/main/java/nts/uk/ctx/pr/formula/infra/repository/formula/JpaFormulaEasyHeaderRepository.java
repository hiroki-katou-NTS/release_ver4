package nts.uk.ctx.pr.formula.infra.repository.formula;

import java.math.BigDecimal;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyHeader;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaEasyHeaderRepository;
import nts.uk.ctx.pr.formula.infra.entity.formula.QcfmtFormulaEasyHeader;
import nts.uk.ctx.pr.formula.infra.entity.formula.QcfmtFormulaEasyHeaderPK;

@Stateless
public class JpaFormulaEasyHeaderRepository extends JpaRepository implements FormulaEasyHeaderRepository {

	private static final String REMOVE_EASY_HEADER;

	private static final String FIND_EASY_HEADER;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE FROM QcfmtFormulaEasyHeader a ");
		builderString.append("WHERE a.qcfmtFormulaEasyHeaderPK.companyCode = :companyCode ");
		builderString.append("AND a.qcfmtFormulaEasyHeaderPK.formulaCode = :formulaCode ");
		builderString.append("AND a.qcfmtFormulaEasyHeaderPK.historyId = :historyId ");
		REMOVE_EASY_HEADER = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a FROM QcfmtFormulaEasyHeader a ");
		builderString.append("WHERE a.qcfmtFormulaEasyHeaderPK.companyCode = :companyCode ");
		builderString.append("AND a.qcfmtFormulaEasyHeaderPK.formulaCode = :formulaCode ");
		builderString.append("AND a.qcfmtFormulaEasyHeaderPK.historyId = :historyId ");
		FIND_EASY_HEADER = builderString.toString();
	}

	@Override
	public Optional<FormulaEasyHeader> findByPriKey(String companyCode, FormulaCode formulaCode, String historyId) {
		return this.queryProxy().query(FIND_EASY_HEADER, QcfmtFormulaEasyHeader.class)
				.setParameter("companyCode", companyCode).setParameter("formulaCode", formulaCode.v())
				.setParameter("historyId", historyId).getSingle(f -> (toDomain(f)));
	}

	@Override
	public void add(FormulaEasyHeader formulaEasyHead) {
		this.commandProxy().insert(toEntity(formulaEasyHead));
	}

	@Override
	public void remove(String companyCode, FormulaCode formulaCode, String historyId) {
		this.getEntityManager().createQuery(REMOVE_EASY_HEADER)
				.setParameter("companyCode", companyCode)
				.setParameter("formulaCode", formulaCode.v())
				.setParameter("historyId", historyId).executeUpdate();
	}

	private static FormulaEasyHeader toDomain(QcfmtFormulaEasyHeader qcfmtFormulaEasyHeader) {
		FormulaEasyHeader formulaEasyHead = FormulaEasyHeader.createFromJavaType(
				qcfmtFormulaEasyHeader.qcfmtFormulaEasyHeaderPK.companyCode,
				qcfmtFormulaEasyHeader.qcfmtFormulaEasyHeaderPK.formulaCode,
				qcfmtFormulaEasyHeader.qcfmtFormulaEasyHeaderPK.historyId, qcfmtFormulaEasyHeader.conditionAtr,
				qcfmtFormulaEasyHeader.qcfmtFormulaEasyHeaderPK.refMasterNo);
		return formulaEasyHead;
	}

	private static QcfmtFormulaEasyHeader toEntity(FormulaEasyHeader formulaEasyHeader) {
		val entity = new QcfmtFormulaEasyHeader();

		entity.qcfmtFormulaEasyHeaderPK = new QcfmtFormulaEasyHeaderPK();
		entity.qcfmtFormulaEasyHeaderPK.companyCode = formulaEasyHeader.getCompanyCode();
		entity.qcfmtFormulaEasyHeaderPK.formulaCode = formulaEasyHeader.getFormulaCode().v();
		entity.qcfmtFormulaEasyHeaderPK.historyId = formulaEasyHeader.getHistoryId();
		entity.qcfmtFormulaEasyHeaderPK.refMasterNo = new BigDecimal(formulaEasyHeader.getReferenceMasterNo().value);
		entity.conditionAtr = new BigDecimal(formulaEasyHeader.getConditionAtr().value);

		return entity;
	}
}
