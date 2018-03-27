package nts.uk.ctx.at.record.infra.repository.divergence.time.history;

import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryGetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.history.KrcstWorktypeDrtHist;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaWorkTypeDivergenceReferenceTimeHistoryGetMemento.
 */
public class JpaWorkTypeDivergenceReferenceTimeHistoryGetMemento
		implements WorkTypeDivergenceReferenceTimeHistoryGetMemento {
	
	/** The entities. */
	private List<KrcstWorktypeDrtHist> entities;
	
	/**
	 * Instantiates a new jpa work type divergence reference time history get memento.
	 */
	public JpaWorkTypeDivergenceReferenceTimeHistoryGetMemento() {}
	
	/**
	 * Instantiates a new jpa work type divergence reference time history get memento.
	 *
	 * @param entities the entities
	 */
	public JpaWorkTypeDivergenceReferenceTimeHistoryGetMemento(List<KrcstWorktypeDrtHist> entities) {
		this.entities = entities;
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return AppContexts.user().companyId();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryGetMemento#getWorkTypeCode()
	 */
	@Override
	public BusinessTypeCode getWorkTypeCode() {
		return this.entities.isEmpty() ? null : new BusinessTypeCode(this.entities.get(0).getWorktypeCd());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryGetMemento#getHistoryItems()
	 */
	@Override
	public List<DateHistoryItem> getHistoryItems() {
		return entities.stream().map(item -> {
			DateHistoryItem dateHistoryItem = new DateHistoryItem(item.getHistId(),
					new DatePeriod(item.getStrD(), item.getEndD()));
			return dateHistoryItem;
		}).collect(Collectors.toList());
	}

}
