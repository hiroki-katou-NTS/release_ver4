package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;

public class SpecificationPayClassification extends AggregateRoot{
	/** code */
	@Getter
	private CompanyCode companyCode;
	
	/** history Id */
	@Getter
	private String historyId;
	
	/** 職位コード */
	@Getter
	@Setter
	private PayClassCode payClassCode;
	
	/** 賞与明細No */
	@Getter
	@Setter
	private BonusStmtCode bonusStmtCode ;
	
	/** 給与明細No */
	@Getter
	@Setter
	private PayStmtCode payStmtCode ;

	public SpecificationPayClassification(CompanyCode companyCode, String historyId, PayClassCode payClassCode,
			BonusStmtCode bonusStmtCode, PayStmtCode payStmtCode) {
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.payClassCode = payClassCode;
		this.bonusStmtCode = bonusStmtCode;
		this.payStmtCode = payStmtCode;
	}
	
	/**
	 * create From Java Type
	 * @return SpecificationPayClassification
	 */
	public static SpecificationPayClassification createFromJavaType(String companyCode, String historyId, String payClassCode,String bonusStmtCode,
			String payStmtCode) {
		if (bonusStmtCode.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		}
		
		return new SpecificationPayClassification(new CompanyCode(companyCode),
				historyId, 
				new PayClassCode(payClassCode),
				new BonusStmtCode(bonusStmtCode),
				new PayStmtCode(payStmtCode));
	}
}
