/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.worktime.flexset;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class KshmtFlexHaRestTimePK.
 */
@Getter
@Setter
@Embeddable
public class KshmtFlexHaRestTimePK implements Serializable {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The cid. */
	@Basic(optional = false)
    @Column(name = "CID")
    private String cid;
    
    /** The worktime cd. */
    @Basic(optional = false)
    @Column(name = "WORKTIME_CD")
    private String worktimeCd;

    /**
     * Instantiates a new kshmt flex ha rest time PK.
     */
    public KshmtFlexHaRestTimePK() {
    }

    /**
     * Instantiates a new kshmt flex ha rest time PK.
     *
     * @param cid the cid
     * @param worktimeCd the worktime cd
     */
    public KshmtFlexHaRestTimePK(String cid, String worktimeCd) {
        this.cid = cid;
        this.worktimeCd = worktimeCd;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cid != null ? cid.hashCode() : 0);
        hash += (worktimeCd != null ? worktimeCd.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtFlexHaRestTimePK)) {
			return false;
		}
		KshmtFlexHaRestTimePK other = (KshmtFlexHaRestTimePK) object;
		if ((this.cid == null && other.cid != null) || (this.cid != null && !this.cid.equals(other.cid))) {
			return false;
		}
		if ((this.worktimeCd == null && other.worktimeCd != null)
				|| (this.worktimeCd != null && !this.worktimeCd.equals(other.worktimeCd))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "entity.KshmtFlexHaRestTimePK[ cid=" + cid + ", worktimeCd=" + worktimeCd + " ]";
	}

}
