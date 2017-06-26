/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.certification.find.dto;

import java.util.Set;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.wagetable.certification.Certification;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupCode;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupName;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupSetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.certification.MultipleTargetSetting;

/**
 * Instantiates a new certify group find out dto.
 */
@Data
public class CertifyGroupFindOutDto implements CertifyGroupSetMemento {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupSetMemento#
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
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupSetMemento#
	 * setCode(nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupCode)
	 */
	@Override
	public void setCode(CertifyGroupCode code) {
		this.code = code.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupSetMemento#
	 * setName(nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupName)
	 */
	@Override
	public void setName(CertifyGroupName name) {
		this.name = name.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupSetMemento#
	 * setMultiApplySet(nts.uk.ctx.pr.core.dom.wagetable.certification.
	 * MultipleTargetSetting)
	 */
	@Override
	public void setMultiApplySet(MultipleTargetSetting multiApplySet) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupSetMemento#
	 * setCertifies(java.util.Set)
	 */
	@Override
	public void setCertifies(Set<Certification> certifies) {
		// Do nothing.
	}

}
