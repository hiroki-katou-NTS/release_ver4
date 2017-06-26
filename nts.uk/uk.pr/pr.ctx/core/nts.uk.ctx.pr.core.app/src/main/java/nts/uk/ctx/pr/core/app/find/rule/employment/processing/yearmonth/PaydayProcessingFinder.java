package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayProcessingRepository;

@Stateless
public class PaydayProcessingFinder {

	@Inject
	private PaydayProcessingRepository repository;

	public List<PaydayProcessingDto> select3(String companyCode) {
		return repository.select3(companyCode).stream().map(m -> PaydayProcessingDto.fromDomain(m))
				.collect(Collectors.toList());
	}

	public List<PaydayProcessingSelect4Dto> select4(String companyCode) {
		return repository.select4(companyCode).stream().map(m -> PaydayProcessingSelect4Dto.fromDomain(m))
				.collect(Collectors.toList());
	}
	
	
	

}
