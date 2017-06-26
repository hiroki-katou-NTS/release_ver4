package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayRepository;

@Stateless
public class PaydayFinder {

	@Inject
	private PaydayRepository repository;

	public List<PaydayDto> select4(String companyCode, int processingNo) {
		return repository.select4(companyCode, processingNo).stream().map(m -> PaydayDto.fromDomain(m))
				.collect(Collectors.toList());
	}

	public List<PaydayDto> select6(String companyCode, int processingNo, int processingYm) {
		return repository.select6(companyCode, processingNo, processingYm).stream().map(m -> PaydayDto.fromDomain(m))
				.collect(Collectors.toList());
	}

	public List<PaydayDto> select12(String companyCode, int payBonusAtr, int sparePayAtr) {
		return repository.select12(companyCode, payBonusAtr, sparePayAtr).stream().map(m -> PaydayDto.fromDomain(m))
				.collect(Collectors.toList());
	}
}
