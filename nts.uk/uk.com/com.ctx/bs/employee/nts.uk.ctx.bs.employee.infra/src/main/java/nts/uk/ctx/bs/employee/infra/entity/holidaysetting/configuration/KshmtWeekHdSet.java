/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.bs.employee.infra.entity.holidaysetting.configuration;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtWeekHdSet.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_WEEK_HD_SET")
public class KshmtWeekHdSet extends UkJpaEntity implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The exclus ver. */
    @Column(name = "EXCLUS_VER")
    private int exclusVer;
   
    /** The cid. */
    @Id
    @Column(name = "CID")
    private String cid;
   
    /** The in legal hd. */
    @Column(name = "IN_LEGAL_HD")
    private BigDecimal inLegalHd;
   
    /** The out legal hd. */
    @Column(name = "OUT_LEGAL_HD")
    private BigDecimal outLegalHd;
   
    /** The start day. */
    @Column(name = "START_DAY")
    private int startDay;

    /**
     * Instantiates a new kshmt week hd set.
     */
    public KshmtWeekHdSet() {
    	super();
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
        if (!(object instanceof KshmtWeekHdSet)) {
            return false;
        }
        KshmtWeekHdSet other = (KshmtWeekHdSet) object;
        if ((this.cid == null && other.cid != null) || (this.cid != null && !this.cid.equals(other.cid))) {
            return false;
        }
        return true;
    }

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.cid;
	}
    
}
