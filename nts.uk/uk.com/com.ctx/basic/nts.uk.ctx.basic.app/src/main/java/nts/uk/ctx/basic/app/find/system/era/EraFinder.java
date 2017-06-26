package nts.uk.ctx.basic.app.find.system.era;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.system.era.EraRepository;
import nts.uk.shr.com.time.japanese.JapaneseEraName;
import nts.uk.shr.com.time.japanese.JapaneseErasProvider;

@Stateless
public class EraFinder {
	@Inject
	private EraRepository eraRepository;

	public List<EraDto> getEras() {
		List<EraDto> result = this.eraRepository.getEras().stream().map(x -> {
			return EraDto.fromDomain(x);
		}).collect(Collectors.toList());

		return result;
	}

	// public Optional<EraDto> getEraDetail(GeneralDate startDate){
	// return this.eraRepository.getEraDetail(startDate).map(era ->
	// EraDto.fromDomain(era));
	// }
	public Optional<EraDto> getEraDetail(String eraHist) {
		return this.eraRepository.getEraDetail(eraHist).map(era -> EraDto.fromDomain(era));
	}

	public int getFixAtribute(String eraHist) {
		Optional<EraDto> eraDto = this.eraRepository.getEraDetail(eraHist).map(era -> EraDto.fromDomain(era));
		if (eraDto.isPresent()) {
			return eraDto.get().getFixAttribute();
		}
		throw new BusinessException("not found");
	}

	public boolean checkStartDate(GeneralDate startDate) {
		if (this.eraRepository.getStartDateEraMaster(startDate).isEmpty()) {
			return false;
		}		
		return true;
	}
}
