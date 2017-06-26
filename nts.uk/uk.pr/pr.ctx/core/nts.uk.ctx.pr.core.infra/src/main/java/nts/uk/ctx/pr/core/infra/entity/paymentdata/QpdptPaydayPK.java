/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.pr.core.infra.entity.paymentdata;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author chinhbv
 */
@Embeddable
public class QpdptPaydayPK {

    @Basic(optional = false)
    @Column(name = "CCD")
    public String ccd;
    @Basic(optional = false)
    @Column(name = "PID")
    public String pid;

    public QpdptPaydayPK() {
    }

    public QpdptPaydayPK(String ccd, String pid) {
        this.ccd = ccd;
        this.pid = pid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ccd != null ? ccd.hashCode() : 0);
        hash += (pid != null ? pid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QpdptPaydayPK)) {
            return false;
        }
        QpdptPaydayPK other = (QpdptPaydayPK) object;
        if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
            return false;
        }
        if ((this.pid == null && other.pid != null) || (this.pid != null && !this.pid.equals(other.pid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.QpdptPaydayPK[ ccd=" + ccd + ", pid=" + pid + " ]";
    }
    
}
