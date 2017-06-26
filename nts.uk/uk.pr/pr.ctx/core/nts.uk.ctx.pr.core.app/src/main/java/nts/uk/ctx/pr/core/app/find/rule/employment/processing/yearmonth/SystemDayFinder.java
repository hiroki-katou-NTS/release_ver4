package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.SystemDayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.systemday.SystemDay;

@Stateless
public class SystemDayFinder {

	@Inject
	private SystemDayRepository repository;

	public SystemDayDto select1(String companyCode, int processingNo) {
		SystemDay domain = repository.select1(companyCode, processingNo);
		
		if (domain != null) {
			return SystemDayDto.fromDomain(domain);
		} else {
			return null;
		}
	}
}
