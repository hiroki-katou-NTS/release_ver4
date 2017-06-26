/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.entity.insurance;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QismtChecklistPrintSet.
 */
@Setter
@Getter
@Entity
@Table(name = "QISMT_CHECKLIST_PRINT_SET")
public class QismtChecklistPrintSet extends UkJpaEntity implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The is show breakdown item. */
    @Basic(optional = false)
    @Column(name = "IS_SHOW_BREAKDOWN_ITEM")
    private short isShowBreakdownItem;
    
    /** The ccd. */
    @Id
    @Basic(optional = false)
    @Column(name = "CCD")
    private String ccd;
    
    /** The is show person sum mny. */
    @Basic(optional = false)
    @Column(name = "IS_SHOW_PERSON_SUM_MNY")
    private short isShowPersonSumMny;
    
    /** The is show office sum mny. */
    @Basic(optional = false)
    @Column(name = "IS_SHOW_OFFICE_SUM_MNY")
    private short isShowOfficeSumMny;
    
    /** The is show total mny col. */
    @Basic(optional = false)
    @Column(name = "IS_SHOW_TOTAL_MNY_COL")
    private short isShowTotalMnyCol;
    
    /** The is show total pay mny. */
    @Basic(optional = false)
    @Column(name = "IS_SHOW_TOTAL_PAY_MNY")
    private short isShowTotalPayMny;
    
    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ccd != null ? ccd.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QismtChecklistPrintSet)) {
            return false;
        }
        QismtChecklistPrintSet other = (QismtChecklistPrintSet) object;
        if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "entity.QismtChecklistPrintSet[ ccd=" + ccd + " ]";
    }

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.ccd;
	}
    
}
