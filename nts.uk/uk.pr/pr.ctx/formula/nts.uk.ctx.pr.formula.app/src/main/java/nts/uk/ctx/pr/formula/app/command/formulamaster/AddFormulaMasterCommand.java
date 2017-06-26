package nts.uk.ctx.pr.formula.app.command.formulamaster;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;
import nts.uk.ctx.pr.formula.dom.formula.FormulaMaster;

/**
 * @author nampt
 *
 */
@Data
@NoArgsConstructor
public class AddFormulaMasterCommand {

	private String formulaCode;
	
	private String formulaName;

	private BigDecimal difficultyAtr;
	
	private int startDate;
	
	private int conditionAtr;
	
	private int refMasterNo;
	
	public FormulaMaster toDomainMaster(String companyCode){
		
		FormulaMaster domain = FormulaMaster.createFromJavaType(companyCode, this.formulaCode, this.difficultyAtr, this.formulaName);
		
		return domain;
	}
	
	public FormulaHistory toDomainHis(String companyCode, String historyId){
		
		FormulaHistory domain = FormulaHistory.createFromJavaType(companyCode, this.formulaCode, historyId, this.startDate, 999912);
		
		return domain;
		
	}
	
	
}
