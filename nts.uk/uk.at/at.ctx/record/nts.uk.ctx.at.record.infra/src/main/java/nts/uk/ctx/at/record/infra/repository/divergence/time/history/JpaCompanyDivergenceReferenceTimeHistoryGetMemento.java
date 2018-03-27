package nts.uk.ctx.at.record.infra.repository.divergence.time.history;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistoryGetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.history.KrcstComDrtHist;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaCompanyDivergenceReferenceTimeHistoryGetMemento.
 */
public class JpaCompanyDivergenceReferenceTimeHistoryGetMemento
		implements CompanyDivergenceReferenceTimeHistoryGetMemento {

	/** The entities. */
	private List<KrcstComDrtHist> entities = new ArrayList<>();

	/**
	 * Instantiates a new jpa company divergence reference time history get memento.
	 */
	public JpaCompanyDivergenceReferenceTimeHistoryGetMemento() {
	}

	/**
	 * Instantiates a new jpa company divergence reference time history get memento.
	 *
	 * @param entities
	 *            the entities
	 */
	public JpaCompanyDivergenceReferenceTimeHistoryGetMemento(List<KrcstComDrtHist> entities) {
		this.entities = entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeHistoryGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return AppContexts.user().companyId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeHistoryGetMemento#getHistoryItems()
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
