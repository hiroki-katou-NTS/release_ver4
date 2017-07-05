/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.employment.statutory.worktime;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class JuuwtstUsageUnitWtSet.
 */

@Setter
@Getter
@Entity
@Table(name = "KUWST_USAGE_UNIT_WT_SET")
public class KuwstUsageUnitWtSet extends UkJpaEntity implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The cid. */
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CID")
    private String cid;
    
    /** The is emp. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_EMP")
    private Integer isEmp;
    
    /** The is wkp. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_WKP")
    private Integer isWkp;
    
    /** The is empt. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_EMPT")
    private Integer isEmpt;

    /**
     * Instantiates a new juuwtst usage unit wt set.
     */
    public KuwstUsageUnitWtSet() {
    }

    /**
     * Instantiates a new juuwtst usage unit wt set.
     *
     * @param cid the cid
     */
    public KuwstUsageUnitWtSet(String cid) {
        this.cid = cid;
    }


    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cid != null ? cid.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
     */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KuwstUsageUnitWtSet)) {
			return false;
		}
		KuwstUsageUnitWtSet other = (KuwstUsageUnitWtSet) object;
		if ((this.cid == null && other.cid != null)
				|| (this.cid != null && !this.cid.equals(other.cid))) {
			return false;
		}
		return true;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "entity.JuuwtstUsageUnitWtSet[ cid=" + cid + " ]";
    }

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.cid;
	}
    
}
