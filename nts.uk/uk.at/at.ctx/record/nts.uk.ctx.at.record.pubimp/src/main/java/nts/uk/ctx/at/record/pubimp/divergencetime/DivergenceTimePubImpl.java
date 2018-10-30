package nts.uk.ctx.at.record.pubimp.divergencetime;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository;
import nts.uk.ctx.at.record.pub.divergencetime.DivergenceTimePub;
import nts.uk.ctx.at.record.pub.divergencetime.DivergenceTimePubDto;

@Stateless
public class DivergenceTimePubImpl implements DivergenceTimePub{
	
	@Inject
	private DivergenceTimeRepository divergenceTimeRepository;

	@Override
	public List<DivergenceTimePubDto> getDivergenceTimeName(String companyId, List<Integer> divTimeIds) {
		List<DivergenceTimePubDto> divergenceTimes = this.divergenceTimeRepository.getUsedDivTimeListByNoV2(companyId, divTimeIds).stream().map(f -> {
			return new DivergenceTimePubDto(f.getCompanyId(), f.getDivergenceTimeNo(), f.getDivTimeName().v());
		}).collect(Collectors.toList());
		return divergenceTimes;
	}
}
