package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.systemday.SystemDay;

@Value
public class SystemDayDto {
	String companyCode;

	int processingNo;

	int socialInsLevyMonAtr;

	int resitaxStdMon;

	int resitaxStdDay;

	int resitaxBeginMon;

	int pickupStdMonAtr;

	int pickupStdDay;

	int payStdDay;

	int accountDueMonAtr;

	int accountDueDay;

	int payslipPrintMonthAtr;

	public static SystemDayDto fromDomain(SystemDay domain) {
		return new SystemDayDto(domain.getCompanyCode().v(), domain.getProcessingNo().v(),
				domain.getSocialInsLevyMonAtr().value, domain.getResitaxStdMon().v(), domain.getResitaxStdDay().v(),
				domain.getResitaxBeginMon().v(), domain.getPickupStdMonAtr().value, domain.getPickupStdDay().v(),
				domain.getPayStdDay().v(), domain.getAccountDueMonAtr().value, domain.getAccountDueDay().v(),
				domain.getPayslipPrintMonthAtr().value);
	}
}
