/**
 * 
 */
package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;

public class SpecificationDepartment extends AggregateRoot {
	
	/** code */
	@Getter
	private CompanyCode companyCode;
	
	/** history Id */
	@Getter
	private String historyId;
	
	/** department Code */
	@Getter
	private DepartmentCode departmentCode;
	
	/** 賞与明細No */
	
	@Getter
	@Setter
	private BonusStmtCode bonusStmtCode ;
	
	/** 給与明細No */
	@Getter
	@Setter
	private PayStmtCode payStmtCode ;

	public SpecificationDepartment(CompanyCode companyCode, String historyId,DepartmentCode departmentCode, BonusStmtCode bonusStmtCode,
			PayStmtCode payStmtCode) {
		super();
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.bonusStmtCode = bonusStmtCode;
		this.payStmtCode = payStmtCode;
	}
	/**
	 * create From Java Type
	 * @return SpecificationDepartment
	 */
	public static SpecificationDepartment createFromJavaType(String companyCode, String historyId,String departmentCode,String bonusStmtCode,
			String payStmtCode) {
		if (bonusStmtCode.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		}
		
		return new SpecificationDepartment(new CompanyCode(companyCode),
				historyId, 
				new DepartmentCode(departmentCode),
				new BonusStmtCode(bonusStmtCode),
				new PayStmtCode(payStmtCode));
	}
}
