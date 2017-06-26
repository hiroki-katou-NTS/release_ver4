/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.pr.core.infra.entity.paymentdata;

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
@Entity
@Table(name = "QPDPT_PAYDAY")
public class QpdptPayday extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    public QpdptPaydayPK qpdptPaydayPK;
   
    @Basic(optional = false)
    @Column(name = "PROCESSING_NO")
    public int processingNo;

    public QpdptPayday() {
    }

    public QpdptPayday(QpdptPaydayPK qpdptPaydayPK) {
        this.qpdptPaydayPK = qpdptPaydayPK;
    }

    public QpdptPayday(QpdptPaydayPK qpdptPaydayPK, int processingNo) {
        this.qpdptPaydayPK = qpdptPaydayPK;
        this.processingNo = processingNo;
    }

    public QpdptPayday(String ccd, String pid) {
        this.qpdptPaydayPK = new QpdptPaydayPK(ccd, pid);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qpdptPaydayPK != null ? qpdptPaydayPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QpdptPayday)) {
            return false;
        }
        QpdptPayday other = (QpdptPayday) object;
        if ((this.qpdptPaydayPK == null && other.qpdptPaydayPK != null) || (this.qpdptPaydayPK != null && !this.qpdptPaydayPK.equals(other.qpdptPaydayPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.QpdptPayday[ qpdptPaydayPK=" + qpdptPaydayPK + " ]";
    }

	@Override
	protected Object getKey() {
		return this.qpdptPaydayPK;
	}
    
}
