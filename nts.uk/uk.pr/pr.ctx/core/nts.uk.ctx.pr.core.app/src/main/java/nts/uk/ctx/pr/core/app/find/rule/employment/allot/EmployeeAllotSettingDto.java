package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.EmployeeAllotSetting;

@Value
public class EmployeeAllotSettingDto {
	String companyCode;
	String historyId;
	String employeeCode;
	String bonusDetailCode;
	String paymentDetailCode;
	
	public static EmployeeAllotSettingDto fromDomain(EmployeeAllotSetting employeeAllotSetting) {
		return new EmployeeAllotSettingDto(
				employeeAllotSetting.getCompanyCode().v(),
				employeeAllotSetting.getHistoryId(),
				employeeAllotSetting.getEmployeeCode().v(),
				employeeAllotSetting.getPaymentDetailCode().v(),
				employeeAllotSetting.getBonusDetailCode().v());
	}
}