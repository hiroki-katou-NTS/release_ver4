package nts.uk.ctx.at.record.app.find.divergence.time.history;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistory;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistoryRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class CompanyDivergenceReferenceTimeHistoryFinder.
 */
@Stateless
public class CompanyDivergenceReferenceTimeHistoryFinder {
	
	/** The com divergence ref time hist repoitory. */
	@Inject
	private CompanyDivergenceReferenceTimeHistoryRepository comDivergenceRefTimeHistRepoitory;
	
	/**
	 * Gets the all histories.
	 *
	 * @return the all histories
	 */
	public List<CompanyDivergenceReferenceTimeHistoryDto> getAllHistories() {
		String companyId = AppContexts.user().companyId();
		
		CompanyDivergenceReferenceTimeHistory domain = this.comDivergenceRefTimeHistRepoitory.findAll(companyId);
		
		if (!domain.getHistoryItems().isEmpty()){
			return domain.getHistoryItems().stream().map(o -> {
				CompanyDivergenceReferenceTimeHistoryDto dto = new CompanyDivergenceReferenceTimeHistoryDto(o.identifier(), o.start(), o.end());
				return dto;
			}).collect(Collectors.toList());
		}
		
		return null;
	}
	
	/**
	 * Gets the history.
	 *
	 * @param historyId the history id
	 * @return the history
	 */
	public CompanyDivergenceReferenceTimeHistoryDto getHistory(String historyId) {
//		String companyId = AppContexts.user().companyId();
		CompanyDivergenceReferenceTimeHistory domain = this.comDivergenceRefTimeHistRepoitory.findByHistId(historyId);
		
		CompanyDivergenceReferenceTimeHistoryDto dto = new CompanyDivergenceReferenceTimeHistoryDto(domain.getHistoryItems().get(0).identifier(), 
																				domain.getHistoryItems().get(0).start(), 
																				domain.getHistoryItems().get(0).end());
		return dto;
	}
}
