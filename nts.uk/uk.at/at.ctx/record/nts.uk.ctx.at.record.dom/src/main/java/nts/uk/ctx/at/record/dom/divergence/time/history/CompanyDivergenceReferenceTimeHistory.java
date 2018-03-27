package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.history.strategic.UnduplicatableHistory;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class CompanyDivergenceReferenceTimeHistory.
 */
// 会社の乖離基準時間履歴
@Getter
public class CompanyDivergenceReferenceTimeHistory extends AggregateRoot implements UnduplicatableHistory<DateHistoryItem, DatePeriod, GeneralDate>{
	
	/** The company id. */
	// 会社ID
	private String cId;

	/** The history item. */
	// 履歴項目
	private List<DateHistoryItem> historyItems = new ArrayList<>();
	
	/**
	 * Instantiates a new company divergence reference time history.
	 *
	 * @param memento the memento
	 */
	public CompanyDivergenceReferenceTimeHistory(CompanyDivergenceReferenceTimeHistoryGetMemento memento){
		this.cId = memento.getCompanyId();
		this.historyItems = memento.getHistoryItems();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(CompanyDivergenceReferenceTimeHistorySetMemento memento){
		memento.setCompanyId(this.cId);
		memento.setHistoryItems(this.historyItems);
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.shr.com.history.History#items()
	 */
	@Override
	public List<DateHistoryItem> items() {
		return this.historyItems;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cId == null) ? 0 : cId.hashCode());
		result = prime * result + ((historyItems == null) ? 0 : historyItems.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyDivergenceReferenceTimeHistory other = (CompanyDivergenceReferenceTimeHistory) obj;
		if (cId == null) {
			if (other.cId != null)
				return false;
		} else if (!cId.equals(other.cId))
			return false;
		if (!historyItems.equals(other.historyItems))
			return false;
		return true;
	}
}
