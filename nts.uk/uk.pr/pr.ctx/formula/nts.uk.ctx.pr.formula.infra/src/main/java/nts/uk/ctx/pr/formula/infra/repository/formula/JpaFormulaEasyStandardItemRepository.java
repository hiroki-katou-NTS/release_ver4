package nts.uk.ctx.pr.formula.infra.repository.formula;

import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyStandardItem;
import nts.uk.ctx.pr.formula.dom.primitive.EasyFormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaEasyStandardItemRepository;
import nts.uk.ctx.pr.formula.infra.entity.formula.QcfmtFormulaEasyStandardItem;
import nts.uk.ctx.pr.formula.infra.entity.formula.QcfmtFormulaEasyStandardItemPK;

@Stateless
public class JpaFormulaEasyStandardItemRepository extends JpaRepository
		implements FormulaEasyStandardItemRepository {

	private final String REMOVE_FORMULA_EASY_STANRDARD_ITEM = "DELETE FROM QcfmtFormulaEasyStandardItem a "
			+ " WHERE a.qcfmtFormulaEasyStandardItemPK.companyCode = :companyCode "
			+ " AND a.qcfmtFormulaEasyStandardItemPK.formulaCode = :formulaCode "
			+ " AND a.qcfmtFormulaEasyStandardItemPK.historyId = :historyId ";

	private final String FIND_FORMULA_EASY_STANDARD = "SELECT a FROM QcfmtFormulaEasyStandardItem a "
			+ " WHERE a.qcfmtFormulaEasyStandardItemPK.companyCode = :companyCode "
			+ " AND a.qcfmtFormulaEasyStandardItemPK.formulaCode = :formulaCode "
			+ " AND a.qcfmtFormulaEasyStandardItemPK.historyId = :historyId "
			+ " AND a.qcfmtFormulaEasyStandardItemPK.easyFormulaCode = :easyFormulaCode ";

	@Override
	public List<FormulaEasyStandardItem> findAll(String companyCode, FormulaCode formulaCode, String historyId,
			EasyFormulaCode easyFormulaCode) {
		return this.queryProxy().query(FIND_FORMULA_EASY_STANDARD, QcfmtFormulaEasyStandardItem.class)
				.setParameter("companyCode", companyCode).setParameter("formulaCode", formulaCode.v())
				.setParameter("historyId", historyId).setParameter("easyFormulaCode", easyFormulaCode.v())
				.getList(f -> toDomain(f));
	}

	@Override
	public void remove(String companyCode, FormulaCode formulaCode, String historyId) {
		this.getEntityManager().createQuery(REMOVE_FORMULA_EASY_STANRDARD_ITEM).setParameter("companyCode", companyCode)
				.setParameter("formulaCode", formulaCode.v()).setParameter("historyId", historyId).executeUpdate();
	}

	@Override
	public void add(List<FormulaEasyStandardItem> formulaEasyStandardItem) {
		formulaEasyStandardItem.stream().forEach(item -> this.commandProxy().insert(toEntity(item)));
	}

	private QcfmtFormulaEasyStandardItem toEntity(FormulaEasyStandardItem formulaEasyStandardItem) {
		val entity = new QcfmtFormulaEasyStandardItem();

		entity.qcfmtFormulaEasyStandardItemPK = new QcfmtFormulaEasyStandardItemPK();
		entity.qcfmtFormulaEasyStandardItemPK.companyCode = formulaEasyStandardItem.getCompanyCode();
		entity.qcfmtFormulaEasyStandardItemPK.easyFormulaCode = formulaEasyStandardItem.getEasyFormulaCode().v();
		entity.qcfmtFormulaEasyStandardItemPK.formulaCode = formulaEasyStandardItem.getFormulaCode().v();
		entity.qcfmtFormulaEasyStandardItemPK.historyId = formulaEasyStandardItem.getHistoryId();
		entity.qcfmtFormulaEasyStandardItemPK.referenceItemCode = formulaEasyStandardItem.getReferenceItemCode().v();

		return entity;
	}

	private FormulaEasyStandardItem toDomain(QcfmtFormulaEasyStandardItem qcfmtFormulaEasyStandardItem) {
		FormulaEasyStandardItem formulaEasyStandardItem = FormulaEasyStandardItem.createFromJavaType(
				qcfmtFormulaEasyStandardItem.qcfmtFormulaEasyStandardItemPK.companyCode,
				qcfmtFormulaEasyStandardItem.qcfmtFormulaEasyStandardItemPK.formulaCode,
				qcfmtFormulaEasyStandardItem.qcfmtFormulaEasyStandardItemPK.historyId,
				qcfmtFormulaEasyStandardItem.qcfmtFormulaEasyStandardItemPK.easyFormulaCode,
				qcfmtFormulaEasyStandardItem.qcfmtFormulaEasyStandardItemPK.referenceItemCode);

		return formulaEasyStandardItem;
	}
}
