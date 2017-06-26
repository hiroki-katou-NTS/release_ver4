package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;
@Value
public class CompanyAllotSalaryNameDto {
	String companyCode;
	String historyId;
	int startDate;
	int endDate;
	String bonusDetailCode;
	String paymentDetailCode;
	
	public static CompanyAllotSettingDto fromDomain(CompanyAllotSetting companyAllotSetting) {
		return new CompanyAllotSettingDto(
			companyAllotSetting.getCompanyCode().v(),
			companyAllotSetting.getHistoryId(),
			companyAllotSetting.getStartDate().v(),
			companyAllotSetting.getEndDate().v(),
			companyAllotSetting.getBonusDetailCode().v(),
			companyAllotSetting.getPaymentDetailCode().v());
	}
}
