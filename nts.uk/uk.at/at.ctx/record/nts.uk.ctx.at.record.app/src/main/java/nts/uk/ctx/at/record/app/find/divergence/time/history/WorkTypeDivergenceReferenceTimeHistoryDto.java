package nts.uk.ctx.at.record.app.find.divergence.time.history;

import lombok.Data;
import nts.arc.time.GeneralDate;

/**
 * The Class WorkTypeDivergenceReferenceTimeHistoryDto.
 */
@Data
public class WorkTypeDivergenceReferenceTimeHistoryDto {
	/** The history id. */
	private String historyId;
	
	/** The start date. */
	private GeneralDate startDate;
	
	/** The end date. */
	private GeneralDate endDate;
	
	/**
	 * Instantiates a new work type divergence reference time history dto.
	 *
	 * @param historyId the history id
	 * @param startDate the start date
	 * @param endDate the end date
	 */
	public WorkTypeDivergenceReferenceTimeHistoryDto (String historyId, GeneralDate startDate, GeneralDate endDate){
		this.historyId = historyId;
		this.startDate = startDate;
		this.endDate = endDate;
	}
}
