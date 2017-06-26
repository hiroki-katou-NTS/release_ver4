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
 * The Class PcpmtPersonComPK.
 */
@Embeddable
@Setter
@Getter
public class PcpmtPersonComPK implements Serializable {

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

	/** The hist id. */
	@Basic(optional = false)
	@Column(name = "HIST_ID")
	private String histId;

	/**
	 * Instantiates a new pcpmt person com PK.
	 */
	public PcpmtPersonComPK() {
	}

	/**
	 * Instantiates a new pcpmt person com PK.
	 *
	 * @param ccd the ccd
	 * @param pid the pid
	 * @param histId the hist id
	 */
	public PcpmtPersonComPK(String ccd, String pid, String histId) {
		this.ccd = ccd;
		this.pid = pid;
		this.histId = histId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccd == null) ? 0 : ccd.hashCode());
		result = prime * result + ((histId == null) ? 0 : histId.hashCode());
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
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
		PcpmtPersonComPK other = (PcpmtPersonComPK) obj;
		if (ccd == null) {
			if (other.ccd != null)
				return false;
		} else if (!ccd.equals(other.ccd))
			return false;
		if (histId == null) {
			if (other.histId != null)
				return false;
		} else if (!histId.equals(other.histId))
			return false;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		return true;
	}

}
