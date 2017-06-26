package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
/**
 * @author Duc
 */
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;

public class SpecificationPosition  extends AggregateRoot{
	
	/** code */
	@Getter
	private CompanyCode companyCode;
	
	/** history Id */
	@Getter
	private String historyId;
	
	/** 職位コード */
	@Getter
	@Setter
	private JobCode jobCode;
	
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
	 * @param version
	 * @param companyCode
	 * @param historyId
	 * @param jobCode
	 * @param bonusStmtCode
	 * @param payStmtCode
	 */
	public SpecificationPosition(CompanyCode companyCode, String historyId, JobCode jobCode,
			BonusStmtCode bonusStmtCode, PayStmtCode payStmtCode) {
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.jobCode = jobCode;
		this.bonusStmtCode = bonusStmtCode;
		this.payStmtCode = payStmtCode;
	}
	/**
	 * create From Java Type
	 * @return SpecificationPosition
	 */
	public static SpecificationPosition createFromJavaType(String companyCode, String historyId, String jobCode,String bonusStmtCode,
			String payStmtCode) {
		if (bonusStmtCode.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		}
		
		return new SpecificationPosition(new CompanyCode(companyCode),
				historyId, 
				new JobCode(jobCode),
				new BonusStmtCode(bonusStmtCode),
				new PayStmtCode(payStmtCode));
	}
	
	
}
