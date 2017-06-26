/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.pr.core.infra.entity.itemmaster;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author chinhbv
 */
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QcamtItemBreakdownPK {

    @Basic(optional = false)
    @Column(name = "CCD")
    public String ccd;
    @Basic(optional = false)
    @Column(name = "CTG_ATR")
    public int ctgAtr;
    @Basic(optional = false)
    @Column(name = "ITEM_CD")
    public String itemCd;
    @Basic(optional = false)
    @Column(name = "ITEM_BREAKDOWN_CD")
    public String itemBreakdownCd;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ccd != null ? ccd.hashCode() : 0);
        hash += (int) ctgAtr;
        hash += (itemCd != null ? itemCd.hashCode() : 0);
        hash += (itemBreakdownCd != null ? itemBreakdownCd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QcamtItemBreakdownPK)) {
            return false;
        }
        QcamtItemBreakdownPK other = (QcamtItemBreakdownPK) object;
        if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
            return false;
        }
        if (this.ctgAtr != other.ctgAtr) {
            return false;
        }
        if ((this.itemCd == null && other.itemCd != null) || (this.itemCd != null && !this.itemCd.equals(other.itemCd))) {
            return false;
        }
        if ((this.itemBreakdownCd == null && other.itemBreakdownCd != null) || (this.itemBreakdownCd != null && !this.itemBreakdownCd.equals(other.itemBreakdownCd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.itemmaster.QcamtItemBreakdownPK[ ccd=" + ccd + ", ctgAtr=" + ctgAtr + ", itemCd=" + itemCd + ", itemBreakdownCd=" + itemBreakdownCd + " ]";
    }
    
}
