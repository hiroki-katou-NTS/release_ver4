package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.StandardDayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.standardday.StandardDay;

@Stateless
public class StandardDayFinder {

	@Inject
	private StandardDayRepository repository;

	public StandardDayDto select1(String companyCode, int processingNo) {
		StandardDay domain = repository.select1(companyCode, processingNo);
		if (domain != null) {
			return StandardDayDto.fromDomain(domain);
		} else {
			return null;
		}
	}
}
