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
public class QcamtItemDeductPK {

    @Basic(optional = false)
    @Column(name = "CCD")
    public String ccd;
    @Basic(optional = false)
    @Column(name = "ITEM_CD")
    public String itemCd;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ccd != null ? ccd.hashCode() : 0);
        hash += (itemCd != null ? itemCd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QcamtItemDeductPK)) {
            return false;
        }
        QcamtItemDeductPK other = (QcamtItemDeductPK) object;
        if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
            return false;
        }
        if ((this.itemCd == null && other.itemCd != null) || (this.itemCd != null && !this.itemCd.equals(other.itemCd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.itemmaster.QcamtItemDeductPK[ ccd=" + ccd + ", itemCd=" + itemCd + " ]";
    }
    
}
