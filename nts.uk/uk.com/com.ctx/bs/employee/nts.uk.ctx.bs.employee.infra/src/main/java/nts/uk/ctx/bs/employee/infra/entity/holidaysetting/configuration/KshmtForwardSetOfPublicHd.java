/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.bs.employee.infra.entity.holidaysetting.configuration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtForwardSetOfPublicHd.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_FORWARD_SET_OF_PUBLIC_HD")
public class KshmtForwardSetOfPublicHd extends UkJpaEntity implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The exclus ver. */
    @Column(name = "EXCLUS_VER")
    private int exclusVer;
    
    /** The cid. */
    @Id
    @Column(name = "CID")
    private String cid;
    
    /** The is public hd minus. */
    @Column(name = "IS_PUBLIC_HD_MINUS")
    private int isPublicHdMinus;

    /** The carry over deadline. */
    @Column(name = "CARRY_OVER_DEADLINE")
    private int carryOverDeadline;

    /**
     * Instantiates a new kshmt forward set of public hd.
     */
    public KshmtForwardSetOfPublicHd() {
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
        if (!(object instanceof KshmtForwardSetOfPublicHd)) {
            return false;
        }
        KshmtForwardSetOfPublicHd other = (KshmtForwardSetOfPublicHd) object;
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
