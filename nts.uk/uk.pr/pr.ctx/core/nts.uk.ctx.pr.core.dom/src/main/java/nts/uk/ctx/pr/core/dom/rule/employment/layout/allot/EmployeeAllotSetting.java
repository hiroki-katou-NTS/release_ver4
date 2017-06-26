package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.paymentdata.BonusDetailCode;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDetailCode;

@Getter
@AllArgsConstructor
public class EmployeeAllotSetting extends AggregateRoot{
	
	private CompanyCode companyCode;
	
	private String historyId;
	
	private EmployeeCode employeeCode;
	
	private PaymentDetailCode paymentDetailCode;

	private BonusDetailCode bonusDetailCode;
	
	
	
	public static EmployeeAllotSetting createFromJavaType(String companyCode, String historyId, String employeeCode,String paymentDetailCode, String bonusDetailCode) {
		return new EmployeeAllotSetting(
					new CompanyCode(companyCode), 
					historyId,
					new EmployeeCode(employeeCode),
					new PaymentDetailCode(paymentDetailCode),
					new BonusDetailCode(bonusDetailCode));
	}

}
