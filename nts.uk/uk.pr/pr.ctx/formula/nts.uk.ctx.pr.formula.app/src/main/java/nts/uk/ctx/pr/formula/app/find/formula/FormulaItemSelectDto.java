package nts.uk.ctx.pr.formula.app.find.formula;

import lombok.Data;
import nts.uk.ctx.pr.formula.dom.formula.FormulaMaster;

@Data
public class FormulaItemSelectDto {
	
	private String formulaCode;
	
	private String formulaName;
	
	public static FormulaItemSelectDto fromDomain(FormulaMaster domain){
		return new FormulaItemSelectDto(domain.getFormulaCode().v(), domain.getFormulaName().v());
	}

	/**
	 * @param formulaCode
	 * @param formulaName
	 */
	public FormulaItemSelectDto(String formulaCode, String formulaName) {
		super();
		this.formulaCode = formulaCode;
		this.formulaName = formulaName;
	}
	
}
