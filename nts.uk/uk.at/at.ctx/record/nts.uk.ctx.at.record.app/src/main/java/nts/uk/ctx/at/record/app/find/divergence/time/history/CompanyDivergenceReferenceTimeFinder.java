package nts.uk.ctx.at.record.app.find.divergence.time.history;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeRepository;

/**
 * The Class CompanyDivergenceReferenceTimeFinder.
 */
@Stateless
public class CompanyDivergenceReferenceTimeFinder {
	
	/** The com divergence ref time repository. */
	@Inject
	private CompanyDivergenceReferenceTimeRepository comDivergenceRefTimeRepository;
	
	/**
	 * Gets the divergence reference time item by hist.
	 *
	 * @param historyId the history id
	 * @return the divergence reference time item by hist
	 */
	public List<CompanyDivergenceReferenceTimeDto> getDivergenceReferenceTimeItemByHist(String historyId){
		
		List<CompanyDivergenceReferenceTime> listDomain = this.comDivergenceRefTimeRepository.findAll(historyId);
		
		if (listDomain.isEmpty()){
			return Collections.emptyList();
		}
		
		return listDomain.stream().map(e -> {
			CompanyDivergenceReferenceTimeDto dto = new CompanyDivergenceReferenceTimeDto();
			e.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}
}
