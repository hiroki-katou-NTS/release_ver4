/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.overtime;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class KshstOverTimeLangBrdPK.
 */

@Getter
@Setter
@Embeddable
public class KshstOverTimeLangBrdPK implements Serializable {
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The cid. */
	@Basic(optional = false)
    @NotNull
    @Column(name = "CID")
    private String cid;
    
    /** The brd item no. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "BRD_ITEM_NO")
    private short brdItemNo;
    
    /** The language id. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "LANGUAGE_ID")
    private String languageId;

    /**
     * Instantiates a new kshst over time lang brd PK.
     */
    public KshstOverTimeLangBrdPK() {
    }

    /**
     * Instantiates a new kshst over time lang brd PK.
     *
     * @param cid the cid
     * @param brdItemNo the brd item no
     * @param languageId the language id
     */
    public KshstOverTimeLangBrdPK(String cid, short brdItemNo, String languageId) {
        this.cid = cid;
        this.brdItemNo = brdItemNo;
        this.languageId = languageId;
    }


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshstOverTimeLangBrdPK)) {
			return false;
		}
		KshstOverTimeLangBrdPK other = (KshstOverTimeLangBrdPK) object;
		if ((this.cid == null && other.cid != null)
				|| (this.cid != null && !this.cid.equals(other.cid))) {
			return false;
		}
		if (this.brdItemNo != other.brdItemNo) {
			return false;
		}
		if ((this.languageId == null && other.languageId != null)
				|| (this.languageId != null && !this.languageId.equals(other.languageId))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "entity.KshstOverTimeLangBrdPK[ cid=" + cid + ", brdItemNo=" + brdItemNo
				+ ", languageId=" + languageId + " ]";
	}
    
}
