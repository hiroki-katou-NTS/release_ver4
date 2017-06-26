package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.paymentdata.BonusDetailCode;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDetailCode;

@Getter
@AllArgsConstructor
public class EmployeeAllSetting extends AggregateRoot{
private CompanyCode companyCode;
	
	private String historyId;
	
	private EmployeeCode employeeCode;
	
	private  EmployeeName employeeName;
	
	private PaymentDetailCode paymentDetailCode;
	
	private PaymentDetailName paymentDetailName;

	private BonusDetailCode bonusDetailCode;
	
	private BonusDetailName bonusDetailName;
	
	public static EmployeeAllSetting createFromJavaType(String companyCode, String historyId, String employeeCode,String employeeName,String paymentDetailCode,String paymentDetailName, String bonusDetailCode, String bonusDetailName){
		return new EmployeeAllSetting(
				new CompanyCode(companyCode), 
				historyId,
				new EmployeeCode(employeeCode),
				new EmployeeName(employeeName),
				new PaymentDetailCode(paymentDetailCode),
				new PaymentDetailName(paymentDetailName),
				new BonusDetailCode(bonusDetailCode),
				new BonusDetailName(bonusDetailName)
				);
	}

}
