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
@Table(name = "QCAMT_ITEM")
@Entity
public class QcamtItem extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    public QcamtItemPK qcamtItemPK;
    
    @Basic(optional = false)
    @Column(name = "FIX_ATR")
    public int fixAtr;
    @Basic(optional = false)
    @Column(name = "ITEM_NAME")
    public String itemName;
    @Basic(optional = false)
    @Column(name = "ITEM_AB_NAME")
    public String itemAbName;
    @Column(name = "ITEM_AB_NAME_E")
    public String itemAbNameE;
    @Column(name = "ITEM_AB_NAME_O")
    public String itemAbNameO;
    @Basic(optional = false)
    @Column(name = "DISP_SET")
    public int dispSet;
    @Column(name = "UNITE_CD")
    public String uniteCd;
    @Column(name = "ZERO_DISP_SET")
    public int zeroDispSet;
    @Column(name = "ITEM_DISP_ATR")
    public int itemDispAtr;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qcamtItemPK != null ? qcamtItemPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QcamtItem)) {
            return false;
        }
        QcamtItem other = (QcamtItem) object;
        if ((this.qcamtItemPK == null && other.qcamtItemPK != null) || (this.qcamtItemPK != null && !this.qcamtItemPK.equals(other.qcamtItemPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.itemmaster.QcamtItem[ qcamtItemPK=" + qcamtItemPK + " ]";
    }
    
    @Override
    protected QcamtItemPK getKey() {
    	return this.qcamtItemPK;
    }
}
