/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.worktime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class KshmtFlexStampReflectPK.
 */
@Getter
@Setter
@Embeddable
public class KshmtFlexStampReflectPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cid. */
	@Column(name = "CID")
	private String cid;

	/** The worktime cd. */
	@Column(name = "WORKTIME_CD")
	private String worktimeCd;

	/** The work no. */
	@Column(name = "WORK_NO")
	private Integer workNo;

	/**
	 * Instantiates a new kshmt flex stamp reflect PK.
	 */
	public KshmtFlexStampReflectPK() {
		super();
	}
	
	/**
	 * Instantiates a new kshmt flex stamp reflect PK.
	 *
	 * @param cid the cid
	 * @param worktimeCd the worktime cd
	 */
	public KshmtFlexStampReflectPK(String cid, String worktimeCd) {
		super();
		this.cid = cid;
		this.worktimeCd = worktimeCd;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
		result = prime * result + ((workNo == null) ? 0 : workNo.hashCode());
		result = prime * result + ((worktimeCd == null) ? 0 : worktimeCd.hashCode());
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
		KshmtFlexStampReflectPK other = (KshmtFlexStampReflectPK) obj;
		if (cid == null) {
			if (other.cid != null)
				return false;
		} else if (!cid.equals(other.cid))
			return false;
		if (workNo == null) {
			if (other.workNo != null)
				return false;
		} else if (!workNo.equals(other.workNo))
			return false;
		if (worktimeCd == null) {
			if (other.worktimeCd != null)
				return false;
		} else if (!worktimeCd.equals(other.worktimeCd))
			return false;
		return true;
	}


}
