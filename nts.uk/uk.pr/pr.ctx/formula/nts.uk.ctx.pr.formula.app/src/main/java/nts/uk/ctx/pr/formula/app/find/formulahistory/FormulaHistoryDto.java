package nts.uk.ctx.pr.formula.app.find.formulahistory;

import lombok.Value;
import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;

@Value
public class FormulaHistoryDto {
	
	private String companyCode;
	
	private String formulaCode;
	
	private String historyId;
	
	private int startDate;
	
	private int endDate;
	
	public static FormulaHistoryDto fromDomain(FormulaHistory domain){
		return new FormulaHistoryDto(domain.getCompanyCode(), domain.getFormulaCode().v(), domain.getHistoryId() 
				, domain.getStartDate().v(), domain.getEndDate().v());
	}

}
