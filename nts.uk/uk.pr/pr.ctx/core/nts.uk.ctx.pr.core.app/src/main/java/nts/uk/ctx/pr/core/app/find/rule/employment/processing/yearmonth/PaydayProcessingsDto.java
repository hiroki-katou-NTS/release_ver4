package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import java.util.List;

import lombok.Value;

@Value
public class PaydayProcessingsDto {
	private PaydayProcessingDto paydayProcessingDto;

	private List<PaydayDto> paydayDtos;
	private List<PaydayDto> paydayBonusDtos;

	public static PaydayProcessingsDto fromDomain(PaydayProcessingDto paydayProcessingDto, List<PaydayDto> paydayDtos,
			List<PaydayDto> paydayBonusDtos) {
		return new PaydayProcessingsDto(paydayProcessingDto, paydayDtos, paydayBonusDtos);
	}
}
