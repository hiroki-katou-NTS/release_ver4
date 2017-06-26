/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.output;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author chinhbv
 */
@Embeddable
public class QcpmtRegalDocComPK {

    @Basic(optional = false)
    @Column(name = "CCD")
    public String ccd;
    @Basic(optional = false)
    @Column(name = "REGAL_DOC_CCD")
    public String regalDocCcd;

    public QcpmtRegalDocComPK() {
    }

    public QcpmtRegalDocComPK(String ccd, String regalDocCcd) {
        this.ccd = ccd;
        this.regalDocCcd = regalDocCcd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ccd != null ? ccd.hashCode() : 0);
        hash += (regalDocCcd != null ? regalDocCcd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QcpmtRegalDocComPK)) {
            return false;
        }
        QcpmtRegalDocComPK other = (QcpmtRegalDocComPK) object;
        if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
            return false;
        }
        if ((this.regalDocCcd == null && other.regalDocCcd != null) || (this.regalDocCcd != null && !this.regalDocCcd.equals(other.regalDocCcd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.QcpmtRegalDocComPK[ ccd=" + ccd + ", regalDocCcd=" + regalDocCcd + " ]";
    }
    
}
