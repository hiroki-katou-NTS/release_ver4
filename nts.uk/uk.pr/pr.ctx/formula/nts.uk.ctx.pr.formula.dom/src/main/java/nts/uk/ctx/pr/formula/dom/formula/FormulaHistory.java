package nts.uk.ctx.pr.formula.dom.formula;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;

@Getter
public class FormulaHistory extends AggregateRoot{

	private String companyCode;

	private FormulaCode formulaCode;

	private String historyId;
	
	private YearMonth startDate;
	
	private YearMonth endDate;

	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 */
	public FormulaHistory(String companyCode, FormulaCode formulaCode, String historyId) {
		super();
		this.companyCode = companyCode;
		this.formulaCode = formulaCode;
		this.historyId = historyId;
	}

	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 * @param startDate
	 * @param endDate
	 */
	public FormulaHistory(String companyCode, FormulaCode formulaCode, String historyId, YearMonth startDate,
			YearMonth endDate) {
		super();
		this.companyCode = companyCode;
		this.formulaCode = formulaCode;
		this.historyId = historyId;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public void validate(){
		super.validate();

		if (StringUtil.isNullOrEmpty(startDate.toString(), true)) {
			throw new BusinessException("ER001");
		}
	}
	
	public static FormulaHistory createFromJavaType(String companyCode, String formulaCode, String historyId, int startDate, int endDate){
		return new FormulaHistory(companyCode, new FormulaCode(formulaCode),
				historyId, new YearMonth(startDate), new YearMonth(endDate));
	}
	
}
