package nts.uk.ctx.pr.formula.dom.formula;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.pr.formula.dom.enums.ConditionAtr;
import nts.uk.ctx.pr.formula.dom.enums.ReferenceMasterNo;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;

/**
 * @author hungnm
 *
 */
@Getter
public class FormulaEasyHeader extends AggregateRoot {
	
	private String companyCode;
	
	private FormulaCode formulaCode;
	
	private String historyId;

	private ConditionAtr conditionAtr;

	private ReferenceMasterNo referenceMasterNo;

	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 * @param conditionAtr
	 * @param referenceMasterNo
	 */
	public FormulaEasyHeader(String companyCode, FormulaCode formulaCode, String historyId,
			ConditionAtr conditionAtr, ReferenceMasterNo referenceMasterNo) {
		super();
		this.companyCode = companyCode;
		this.formulaCode = formulaCode;
		this.historyId = historyId;
		this.conditionAtr = conditionAtr;
		this.referenceMasterNo = referenceMasterNo;
	}

	public static FormulaEasyHeader createFromJavaType(String companyCode, String formulaCode, String historyId, BigDecimal conditionAtr, BigDecimal referenceMasterNo) {
		return new FormulaEasyHeader(companyCode, new FormulaCode(formulaCode), historyId,
				EnumAdaptor.valueOf(conditionAtr.intValue(), ConditionAtr.class),
				EnumAdaptor.valueOf(referenceMasterNo.intValue(), ReferenceMasterNo.class));
	}

	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 */
	public FormulaEasyHeader(String companyCode, FormulaCode formulaCode, String historyId) {
		super();
		this.companyCode = companyCode;
		this.formulaCode = formulaCode;
		this.historyId = historyId;
	}

}
