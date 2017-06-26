/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class ReportCmnmtEmpPK.
 */
@Setter
@Getter
@Embeddable
public class ReportCmnmtEmpPK implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2057071023975099159L;
	
	/** The company code. */
	@NotNull
	@Column(name = "CCD")
	public String companyCode;
	
	/** The employment code. */
	@NotNull
	@Column(name = "EMPCD")
	public String employmentCode;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
		result = prime * result + ((employmentCode == null) ? 0 : employmentCode.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportCmnmtEmpPK other = (ReportCmnmtEmpPK) obj;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		if (employmentCode == null) {
			if (other.employmentCode != null)
				return false;
		} else if (!employmentCode.equals(other.employmentCode))
			return false;
		return true;
	}
	
	
}
