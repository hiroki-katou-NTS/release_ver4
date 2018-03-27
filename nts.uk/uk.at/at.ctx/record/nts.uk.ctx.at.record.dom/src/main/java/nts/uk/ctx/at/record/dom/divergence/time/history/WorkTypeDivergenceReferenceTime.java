package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class WorkTypeDivergenceReferenceTime.
 */
// 勤務種別ごとの乖離基準時間
@Getter
@Setter
public class WorkTypeDivergenceReferenceTime extends AggregateRoot {
	/** The divergence time no. */
	// 乖離時間NO
	private Integer divergenceTimeNo;
	
	/** The c id. */
	// 会社ID
	private String cId;
	
	/** The not use atr. */
	// 使用区分
	private NotUseAtr notUseAtr;
	
	/** The work type code. */
	// 勤務種別コード
	private BusinessTypeCode workTypeCode;
	
	/** The history id. */
	// 履歴ID
	private String historyId;
	
	/** The divergence reference time value. */
	// 基準値
	private Optional<DivergenceReferenceTimeValue> divergenceReferenceTimeValue;
	
	/**
	 * Instantiates a new work type divergence reference time.
	 *
	 * @param memento the memento
	 */
	public WorkTypeDivergenceReferenceTime(WorkTypeDivergenceReferenceTimeGetMemento memento) {
		this.divergenceTimeNo = memento.getDivergenceTimeNo();
		this.cId = memento.getCompanyId();
		this.notUseAtr = memento.getNotUseAtr();
		this.workTypeCode = memento.getWorkTypeCode();
		this.historyId = memento.getHistoryId();
		this.divergenceReferenceTimeValue = memento.getDivergenceReferenceTimeValue();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento (WorkTypeDivergenceReferenceTimeSetMemento memento) {
		memento.setDivergenceTimeNo(this.divergenceTimeNo);
		memento.setCompanyId(this.cId);
		memento.setNotUseAtr(this.notUseAtr);
		memento.setWorkTypeCode(this.workTypeCode);
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
		result = prime * result + ((workTypeCode == null) ? 0 : workTypeCode.hashCode());
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
		WorkTypeDivergenceReferenceTime other = (WorkTypeDivergenceReferenceTime) obj;
		if (divergenceTimeNo != other.divergenceTimeNo)
			return false;
		if (workTypeCode == null) {
			if (other.workTypeCode != null)
				return false;
		} else if (!workTypeCode.equals(other.workTypeCode))
			return false;
		if (historyId == null) {
			if (other.historyId != null)
				return false;
		} else if (!historyId.equals(other.historyId))
			return false;
		return true;
	}
}
