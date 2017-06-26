/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.service.internal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.service.SocialInsuranceOfficeService;

/**
 * The Class SocialInsuranceOfficeServiceImpl.
 */
@Stateless
public class SocialInsuranceOfficeServiceImpl implements SocialInsuranceOfficeService {

	/** The social insurance office repo. */
	@Inject
	private SocialInsuranceOfficeRepository officeRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.service.
	 * SocialInsuranceOfficeService#validateRequiredItem(nts.uk.ctx.pr.core.dom.
	 * insurance.social.SocialInsuranceOffice)
	 */
	@Override
	public void validateRequiredItem(SocialInsuranceOffice office) {
		if (office.getCode() == null 
				|| office.getName() == null
				|| office.getPicPosition() == null
				|| StringUtil.isNullOrEmpty(office.getCode().v(), true)
				|| StringUtil.isNullOrEmpty(office.getName().v(), true)
				|| StringUtil.isNullOrEmpty(office.getPicPosition().v(), true)) {
			throw new BusinessException("ER001");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.service.
	 * SocialInsuranceOfficeService#checkDuplicateCode(nts.uk.ctx.pr.core.dom.
	 * insurance.social.SocialInsuranceOffice)
	 */
	@Override
	public void checkDuplicateCode(SocialInsuranceOffice office) {
		// Check duplicate code.
		if (officeRepo.isDuplicateCode(office.getCompanyCode(), office.getCode())) {
			throw new BusinessException("ER005");
		}
	}

}
