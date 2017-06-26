/******************************************************************
// * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.certification.find.dto;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationSetMemento;

/**
 * Instantiates a new certification find in dto.
 */
@Data
public class CertificationFindInDto implements CertificationSetMemento {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationSetMemento#
	 * setCompanyCode(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationSetMemento#
	 * setCode(java.lang.String)
	 */
	@Override
	public void setCode(String code) {
		this.code = code;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationSetMemento#
	 * setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

}
