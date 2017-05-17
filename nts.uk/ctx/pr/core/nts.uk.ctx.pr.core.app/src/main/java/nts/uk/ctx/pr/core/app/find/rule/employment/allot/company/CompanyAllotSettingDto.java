package nts.uk.ctx.pr.core.app.find.rule.employment.allot.company;

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

	public static CompanyAllotSettingDto fromDomain(CompanyAllotSetting domain) {
		return new CompanyAllotSettingDto(domain.getCompanyCode().v(), domain.getHistoryId(), domain.getStartDate().v(),
				domain.getEndDate().v(), domain.getPaymentDetailCode().v(), domain.getBonusDetailCode().v());
	}
}
