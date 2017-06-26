package rule.employment.processing.yearmonth;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayProcessingRepository;
import nts.uk.ctx.pr.core.pub.rule.employment.processing.yearmonth.IPaydayProcessingPub;
import nts.uk.ctx.pr.core.pub.rule.employment.processing.yearmonth.PaydayProcessingDto;

@Stateless
public class PaydayProcessingPubimp implements IPaydayProcessingPub {
	@Inject
	private PaydayProcessingRepository paydayProcessingRep;

	@Override
	public List<PaydayProcessingDto> getPaydayProcessing(String companyCd) {
		
		return paydayProcessingRep.select3(companyCd).stream()
				.map(m -> new PaydayProcessingDto(m.getCompanyCode().v(), m.getProcessingNo().v(),
						m.getProcessingName().v(), m.getDispSet().value, m.getCurrentProcessingYm().v(),
						m.getBonusAtr().value, m.getBCurrentProcessingYm().v()))
				.collect(Collectors.toList());
	}

}
