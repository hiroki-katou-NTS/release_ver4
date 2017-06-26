/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.certification.find.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.wagetable.certification.Certification;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupCode;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupName;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupSetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.certification.MultipleTargetSetting;

/**
 * Instantiates a new certify group find dto.
 */
@Data
public class CertifyGroupFindDto implements CertifyGroupSetMemento {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/** The multi apply set. */
	private Integer multiApplySet;

	/** The certifies. */
	private List<CertificationFindDto> certifies;

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
		this.multiApplySet = multiApplySet.value;
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
		this.certifies = certifies.stream().map(certification -> {
			CertificationFindDto certificationDto = new CertificationFindDto();
			certification.saveToMemento(certificationDto);
			return certificationDto;
		}).collect(Collectors.toList());
	}
}