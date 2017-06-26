package nts.uk.ctx.pr.core.pub.rule.employment.processing.yearmonth;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class PaydayProcessingDto {

	String companyCode;

	int processingNo;

	String processingName;

	int dispSet;

	int currentProcessingYm;

	int bonusAtr;

	int bCurrentProcessingYm;
}
