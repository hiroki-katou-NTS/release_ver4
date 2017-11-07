/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.shortworktime;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class BshmtSchildCareFramePK.
 */
@Getter
@Setter
@Embeddable
public class BshmtSchildCareFramePK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The sid. */
	@Basic(optional = false)
	@Column(name = "SID")
	private String sid;

	/** The hist id. */
	@Basic(optional = false)
	@Column(name = "HIST_ID")
	private String histId;

	/** The str clock. */
	@Basic(optional = false)
	@Column(name = "STR_CLOCK")
	private Integer strClock;

	/**
	 * Instantiates a new bshmt schild care frame PK.
	 */
	public BshmtSchildCareFramePK() {
	}

	/**
	 * Instantiates a new bshmt schild care frame PK.
	 *
	 * @param sid
	 *            the sid
	 * @param histId
	 *            the hist id
	 * @param strClock
	 *            the str clock
	 */
	public BshmtSchildCareFramePK(String sid, String histId, Integer strClock) {
		this.sid = sid;
		this.histId = histId;
		this.strClock = strClock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (sid != null ? sid.hashCode() : 0);
		hash += (histId != null ? histId.hashCode() : 0);
		hash += (int) strClock;
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof BshmtSchildCareFramePK)) {
			return false;
		}
		BshmtSchildCareFramePK other = (BshmtSchildCareFramePK) object;
		if ((this.sid == null && other.sid != null) || (this.sid != null && !this.sid.equals(other.sid))) {
			return false;
		}
		if ((this.histId == null && other.histId != null)
				|| (this.histId != null && !this.histId.equals(other.histId))) {
			return false;
		}
		if (this.strClock != other.strClock) {
			return false;
		}
		return true;
	}

}
