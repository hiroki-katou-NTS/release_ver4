package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayProcessingRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.find.employment.processing.yearmonth.IPaydayProcessingFinder;
import nts.uk.shr.find.employment.processing.yearmonth.PaydayProcessingDto;

@Stateless
public class PaydayProcessingFinderShr implements IPaydayProcessingFinder {
	@Inject
	private PaydayProcessingRepository repository;

	@Override
	public List<PaydayProcessingDto> getPaydayProcessing(String companyCode) {
		if (companyCode == null || companyCode.trim() == "")
			companyCode = AppContexts.user().companyCode();

		return repository.select3(companyCode).stream()
				.map(c -> new PaydayProcessingDto(c.getCompanyCode().v(), c.getProcessingNo().v(),
						c.getProcessingName().v(), c.getDispSet().value, c.getCurrentProcessingYm().v(),
						c.getBonusAtr().value, c.getBCurrentProcessingYm().v()))
				.collect(Collectors.toList());
	}

	@Override
	public List<PaydayProcessingDto> getPaydayProcessing() {
		 String	companyCode = AppContexts.user().companyCode();

		return repository.select1(companyCode).stream()
				.map(c -> new PaydayProcessingDto(c.getCompanyCode().v(), c.getProcessingNo().v(),
						c.getProcessingName().v(), c.getDispSet().value, c.getCurrentProcessingYm().v(),
						c.getBonusAtr().value, c.getBCurrentProcessingYm().v()))
				.collect(Collectors.toList());
	}
}
