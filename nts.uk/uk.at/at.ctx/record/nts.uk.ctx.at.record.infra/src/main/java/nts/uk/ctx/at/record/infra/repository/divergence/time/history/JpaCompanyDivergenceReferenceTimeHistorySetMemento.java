package nts.uk.ctx.at.record.infra.repository.divergence.time.history;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistorySetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.history.KrcstComDrtHist;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * The Class JpaCompanyDivergenceReferenceTimeHistorySetMemento.
 */
public class JpaCompanyDivergenceReferenceTimeHistorySetMemento
		implements CompanyDivergenceReferenceTimeHistorySetMemento {

	/** The entities. */
	private List<KrcstComDrtHist> entities;

	/**
	 * Instantiates a new jpa company divergence reference time history set memento.
	 */
	public JpaCompanyDivergenceReferenceTimeHistorySetMemento() {
	}

	/**
	 * Instantiates a new jpa company divergence reference time history set memento.
	 *
	 * @param entities
	 *            the entities
	 */
	public JpaCompanyDivergenceReferenceTimeHistorySetMemento(List<KrcstComDrtHist> entities) {
		this.entities = entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeHistorySetMemento#setCompanyId(java.lang.
	 * String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		// No coding.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeHistorySetMemento#setHistoryItems(java.util.
	 * List)
	 */
	@Override
	public void setHistoryItems(List<DateHistoryItem> historyItems) {

		// Create history map
		Map<String, DateHistoryItem> historyMap = historyItems.stream()
				.collect(Collectors.toMap(DateHistoryItem::identifier, item -> item));

		// If history is exist in DB
		this.entities.forEach(entity -> {
			if (historyMap.containsKey(entity.getHistId())) {
				DateHistoryItem historyItem = historyMap.get(entity.getHistId());
				entity.setStrD(historyItem.start());
				entity.setEndD(historyItem.end());

				historyMap.remove(entity.getHistId());
			}
		});

		// If history is not exist in DB, create new
		historyMap.forEach((k, v) -> {
			KrcstComDrtHist comDrtHist = new KrcstComDrtHist();
			comDrtHist.setHistId(k);
			comDrtHist.setCid(AppContexts.user().companyId());
			comDrtHist.setStrD(v.start());
			comDrtHist.setEndD(v.end());

			// add to entities
			this.entities.add(comDrtHist);
		});
	}
}
