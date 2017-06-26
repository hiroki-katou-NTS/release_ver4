package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;

/** Finder DTO of Company Allot Setting */
@Value
public class CompanyAllotSettingDto {
	
	String companyCode;
	String historyId;
	int startDate;
	int endDate;
	String paymentDetailCode;
	String bonusDetailCode;
	
	
	public static CompanyAllotSettingDto fromDomain(CompanyAllotSetting companyAllotSetting) {
		return new CompanyAllotSettingDto(
			companyAllotSetting.getCompanyCode().v(),
			companyAllotSetting.getHistoryId(),
			companyAllotSetting.getStartDate().v(),
			companyAllotSetting.getEndDate().v(),
			companyAllotSetting.getPaymentDetailCode().v(),
			companyAllotSetting.getBonusDetailCode().v()
			);
	}
}
