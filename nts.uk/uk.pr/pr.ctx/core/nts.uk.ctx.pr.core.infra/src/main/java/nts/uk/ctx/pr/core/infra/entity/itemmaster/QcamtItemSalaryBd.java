/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.pr.core.infra.entity.itemmaster;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 *
 * @author chinhbv
 */
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QCAMT_ITEM_SALARY_BD")
@Entity
public class QcamtItemSalaryBd extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    public QcamtItemSalaryBdPK qcamtItemSalaryBdPK;
    
    @Column(name = "ITEM_BREAKDOWN_NAME")
    public String itemBreakdownName;
    @Column(name = "ITEM_BREAKDOWN_AB_NAME")
    public String itemBreakdownAbName;
    @Column(name = "UNITE_CD")
    public String uniteCd;
    @Basic(optional = false)
    @Column(name = "ZERO_DISP_SET")
    public int zeroDispSet;
    @Basic(optional = false)
    @Column(name = "ITEM_DISP_ATR")
    public int itemDispAtr;
    @Basic(optional = false)
    @Column(name = "ERR_RANGE_LOW_ATR")
    public int errRangeLowAtr;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "ERR_RANGE_LOW")
    public BigDecimal errRangeLow;
    @Basic(optional = false)
    @Column(name = "ERR_RANGE_HIGH_ATR")
    public int errRangeHighAtr;
    @Basic(optional = false)
    @Column(name = "ERR_RANGE_HIGH")
    public BigDecimal errRangeHigh;
    @Basic(optional = false)
    @Column(name = "AL_RANGE_LOW_ATR")
    public int alRangeLowAtr;
    @Basic(optional = false)
    @Column(name = "AL_RANGE_LOW")
    public BigDecimal alRangeLow;
    @Basic(optional = false)
    @Column(name = "AL_RANGE_HIGH_ATR")
    public int alRangeHighAtr;
    @Basic(optional = false)
    @Column(name = "AL_RANGE_HIGH")
    public BigDecimal alRangeHigh;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qcamtItemSalaryBdPK != null ? qcamtItemSalaryBdPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QcamtItemSalaryBd)) {
            return false;
        }
        QcamtItemSalaryBd other = (QcamtItemSalaryBd) object;
        if ((this.qcamtItemSalaryBdPK == null && other.qcamtItemSalaryBdPK != null) || (this.qcamtItemSalaryBdPK != null && !this.qcamtItemSalaryBdPK.equals(other.qcamtItemSalaryBdPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.itemmaster.QcamtItemSalaryBd[ qcamtItemSalaryBdPK=" + qcamtItemSalaryBdPK + " ]";
    }
    
    @Override
    protected QcamtItemSalaryBdPK getKey() {
    	return this.qcamtItemSalaryBdPK;
    }
}
