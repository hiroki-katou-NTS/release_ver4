/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class DeductionTime.
 */
@Getter
@NoArgsConstructor
// 控除時間帯(丸め付き)
public class DeductionTime extends TimeZone {
	
	/**
	 * Instantiates a new deduction time.
	 *
	 * @param start the start
	 * @param end the end
	 */
	public DeductionTime(TimeWithDayAttr start, TimeWithDayAttr end) {
		super(start, end);
	}
	
	/**
	 * Instantiates a new deduction time.
	 *
	 * @param memento the memento
	 */
	public DeductionTime(DeductionTimeGetMemento memento) {		
		super(memento.getStart(), memento.getEnd());
		this.start = memento.getStart();
		this.end = memento.getEnd();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(DeductionTimeSetMemento memento){
		memento.setStart(this.start);
		memento.setEnd(this.end);
	}
	
	/**
	 * Restore data.
	 *
	 * @param oldDomain the old domain
	 */
	public void restoreData(DeductionTime oldDomain) {
		this.start = oldDomain.getStart();
		this.end = oldDomain.getEnd();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.start.v() + "," + this.end.v();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		DeductionTime other = (DeductionTime) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}
	
	@Override
	public DeductionTime clone() {
		DeductionTime cloned = new DeductionTime();
		try {
			cloned.start = new TimeWithDayAttr(this.start.valueAsMinutes());
			cloned.end = new TimeWithDayAttr(this.end.valueAsMinutes());
		}
		catch (Exception e){
			throw new RuntimeException("DeductionTime clone error.");
		}
		return cloned;
	}

}
