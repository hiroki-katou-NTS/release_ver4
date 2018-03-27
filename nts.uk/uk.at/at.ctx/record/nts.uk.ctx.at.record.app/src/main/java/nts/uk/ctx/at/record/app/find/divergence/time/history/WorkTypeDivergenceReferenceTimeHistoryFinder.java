package nts.uk.ctx.at.record.app.find.divergence.time.history;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistory;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WorkTypeDivergenceReferenceTimeHistoryFinder.
 */
@Stateless
public class WorkTypeDivergenceReferenceTimeHistoryFinder {

	/** The com divergence ref time hist repoitory. */
	@Inject
	private WorkTypeDivergenceReferenceTimeHistoryRepository histRepoitory;

	/**
	 * Gets the all histories.
	 *
	 * @param workTypeCode
	 *            the work type code
	 * @return the all histories
	 */
	public List<WorkTypeDivergenceReferenceTimeHistoryDto> getAllHistories(String workTypeCode) {
		String companyId = AppContexts.user().companyId();

		WorkTypeDivergenceReferenceTimeHistory domain = this.histRepoitory.findAll(companyId,
				new BusinessTypeCode(workTypeCode));

		if (!domain.getHistoryItems().isEmpty()) {
			return domain.getHistoryItems().stream().map(o -> {
				WorkTypeDivergenceReferenceTimeHistoryDto dto = new WorkTypeDivergenceReferenceTimeHistoryDto(
						o.identifier(), o.start(), o.end());
				return dto;
			}).collect(Collectors.toList());
		}

		return null;
	}

	/**
	 * Gets the history.
	 *
	 * @param historyId
	 *            the history id
	 * @return the history
	 */
	public WorkTypeDivergenceReferenceTimeHistoryDto getHistory(String historyId) {

		WorkTypeDivergenceReferenceTimeHistory domain = this.histRepoitory.findByKey(historyId);

		WorkTypeDivergenceReferenceTimeHistoryDto dto = new WorkTypeDivergenceReferenceTimeHistoryDto(
				domain.getHistoryItems().get(0).identifier(), domain.getHistoryItems().get(0).start(),
				domain.getHistoryItems().get(0).end());
		return dto;
	}
}
