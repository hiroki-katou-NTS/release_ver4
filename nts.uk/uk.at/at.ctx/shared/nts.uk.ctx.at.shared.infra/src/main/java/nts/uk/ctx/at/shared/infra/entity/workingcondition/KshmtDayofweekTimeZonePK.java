/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workingcondition;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class KshmtDayofweekTimeZonePK.
 */
@Getter
@Setter
@Embeddable
public class KshmtDayofweekTimeZonePK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The history id. */
	@Column(name = "HISTORY_ID")
	private String historyId;

	/** The per work day off atr. */
	@Column(name = "PER_WORK_DAY_OFF_ATR")
	private int perWorkDayOffAtr;

	/** The start time. */
	@Column(name = "START_TIME")
	private int startTime;

	/**
	 * Instantiates a new kshmt dayofweek time zone PK.
	 */
	public KshmtDayofweekTimeZonePK() {
		super();
	}

	/**
	 * Instantiates a new kshmt dayofweek time zone PK.
	 *
	 * @param historyId
	 *            the history id
	 * @param perWorkDayOffAtr
	 *            the per work day off atr
	 * @param startTime
	 *            the start time
	 */
	public KshmtDayofweekTimeZonePK(String historyId, int perWorkDayOffAtr, int startTime) {
		super();
		this.historyId = historyId;
		this.perWorkDayOffAtr = perWorkDayOffAtr;
		this.startTime = startTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (historyId != null ? historyId.hashCode() : 0);
		hash += (int) perWorkDayOffAtr;
		hash += (int) startTime;
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtDayofweekTimeZonePK)) {
			return false;
		}
		KshmtDayofweekTimeZonePK other = (KshmtDayofweekTimeZonePK) object;
		if ((this.historyId == null && other.historyId != null)
				|| (this.historyId != null && !this.historyId.equals(other.historyId))) {
			return false;
		}
		if (this.perWorkDayOffAtr != other.perWorkDayOffAtr) {
			return false;
		}
		if (this.startTime != other.startTime) {
			return false;
		}
		return true;
	}

}
