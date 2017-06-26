/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.pr.core.infra.entity.itemmaster;

import java.io.Serializable;

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
@Table(name = "QCAMT_ITEM_PERIOD")
@Entity
public class QcamtItemPeriod extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    public QcamtItemPeriodPK qcamtItemPeriodPK;
    @Basic(optional = false)
    @Column(name = "PERIOD_ATR")
    public int periodAtr;
    @Basic(optional = false)
    @Column(name = "STR_Y")
    public int strY;
    @Basic(optional = false)
    @Column(name = "END_Y")
    public int endY;
    @Basic(optional = false)
    @Column(name = "CYCLE_ATR")
    public int cycleAtr;
    @Basic(optional = false)
    @Column(name = "CYCLE_01_ATR")
    public int cycle01Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_02_ATR")
    public int cycle02Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_03_ATR")
    public int cycle03Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_04_ATR")
    public int cycle04Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_05_ATR")
    public int cycle05Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_06_ATR")
    public int cycle06Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_07_ATR")
    public int cycle07Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_08_ATR")
    public int cycle08Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_09_ATR")
    public int cycle09Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_10_ATR")
    public int cycle10Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_11_ATR")
    public int cycle11Atr;
    @Basic(optional = false)
    @Column(name = "CYCLE_12_ATR")
    public int cycle12Atr;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qcamtItemPeriodPK != null ? qcamtItemPeriodPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QcamtItemPeriod)) {
            return false;
        }
        QcamtItemPeriod other = (QcamtItemPeriod) object;
        if ((this.qcamtItemPeriodPK == null && other.qcamtItemPeriodPK != null) || (this.qcamtItemPeriodPK != null && !this.qcamtItemPeriodPK.equals(other.qcamtItemPeriodPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.itemmaster.QcamtItemPeriod[ qcamtItemPeriodPK=" + qcamtItemPeriodPK + " ]";
    }
    
    @Override
    protected QcamtItemPeriodPK getKey() {
    	return this.qcamtItemPeriodPK;
    }
    
}
