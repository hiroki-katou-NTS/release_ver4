/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.service.internal;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.WtHeadRepository;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistory;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryRepository;
import nts.uk.ctx.pr.core.dom.wagetable.history.service.WtHistoryService;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WtHistoryServiceImpl.
 */
@Stateless
public class WtHistoryServiceImpl extends WtHistoryService {

	/** The wage table head repo. */
	@Inject
	private WtHeadRepository wtHeadRepo;

	/** The wage table history repo. */
	@Inject
	private WtHistoryRepository wtHistoryRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.
	 * WageTableHistoryService#validateRequiredItem(nts.uk.ctx.pr.core.dom.rule.
	 * employment.unitprice.WageTableHistory)
	 */
	public void validateRequiredItem(WtHistory history) {
		// Validate required item
		if (history.getWageTableCode() == null || history.getApplyRange() == null
				|| StringUtil.isNullOrEmpty(history.getHistoryId(), true)
				|| StringUtil.isNullOrEmpty(history.getWageTableCode().v(), true)
				|| CollectionUtil.isEmpty(history.getElementSettings())) {
			throw new BusinessException("ER001");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.
	 * WageTableHistoryService#validateDateRange(nts.uk.ctx.pr.core.dom.rule.
	 * employment.unitprice.WageTableHistory)
	 */
	public void validateDateRange(WtHistory history) {
		// Check is valid date range
		if (!wtHistoryRepo.isValidDateRange(history.getCompanyCode(),
				history.getWageTableCode().v(), history.getApplyRange().getStartMonth().v())) {
			// History after start date and time exists
			throw new BusinessException("ER011");
		}
	}

	/**
	 * Delete history.
	 *
	 * @param uuid
	 *            the uuid
	 */
	@Override
	public void deleteHistory(String uuid) {
		// Get the history.
		WtHistory history = this.wtHistoryRepo.findHistoryByUuid(uuid).get();

		// Get histories of master.
		List<WtHistory> wtHistoryList = this.wtHistoryRepo.findAllHistoryByMasterCode(
				AppContexts.user().companyCode(), history.getMasterCode().v());

		// Delete history.
		super.deleteHistory(uuid);

		// Remove unit price.
		if (!CollectionUtil.isEmpty(wtHistoryList) && wtHistoryList.size() == 1) {
			this.wtHeadRepo.remove(history.getCompanyCode(), history.getMasterCode().v());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.
	 * WageTableHistoryService#checkDuplicateCode(nts.uk.ctx.pr.core.dom.rule.
	 * employment.unitprice.WageTableHistory)
	 */
	public void checkDuplicateCode(WtHistory history) {
		// Check exist code
		if (wtHeadRepo.isExistCode(history.getCompanyCode(), history.getWageTableCode().v())) {
			throw new BusinessException("ER005");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService#
	 * getRepository()
	 */
	@Override
	public SimpleHistoryRepository<WtHistory> getRepository() {
		return this.wtHistoryRepo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService#
	 * createInitalHistory(java.lang.String, java.lang.String)
	 */
	@Override
	public WtHistory createInitalHistory(String companyCode, String masterCode,
			YearMonth startYearMonth) {
		return WtHistory.createWithIntial(companyCode, new WtCode(masterCode), startYearMonth);
	}
}
