package nts.uk.ctx.at.record.infra.repository.divergence.time.history;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistorySetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.history.KrcstWorktypeDrtHist;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * The Class JpaWorkTypeDivergenceReferenceTimeHistorySetMemento.
 */
public class JpaWorkTypeDivergenceReferenceTimeHistorySetMemento
		implements WorkTypeDivergenceReferenceTimeHistorySetMemento {

	/** The entities. */
	private List<KrcstWorktypeDrtHist> entities;

	/**
	 * Instantiates a new jpa work type divergence reference time history get
	 * memento.
	 */
	public JpaWorkTypeDivergenceReferenceTimeHistorySetMemento() {
	}

	/**
	 * Instantiates a new jpa work type divergence reference time history get
	 * memento.
	 *
	 * @param entities
	 *            the entities
	 */
	public JpaWorkTypeDivergenceReferenceTimeHistorySetMemento(List<KrcstWorktypeDrtHist> entities) {
		this.entities = entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeHistorySetMemento#setCompanyId(java.lang.
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
	 * WorkTypeDivergenceReferenceTimeHistorySetMemento#setWorkTypeCode(nts.uk.ctx.
	 * at.shared.dom.worktype.WorkTypeCode)
	 */
	@Override
	public void setWorkTypeCode(BusinessTypeCode workTypeCode) {
		if (this.entities.isEmpty()) {
			KrcstWorktypeDrtHist worktypeDrtHist = new KrcstWorktypeDrtHist();
			worktypeDrtHist.setWorktypeCd(workTypeCode.v());

			this.entities.add(worktypeDrtHist);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeHistorySetMemento#setHistoryItems(java.util.
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
			KrcstWorktypeDrtHist worktypeDrtHist = new KrcstWorktypeDrtHist();
			worktypeDrtHist.setHistId(k);
			worktypeDrtHist.setWorktypeCd(this.entities.get(0).getWorktypeCd());
			worktypeDrtHist.setCid(AppContexts.user().companyId());
			worktypeDrtHist.setStrD(v.start());
			worktypeDrtHist.setEndD(v.end());

			// add to entities
			this.entities.add(worktypeDrtHist);
		});

		// remove item that's history ID is empty
		this.entities.removeIf(item -> StringUtils.isEmpty(item.getHistId()));
	}
}
