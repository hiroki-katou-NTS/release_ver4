/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.outsideot.breakdown.attendance;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * The Class KshmtOutsideAtd.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_OUTSIDE_ATD")
public class KshmtOutsideAtd extends ContractUkJpaEntity implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected KshstOutsideOtBrdAtenPK kshstOutsideOtBrdAtenPK;

    /**
     * Instantiates a new kshst over time brd aten.
     */
    public KshmtOutsideAtd() {
    	super();
    }

    /**
     * Instantiates a new kshst over time brd aten.
     *
     * @param kshstOverTimeBrdAtenPK the kshst over time brd aten PK
     */
    public KshmtOutsideAtd(KshstOutsideOtBrdAtenPK kshstOverTimeBrdAtenPK) {
        this.kshstOutsideOtBrdAtenPK = kshstOverTimeBrdAtenPK;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kshstOutsideOtBrdAtenPK != null ? kshstOutsideOtBrdAtenPK.hashCode() : 0);
        return hash;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtOutsideAtd)) {
			return false;
		}
		KshmtOutsideAtd other = (KshmtOutsideAtd) object;
		if ((this.kshstOutsideOtBrdAtenPK == null && other.kshstOutsideOtBrdAtenPK != null)
				|| (this.kshstOutsideOtBrdAtenPK != null
						&& !this.kshstOutsideOtBrdAtenPK.equals(other.kshstOutsideOtBrdAtenPK))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.kshstOutsideOtBrdAtenPK;
	}

}
