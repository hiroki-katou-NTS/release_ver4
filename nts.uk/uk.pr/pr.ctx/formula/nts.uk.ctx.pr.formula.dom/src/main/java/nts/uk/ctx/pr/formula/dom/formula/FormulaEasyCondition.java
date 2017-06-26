package nts.uk.ctx.pr.formula.dom.formula;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.pr.formula.dom.enums.FixFormulaAtr;
import nts.uk.ctx.pr.formula.dom.primitive.EasyFormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.Money;
import nts.uk.ctx.pr.formula.dom.primitive.ReferenceMasterCode;

/**
 * @author hungnm
 *
 */
@Getter
public class FormulaEasyCondition extends AggregateRoot {
	
	private String companyCode;
	
	private FormulaCode formulaCode;
	
	private String historyId;
	
	private EasyFormulaCode easyFormulaCode;

	private FixFormulaAtr fixFormulaAtr;

	private Money fixMoney;

	private ReferenceMasterCode referenceMasterCode;

 
	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 * @param easyFormulaCode
	 * @param fixFormulaAtr
	 * @param fixMoney
	 * @param referenceMasterCode
	 * @param formulaEasyDetail
	 */
	public FormulaEasyCondition(String companyCode, FormulaCode formulaCode, String historyId, EasyFormulaCode easyFormulaCode, FixFormulaAtr fixFormulaAtr, Money fixMoney,
			ReferenceMasterCode referenceMasterCode) {
		super();
		this.companyCode = companyCode;
		this.formulaCode = formulaCode;
		this.historyId = historyId;
		this.easyFormulaCode = easyFormulaCode;
		this.fixFormulaAtr = fixFormulaAtr;
		this.fixMoney = fixMoney;
		this.referenceMasterCode = referenceMasterCode;
	}

	public static FormulaEasyCondition createFromJavaType(String companyCode, String formulaCode, String historyId, String easyFormulaCode, BigDecimal fixFormulaAtr, BigDecimal fixMoney,
			String referenceMasterCode) {
		return new FormulaEasyCondition(companyCode, new FormulaCode(formulaCode), historyId,
				new EasyFormulaCode(easyFormulaCode), EnumAdaptor.valueOf(fixFormulaAtr.intValue(), FixFormulaAtr.class),
				new Money(fixMoney), new ReferenceMasterCode(referenceMasterCode));
	}

	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 */
	public FormulaEasyCondition(String companyCode, FormulaCode formulaCode, String historyId) {
		super();
		this.companyCode = companyCode;
		this.formulaCode = formulaCode;
		this.historyId = historyId;
	}

	/**
	 * 
	 */
	public FormulaEasyCondition() {
		super();
	}

}
