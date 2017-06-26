package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.paydayprocessing.PaydayProcessing;

@Value
public class PaydayProcessingSelect4Dto {
	String companyCode;

	int processingNo;

	String processingName;

	public static PaydayProcessingSelect4Dto fromDomain(PaydayProcessing domain) {
		return new PaydayProcessingSelect4Dto(domain.getCompanyCode().v(), domain.getProcessingNo().v(),
				domain.getProcessingName().v());
	}
}
