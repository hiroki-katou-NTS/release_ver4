package nts.uk.ctx.pr.formula.dom.formula;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.pr.formula.dom.primitive.EasyFormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.ReferenceItemCode;

@Getter
public class FormulaEasyStandardItem extends AggregateRoot{

	private String companyCode;

	private FormulaCode formulaCode;

	private String historyId;

	private EasyFormulaCode easyFormulaCode;

	private ReferenceItemCode referenceItemCode;

	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 * @param easyFormulaCode
	 * @param referenceItemCode
	 */
	public FormulaEasyStandardItem(String companyCode, FormulaCode formulaCode, String historyId,
			EasyFormulaCode easyFormulaCode, ReferenceItemCode referenceItemCode) {
		super();
		this.companyCode = companyCode;
		this.formulaCode = formulaCode;
		this.historyId = historyId;
		this.easyFormulaCode = easyFormulaCode;
		this.referenceItemCode = referenceItemCode;
	}

	public static FormulaEasyStandardItem createFromJavaType(String companyCode, String formulaCode, String historyId,
			String easyFormulaCode, String referenceItemCode) {
		return new FormulaEasyStandardItem(companyCode, new FormulaCode(formulaCode),
				historyId, new EasyFormulaCode(easyFormulaCode),
				new ReferenceItemCode(referenceItemCode));
	}

}
