package nts.uk.ctx.pr.formula.app.find.formula;

import lombok.Value;
import nts.uk.ctx.pr.formula.dom.formula.FormulaMaster;

@Value
public class FormulaDto {

	private String companyCode;

	private String formulaCode;

	private String formulaName;

	private int difficultyAtr;

	public static FormulaDto fromDomain(FormulaMaster formulaMaster) {
		return new FormulaDto(formulaMaster.getCompanyCode(), formulaMaster.getFormulaCode().v(),
				formulaMaster.getFormulaName().v(), formulaMaster.getDifficultyAtr().value);
	}

}
