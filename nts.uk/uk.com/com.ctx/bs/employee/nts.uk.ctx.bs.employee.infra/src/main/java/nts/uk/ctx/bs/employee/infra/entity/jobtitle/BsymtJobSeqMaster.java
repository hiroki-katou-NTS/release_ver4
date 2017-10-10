/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.bs.employee.infra.entity.jobtitle;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class BsymtJobSeqMaster.
 *
 * @author NWS_THANHNC_PC
 */
@Getter
@Setter
@Entity
@Table(name = "BSYMT_JOB_SEQ_MASTER")
public class BsymtJobSeqMaster extends UkJpaEntity implements Serializable {
	
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The bsymt job seq master PK. */
    @EmbeddedId
    protected BsymtJobSeqMasterPK bsymtJobSeqMasterPK;
    
    /** The seq name. */
    @Column(name = "SEQ_NAME")
    private String seqName;
   
    /** The disporder. */
    @Column(name = "DISPORDER")
    private short disporder;

    /**
     * Instantiates a new bsymt job seq master.
     */
    public BsymtJobSeqMaster() {
    }

    /**
     * Instantiates a new bsymt job seq master.
     *
     * @param bsymtJobSeqMasterPK the bsymt job seq master PK
     */
    public BsymtJobSeqMaster(BsymtJobSeqMasterPK bsymtJobSeqMasterPK) {
        this.bsymtJobSeqMasterPK = bsymtJobSeqMasterPK;
    }
   
    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bsymtJobSeqMasterPK != null ? bsymtJobSeqMasterPK.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BsymtJobSeqMaster)) {
            return false;
        }
        BsymtJobSeqMaster other = (BsymtJobSeqMaster) object;
        if ((this.bsymtJobSeqMasterPK == null && other.bsymtJobSeqMasterPK != null) || (this.bsymtJobSeqMasterPK != null && !this.bsymtJobSeqMasterPK.equals(other.bsymtJobSeqMasterPK))) {
            return false;
        }
        return true;
    }

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.bsymtJobSeqMasterPK;
	}
    
}
