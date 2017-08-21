/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.dailypattern;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.query.DBCharPaddingAs;
import nts.uk.ctx.at.schedule.dom.shift.pattern.daily.PatternCode;

/**
 * The Class KdpstDailyPatternSetPK.
 */

/**
 * Sets the pattern cd.
 *
 * @param patternCd the new pattern cd
 */
@Setter
@Getter
@Embeddable
public class KdpstDailyPatternSetPK implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The cid. */
    @Basic(optional = false)
    @Column(name = "CID")
	private String cid;
	
    /** The cid. */
    @DBCharPaddingAs(PatternCode.class)
    @Basic(optional = false)
    @Column(name = "PATTERN_CD")
	private String patternCd;
	
    /**
     * Instantiates a new kcsmt work type PK.
     */
    public KdpstDailyPatternSetPK(){
    }

	/**
	 * Instantiates a new kcsmt work type PK.
	 *
	 * @param cid the cid
	 * @param patternCd the pattern cd
	 */
	public KdpstDailyPatternSetPK(String cid, String patternCd) {
		this.cid = cid;
		this.patternCd = patternCd;
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cid != null ? cid.hashCode() : 0);
        hash += (patternCd != null ? patternCd.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KdpstDailyPatternSetPK)) {
            return false;
        }
        KdpstDailyPatternSetPK other = (KdpstDailyPatternSetPK) object;
        if ((this.cid == null && other.cid != null) || (this.cid != null && !this.cid.equals(other.cid))) {
            return false;
        }
        if ((this.patternCd == null && other.patternCd != null) || (this.patternCd != null
                && !this.patternCd.equals(other.patternCd))) {
            return false;
        }
        return true;
    }
}
