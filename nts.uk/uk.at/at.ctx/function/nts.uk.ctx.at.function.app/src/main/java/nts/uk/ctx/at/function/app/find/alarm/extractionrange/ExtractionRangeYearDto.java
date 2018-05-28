package nts.uk.ctx.at.function.app.find.alarm.extractionrange;

import lombok.Value;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.year.AYear;

@Value
public class ExtractionRangeYearDto {

	private String extractionId;

	private int extractionRange;

	private int year;

	private int thisYear;

	public static ExtractionRangeYearDto fromDomain(AYear domain) {
		return new ExtractionRangeYearDto(domain.getExtractionId(), domain.getExtractionRange().value, domain.getYear(),
				domain.isToBeThisYear() ? 1 : 0);
	}

}
