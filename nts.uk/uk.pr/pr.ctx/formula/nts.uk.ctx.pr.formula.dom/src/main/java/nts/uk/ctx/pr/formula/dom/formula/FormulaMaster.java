package nts.uk.ctx.pr.formula.dom.formula;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.formula.dom.enums.DifficultyAtr;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaName;


/**
 * @author hungnm
 *
 */
@Getter
public class FormulaMaster extends AggregateRoot {

	private String companyCode;

	private FormulaCode formulaCode;

	private FormulaName formulaName;

	private DifficultyAtr difficultyAtr;

	/**
	 * @param formulaCode
	 * @param formulaName
	 */
	public FormulaMaster(FormulaCode formulaCode, FormulaName formulaName) {
		super();
		this.formulaCode = formulaCode;
		this.formulaName = formulaName;
	}

	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param difficultyAtr
	 * @param formulaName
	 */
	public FormulaMaster(String companyCode, FormulaCode formulaCode, DifficultyAtr difficultyAtr,
			FormulaName formulaName) {
		super();
		this.companyCode = companyCode;
		this.formulaCode = formulaCode;
		this.difficultyAtr = difficultyAtr;
		this.formulaName = formulaName;
	}
	
	/**
	 * create basic formula From Java Type
	 * 
	 * @return LayoutMaster
	 */
	public static FormulaMaster createFromJavaType(String companyCode, String formulaCode, BigDecimal difficultyAtr,
			String formulaName) {
		return new FormulaMaster(companyCode, new FormulaCode(formulaCode), 
				EnumAdaptor.valueOf(difficultyAtr.intValue(), DifficultyAtr.class),
				new FormulaName(formulaName));
	}
	
	public void validate(){
		super.validate();

		if (StringUtil.isNullOrEmpty(formulaCode.v(), true) || StringUtil.isNullOrEmpty(formulaName.v(), true)) {
			throw new BusinessException("ER001");
		}
	}
}
