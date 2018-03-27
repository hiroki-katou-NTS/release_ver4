package nts.uk.ctx.at.record.app.find.divergence.time.history;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeRepository;

/**
 * The Class WorkTypeDivergenceReferenceTimeFinder.
 */
@Stateless
public class WorkTypeDivergenceReferenceTimeFinder {
	/** The com divergence ref time repository. */
	@Inject
	private WorkTypeDivergenceReferenceTimeRepository itemRepository;
	
	/**
	 * Gets the divergence reference time item by hist.
	 *
	 * @param historyId the history id
	 * @param workTypeCode the work type code
	 * @return the divergence reference time item by hist
	 */
	public List<WorkTypeDivergenceReferenceTimeDto> getDivergenceReferenceTimeItemByHist(String historyId, String workTypeCode){
		
		List<WorkTypeDivergenceReferenceTime> listDomain = this.itemRepository.findAll(historyId, new BusinessTypeCode(workTypeCode));
		
		if (listDomain.isEmpty()){
			return Collections.emptyList();
		}
		
		return listDomain.stream().map(e -> {
			WorkTypeDivergenceReferenceTimeDto dto = new WorkTypeDivergenceReferenceTimeDto();
			e.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}
}
