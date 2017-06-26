package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.EmployeeAllotSettingHeader;

@Value
public class EmployeeAllotSettingHeaderDto {
	String companyCode;
	int startYm;
	int endYm;
	String historyId;
	
	public static EmployeeAllotSettingHeaderDto fromDomain(EmployeeAllotSettingHeader employeeAllotSettingHeader) {
		return new EmployeeAllotSettingHeaderDto(
				employeeAllotSettingHeader.getCompanyCode(),
				employeeAllotSettingHeader.getStartYm().v(),
				employeeAllotSettingHeader.getEndYm().v(),
				employeeAllotSettingHeader.getHistoryId());
	}
}
