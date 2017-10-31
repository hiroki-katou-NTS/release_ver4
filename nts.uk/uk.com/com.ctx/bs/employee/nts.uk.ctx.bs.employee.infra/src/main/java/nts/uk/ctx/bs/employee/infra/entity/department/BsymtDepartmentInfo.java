/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.entity.department;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class BsymtDepartmentInfo.
 */
@Entity
@Setter
@Getter
@Table(name = "BSYMT_DEPARTMENT_INFO")
public class BsymtDepartmentInfo extends UkJpaEntity implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The bsymt department info PK. */
    @EmbeddedId
    protected BsymtDepartmentInfoPK bsymtDepartmentInfoPK;
    
    /** The cd. */
    @Column(name = "CD")
    private String cd;
    
    /** The name. */
    @Column(name = "NAME")
    private String name;
    
    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bsymtDepartmentInfoPK != null ? bsymtDepartmentInfoPK.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object object) {
		if (!(object instanceof BsymtDepartmentInfo)) {
			return false;
		}
		BsymtDepartmentInfo other = (BsymtDepartmentInfo) object;
		if ((this.bsymtDepartmentInfoPK == null && other.bsymtDepartmentInfoPK != null)
				|| (this.bsymtDepartmentInfoPK != null
						&& !this.bsymtDepartmentInfoPK.equals(other.bsymtDepartmentInfoPK))) {
			return false;
		}
		return true;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "entity.BsymtDepartmentInfo[ bsymtDepartmentInfoPK=" + bsymtDepartmentInfoPK + " ]";
    }

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.bsymtDepartmentInfoPK;
	}
    
}
