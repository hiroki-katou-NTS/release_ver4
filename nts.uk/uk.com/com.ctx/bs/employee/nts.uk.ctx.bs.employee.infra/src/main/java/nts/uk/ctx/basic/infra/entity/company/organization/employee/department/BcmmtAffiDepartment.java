/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.infra.entity.company.organization.employee.department;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class BcmmtAffiDepartment.
 */
@Getter
@Setter
@Entity
@Table(name = "BCMMT_AFFI_DEPARTMENT")
public class BcmmtAffiDepartment extends UkJpaEntity {

	/** The id. */
	@Id
	@Column(name = "ID")
	private String id;

	/** The sid. */
	@Column(name = "SID")
	private String sid;

	/** The dep id. */
	@Column(name = "DEP_ID")
	private String depId;

	/** The str D. */
	@Column(name = "STR_D")
	@Convert(converter = GeneralDate.class)
	private GeneralDate strD;

	/** The end D. */
	@Column(name = "END_D")
	@Convert(converter = GeneralDate.class)
	private GeneralDate endD;
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof BcmmtAffiDepartment)) {
			return false;
		}
		BcmmtAffiDepartment other = (BcmmtAffiDepartment) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.id;
	}

}
