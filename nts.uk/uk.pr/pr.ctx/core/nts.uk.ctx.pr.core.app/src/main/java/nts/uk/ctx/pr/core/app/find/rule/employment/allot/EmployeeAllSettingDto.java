package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.EmployeeAllSetting;

@Value
public class EmployeeAllSettingDto {
	String companyCode;
	String historyId;
	String employeeCode;
	String employeeName;
	String paymentDetailCode;
	String paymentDatailName;
	String bonusDetailCode;
	String bonusDetailName;
	
	public static EmployeeAllSettingDto fromDomain(EmployeeAllSetting employeeAllSetting){
		return new EmployeeAllSettingDto(employeeAllSetting.getCompanyCode().v(),
				employeeAllSetting.getHistoryId(),
				employeeAllSetting.getEmployeeCode().v(),
				employeeAllSetting.getEmployeeName().v(),
				employeeAllSetting.getPaymentDetailCode().v(),
				employeeAllSetting.getPaymentDetailName().v(),
				employeeAllSetting.getBonusDetailCode().v(),
				employeeAllSetting.getEmployeeName().v());
		
	}
}
