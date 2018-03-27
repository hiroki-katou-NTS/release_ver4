package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.history.strategic.UnduplicatableHistory;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class WorkTypeDivergenceReferenceTimeHistory.
 */
// 勤務種別ごとの乖離基準時間履歴
@Getter
@Setter
public class WorkTypeDivergenceReferenceTimeHistory extends AggregateRoot implements UnduplicatableHistory<DateHistoryItem, DatePeriod, GeneralDate>{
	
	/** The company id. */
	// 会社ID
	private String cId;
	
	/** The work type code. */
	// 勤務種別コード
	private BusinessTypeCode workTypeCode;

	/** The history item. */
	// 履歴項目
	private List<DateHistoryItem> historyItems = new ArrayList<>();
	
	/**
	 * Instantiates a new work type divergence reference time history.
	 *
	 * @param memento the memento
	 */
	public WorkTypeDivergenceReferenceTimeHistory(WorkTypeDivergenceReferenceTimeHistoryGetMemento memento) {
		this.cId = memento.getCompanyId();
		this.workTypeCode = memento.getWorkTypeCode();
		this.historyItems = memento.getHistoryItems();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WorkTypeDivergenceReferenceTimeHistorySetMemento memento) {
		memento.setCompanyId(this.cId);
		memento.setWorkTypeCode(this.workTypeCode);
		memento.setHistoryItems(this.historyItems);
	}

	/**
	 * Items.
	 *
	 * @return the list
	 */
	@Override
	public List<DateHistoryItem> items() {
		return this.historyItems;
	}
	
	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cId == null) ? 0 : cId.hashCode());
		result = prime * result + ((workTypeCode == null) ? 0 : workTypeCode.hashCode());
		result = prime * result + ((historyItems.isEmpty()) ? 0 : historyItems.hashCode());
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkTypeDivergenceReferenceTimeHistory other = (WorkTypeDivergenceReferenceTimeHistory) obj;
		if (cId == null) {
			if (other.cId != null)
				return false;
		} else if (!cId.equals(other.cId))
			return false;
		if (workTypeCode == null) {
			if (other.workTypeCode != null)
				return false;
		} else if (!workTypeCode.equals(other.workTypeCode))
			return false;
		if (!historyItems.equals(other.historyItems))
			return false;
		return true;
	}
}
