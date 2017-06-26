/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.service.internal;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroup;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupRepository;
import nts.uk.ctx.pr.core.dom.wagetable.certification.service.CertifyGroupService;

/**
 * The Class CertifyGroupServiceImpl.
 */
@Stateless
public class CertifyGroupServiceImpl implements CertifyGroupService {

	/** The repository. */
	@Inject
	private CertifyGroupRepository repository;

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.wagetable.certification.service.
	 * CertifyGroupService#validateRequiredItem(nts.uk.ctx.pr.core.dom.wagetable
	 * .certification.CertifyGroup)
	 */
	@Override
	public void validateRequiredItem(CertifyGroup certifyGroup) {
		// Validate required item
		if (certifyGroup.getCode() == null 
				|| certifyGroup.getName() == null
				|| StringUtil.isNullOrEmpty(certifyGroup.getCode().v(), true)
				|| StringUtil.isNullOrEmpty(certifyGroup.getName().v(), true)) {
			throw new BusinessException("ER001");
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.wagetable.certification.service.
	 * CertifyGroupService#checkDuplicateCode(nts.uk.ctx.pr.core.dom.wagetable.
	 * certification.CertifyGroup)
	 */
	@Override
	public void checkDuplicateCode(CertifyGroup certifyGroup) {

		// find data
		Optional<CertifyGroup> data = this.repository.findById(certifyGroup.getCompanyCode(),
			certifyGroup.getCode().v());

		// check exist
		if (data.isPresent()) {
			throw new BusinessException("ER005");
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.wagetable.certification.service.
	 * CertifyGroupService#checkDulicateCertification(nts.uk.ctx.pr.core.dom.
	 * wagetable.certification.CertifyGroup, java.lang.String)
	 */
	@Override
	public void checkCertificationIsBelong(CertifyGroup certifyGroup) {
		// Check certification is belong to exist group.
		if (certifyGroup.getCertifies().stream()
			.anyMatch(item -> this.repository.isBelongToExistGroup(certifyGroup.getCompanyCode(),
				certifyGroup.getCode().v(), item.getCode()))) {
			throw new BusinessException("ER005");
		}
	}

}
