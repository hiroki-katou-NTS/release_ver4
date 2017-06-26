package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.standardday.StandardDay;

@Value
public class StandardDayDto {

	String companyCode;

	int processingNo;

	int socialInsStdYearAtr;

	int socialInsStdMon;

	int socialInsStdDay;

	int incometaxStdYearAtr;

	int incometaxStdMon;

	int incometaxStdDay;

	int empInsStdMon;

	int empInsStdDay;

	public static StandardDayDto fromDomain(StandardDay domain) {
		return new StandardDayDto(domain.getCompanyCode().v(), domain.getProcessingNo().v(),
				domain.getSocialInsStdYearAtr().value, domain.getSocialInsStdMon().v(), domain.getSocialInsStdDay().v(),
				domain.getIncometaxStdYearAtr().value, domain.getIncometaxStdMon().v(), domain.getIncometaxStdDay().v(),
				domain.getEmpInsStdMon().v(), domain.getEmpInsStdDay().v());
	}

}
