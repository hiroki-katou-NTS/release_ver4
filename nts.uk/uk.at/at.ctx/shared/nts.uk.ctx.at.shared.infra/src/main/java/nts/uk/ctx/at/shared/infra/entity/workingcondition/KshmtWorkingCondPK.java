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
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtWorkingCondPK.
 */
@Getter
@Setter
@Embeddable
public class KshmtWorkingCondPK extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The sid. */
	@Column(name = "SID")
	private String sid;

	/** The history id. */
	@Column(name = "HISTORY_ID")
	private String historyId;

	/**
	 * Instantiates a new kshmt working cond PK.
	 */
	public KshmtWorkingCondPK() {
		super();
	}
	
	public KshmtWorkingCondPK(String sid, String historyId) {
		 this.sid = sid;
	     this.historyId = historyId;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (sid != null ? sid.hashCode() : 0);
		hash += (historyId != null ? historyId.hashCode() : 0);
		return hash;
	}

	/**
	 * Equals.
	 *
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtWorkingCondPK)) {
			return false;
		}
		KshmtWorkingCondPK other = (KshmtWorkingCondPK) object;
		if ((this.sid == null && other.sid != null)
				|| (this.sid != null && !this.sid.equals(other.sid))) {
			return false;
		}
		if ((this.historyId == null && other.historyId != null)
				|| (this.historyId != null && !this.historyId.equals(other.historyId))) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	@Override
	protected Object getKey() {
		return this.historyId;
	}

}
