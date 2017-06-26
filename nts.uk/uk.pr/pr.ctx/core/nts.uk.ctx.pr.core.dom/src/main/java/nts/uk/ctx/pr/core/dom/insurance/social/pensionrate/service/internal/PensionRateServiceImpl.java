/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.service.internal;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.PensionAvgEarnLimit;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.PensionAvgEarnLimitRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRate;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.service.PensionRateService;

/**
 * The Class PensionRateServiceImpl.
 */
@Stateless
public class PensionRateServiceImpl extends PensionRateService {

	/** The fund rate item count. */
	private final int FUND_RATE_ITEM_COUNT = 6;

	/** The rounding method count. */
	private final int ROUNDING_METHOD_COUNT = 2;

	/** The pension rate repo. */
	@Inject
	private PensionRateRepository pensionRateRepo;

	/** The avg earn level master setting repository. */
	@Inject
	private PensionAvgEarnLimitRepository avgEarnLimitRepo;

	/** The pension avgearn repository. */
	@Inject
	private PensionAvgearnRepository pensionAvgearnRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.service.
	 * PensionRateService#validateRequiredItem(nts.uk.ctx.pr.core.dom.insurance.
	 * social.pensionrate.PensionRate)
	 */
	@Override
	public void validateRequiredItem(PensionRate rate) {
		if (rate.getOfficeCode() == null || StringUtil.isNullOrEmpty(rate.getOfficeCode().v(), true)
				|| rate.getApplyRange() == null || rate.getMaxAmount() == null
				|| rate.getChildContributionRate() == null
				|| CollectionUtil.isEmpty(rate.getFundRateItems())
				|| rate.getFundRateItems().size() != FUND_RATE_ITEM_COUNT
				|| CollectionUtil.isEmpty(rate.getRoundingMethods())
				|| rate.getRoundingMethods().size() != ROUNDING_METHOD_COUNT) {
			throw new BusinessException("ER001");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService#
	 * getRepository()
	 */
	@Override
	public SimpleHistoryRepository<PensionRate> getRepository() {
		return this.pensionRateRepo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService#
	 * createInitalHistory(java.lang.String, java.lang.String,
	 * nts.arc.time.YearMonth)
	 */
	@Override
	public PensionRate createInitalHistory(String companyCode, String officeCode,
			YearMonth startTime) {
		List<PensionRate> listPensionOfOffice = this.pensionRateRepo.findAllOffice(companyCode,
				officeCode);
		List<PensionRate> lstPensionRate = listPensionOfOffice.stream()
				.filter(c -> c.getStart().equals(startTime)).collect(Collectors.toList());
		if (!lstPensionRate.isEmpty()) {
			throw new BusinessException("ER011");
		}
		return PensionRate.createWithIntial(companyCode, new OfficeCode(officeCode), startTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService#
	 * onCopyHistory(java.lang.String, java.lang.String,
	 * nts.uk.ctx.pr.core.dom.base.simplehistory.History,
	 * nts.uk.ctx.pr.core.dom.base.simplehistory.History)
	 */
	@Override
	protected void onCopyHistory(String companyCode, String masterCode, PensionRate copiedHistory,
			PensionRate newHistory) {

		// Get listAvgEarn of copiedHistory.
		List<PensionAvgearn> listPensionAvgearn = pensionAvgearnRepo
				.findById(copiedHistory.getHistoryId());

		// Update newHistoryId.
		List<PensionAvgearn> updatedList = listPensionAvgearn.stream().map(item -> {
			return item.copyWithNewHistoryId(newHistory.getHistoryId());
		}).collect(Collectors.toList());

		this.pensionAvgearnRepo.update(updatedList, companyCode,
				newHistory.getOfficeCode().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService#
	 * onCreateHistory(java.lang.String, java.lang.String,
	 * nts.uk.ctx.pr.core.dom.base.simplehistory.History)
	 */
	@Override
	protected void onCreateHistory(String companyCode, String masterCode, PensionRate newHistory) {

		// Get listHealthAvgEarnLimit.
		List<PensionAvgEarnLimit> listHealthAvgEarnLimit = avgEarnLimitRepo
				.findAll(companyCode);

		// Create HealthInsuranceAvgearn list with initial values.
		List<PensionAvgearn> newList = listHealthAvgEarnLimit.stream().map(setting -> {
			return PensionAvgearn.createWithIntial(newHistory.getHistoryId(), setting.getGrade(),
					setting.getAvgEarn(), setting.getSalLimit());
		}).collect(Collectors.toList());

		this.pensionAvgearnRepo.update(newList, companyCode, newHistory.getOfficeCode().v());
	}
}
