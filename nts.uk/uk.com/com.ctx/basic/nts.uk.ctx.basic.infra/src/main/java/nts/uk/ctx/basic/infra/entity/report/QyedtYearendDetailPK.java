/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.infra.entity.report;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QyedtYearendDetailPK.
 */
@Setter
@Getter
@Embeddable
public class QyedtYearendDetailPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The pid. */
	@Basic(optional = false)
	@Column(name = "PID")
	private String pid;

	/** The year K. */
	@Basic(optional = false)
	@Column(name = "YEAR_K")
	private short yearK;

	/** The ye cnt. */
	@Basic(optional = false)
	@Column(name = "YE_CNT")
	private short yeCnt;

	/** The adj item no. */
	@Basic(optional = false)
	@Column(name = "ADJ_ITEM_NO")
	private short adjItemNo;


	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "entity.QyedtYearendDetailPK[ ccd=" + ccd + ", pid=" + pid + ", yearK=" + yearK + ", yeCnt=" + yeCnt
				+ ", adjItemNo=" + adjItemNo + " ]";
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adjItemNo;
		result = prime * result + ((ccd == null) ? 0 : ccd.hashCode());
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		result = prime * result + yeCnt;
		result = prime * result + yearK;
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
		QyedtYearendDetailPK other = (QyedtYearendDetailPK) obj;
		if (adjItemNo != other.adjItemNo)
			return false;
		if (ccd == null) {
			if (other.ccd != null)
				return false;
		} else if (!ccd.equals(other.ccd))
			return false;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		if (yeCnt != other.yeCnt)
			return false;
		if (yearK != other.yearK)
			return false;
		return true;
	}

}
