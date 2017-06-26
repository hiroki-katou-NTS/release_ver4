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
@Table(name = "QCAMT_OTHER_ITEM")
@Entity
public class QcamtOtherItem extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    public QcamtOtherItemPK qcamtOtherItemPK;
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
    @Basic(optional = false)
    @Column(name = "ZERO_DISP_SET")
    public int zeroDispSet;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qcamtOtherItemPK != null ? qcamtOtherItemPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QcamtOtherItem)) {
            return false;
        }
        QcamtOtherItem other = (QcamtOtherItem) object;
        if ((this.qcamtOtherItemPK == null && other.qcamtOtherItemPK != null) || (this.qcamtOtherItemPK != null && !this.qcamtOtherItemPK.equals(other.qcamtOtherItemPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.itemmaster.QcamtOtherItem[ qcamtOtherItemPK=" + qcamtOtherItemPK + " ]";
    }
    
    @Override
    protected QcamtOtherItemPK getKey() {
    	return this.qcamtOtherItemPK;
    }
}
