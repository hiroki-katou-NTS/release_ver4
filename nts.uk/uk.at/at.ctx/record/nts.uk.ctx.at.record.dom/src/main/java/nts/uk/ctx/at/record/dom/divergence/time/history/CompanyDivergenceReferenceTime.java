package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.Optional;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class CompanyDivergenceReferenceTime.
 */
// 会社の乖離基準時間
@Getter
public class CompanyDivergenceReferenceTime extends AggregateRoot {
	
	/** The divergence time no. */
	// 乖離時間NO
	private Integer divergenceTimeNo;
	
	/** The c id. */
	// 会社ID
	private String cId;
	
	/** The not use atr. */
	// 使用区分
	private NotUseAtr notUseAtr;
	
	/** The history id. */
	// 履歴ID
	private String historyId;
	
	/** The divergence reference time value. */
	// 基準値
	private Optional<DivergenceReferenceTimeValue> divergenceReferenceTimeValue;
	
	/**
	 * Instantiates a new company divergence reference time.
	 *
	 * @param memento the memento
	 */
	public CompanyDivergenceReferenceTime(CompanyDivergenceReferenceTimeGetMemento memento) {
		this.divergenceTimeNo = memento.getDivergenceTimeNo();
		this.cId = memento.getCompanyId();
		this.notUseAtr = memento.getNotUseAtr();
		this.historyId = memento.getHistoryId();
		this.divergenceReferenceTimeValue = memento.getDivergenceReferenceTimeValue();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(CompanyDivergenceReferenceTimeSetMemento memento) {
		memento.setDivergenceTimeNo(this.divergenceTimeNo);
		memento.setCompanyId(this.cId);
		memento.setNotUseAtr(this.notUseAtr);
		memento.setHistoryId(this.historyId);
		memento.setDivergenceReferenceTimeValue(this.divergenceReferenceTimeValue);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((divergenceTimeNo == null) ? 0 : divergenceTimeNo.hashCode());
		result = prime * result + ((historyId == null) ? 0 : historyId.hashCode());
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
		CompanyDivergenceReferenceTime other = (CompanyDivergenceReferenceTime) obj;
		if (divergenceTimeNo != other.divergenceTimeNo)
			return false;
		if (historyId == null) {
			if (other.historyId != null)
				return false;
		} else if (!historyId.equals(other.historyId))
			return false;
		return true;
	}
}
