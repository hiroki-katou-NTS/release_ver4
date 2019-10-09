package nts.uk.ctx.at.function.ac.divergencetime;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.DivergenceTimeAdapter;
import nts.uk.ctx.at.function.dom.adapter.DivergenceTimeAdapterDto;
import nts.uk.ctx.at.record.pub.divergencetime.DivergenceTimePub;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DivergenceTimeAcFinder implements DivergenceTimeAdapter{
	
	@Inject
	private DivergenceTimePub divergenceTimePub;

	@Override
	public List<DivergenceTimeAdapterDto> getDivergenceTimeName(String companyId, List<Integer> divTimeIds) {
		return this.divergenceTimePub.getDivergenceTimeName(companyId, divTimeIds).stream().map(f -> {
			return new DivergenceTimeAdapterDto(f.getCompanyId(), f.getDivTimeId(), f.getDivTimeName());
		}).collect(Collectors.toList());
	}

}
