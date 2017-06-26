package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
/**
 * 
 * @author Duc
 *
 */
public class SpecificationPerson extends AggregateRoot{
	/** code */
	@Getter
	private CompanyCode companyCode;
	
	/** history Id */
	@Getter
	private String historyId;
	
	/** 開始年月 */
	@Getter
	@Setter
	private YearMonth endYm;
	
	/** 賞与明細No */
	@Getter
	@Setter
	private BonusStmtCode bonusStmtCode ;
	
	/** 給与明細No */
	@Getter
	@Setter
	private PayStmtCode payStmtCode ;

	/**
	 * 
	 * @param companyCode
	 * @param historyId
	 * @param endYm
	 * @param bonusStmtCode
	 * @param payStmtCode
	 */
	public SpecificationPerson(CompanyCode companyCode, String historyId, YearMonth endYm, BonusStmtCode bonusStmtCode,
			PayStmtCode payStmtCode) {
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.endYm = endYm;
		this.bonusStmtCode = bonusStmtCode;
		this.payStmtCode = payStmtCode;
	}
	/**
	 * create From Java Type
	 * @return SpecificationPerson
	 */
	public static SpecificationPerson createFromJavaType(String companyCode, String historyId, Integer endYm,String bonusStmtCode,
			String payStmtCode) {
		if (bonusStmtCode.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		}
		
		return new SpecificationPerson(new CompanyCode(companyCode),
				historyId, 
				new YearMonth(endYm),
				new BonusStmtCode(bonusStmtCode),
				new PayStmtCode(payStmtCode));
	}
}
