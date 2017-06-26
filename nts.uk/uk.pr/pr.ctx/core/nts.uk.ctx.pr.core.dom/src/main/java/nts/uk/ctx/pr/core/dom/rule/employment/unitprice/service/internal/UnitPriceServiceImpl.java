/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.internal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.UnitPriceService;

/**
 * The Class UnitPriceServiceImpl.
 */
@Stateless
public class UnitPriceServiceImpl implements UnitPriceService {

	/** The unit price repo. */
	@Inject
	private UnitPriceRepository unitPriceRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.UnitPriceService
	 * #validateRequiredItem(nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPrice)
	 */
	@Override
	public void validateRequiredItem(UnitPrice unitPrice) {
		if (unitPrice.getCode() == null || StringUtil.isNullOrEmpty(unitPrice.getCode().v(), true)
				|| unitPrice.getName() == null || StringUtil.isNullOrEmpty(unitPrice.getName().v(), true)) {
			throw new BusinessException("ER001");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.UnitPriceService
	 * #checkDuplicateCode(nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPrice)
	 */
	@Override
	public void checkDuplicateCode(UnitPrice unitPrice) {
		if (unitPriceRepo.isDuplicateCode(unitPrice.getCompanyCode(), unitPrice.getCode())) {
			throw new BusinessException("ER005");
		}
	}

}
