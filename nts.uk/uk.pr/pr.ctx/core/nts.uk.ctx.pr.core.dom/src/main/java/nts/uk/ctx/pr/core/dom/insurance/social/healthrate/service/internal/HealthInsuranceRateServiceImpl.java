/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthrate.service.internal;

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
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimit;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimitRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.service.HealthInsuranceRateService;

/**
 * The Class HealthInsuranceRateServiceImpl.
 */
@Stateless
public class HealthInsuranceRateServiceImpl extends HealthInsuranceRateService {

	/** The Constant INSURANCE_RATE_ITEM_COUNT. */
	private static final int INSURANCE_RATE_ITEM_COUNT = 8;

	/** The Constant HEALTH_INSURANCE_ROUNDING_COUNT. */
	private static final int HEALTH_INSURANCE_ROUNDING_COUNT = 2;

	/** The health insurance rate repo. */
	@Inject
	private HealthInsuranceRateRepository healthInsuranceRateRepo;

	/** The avg earn limit repo. */
	@Inject
	private HealthAvgEarnLimitRepository avgEarnLimitRepo;

	/** The health insurance avgearn repository. */
	@Inject
	private HealthInsuranceAvgearnRepository healthInsuranceAvgearnRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.service.
	 * HealthInsuranceRateService#validateRequiredItem(nts.uk.ctx.pr.core.dom.
	 * insurance.social.healthrate.HealthInsuranceRate)
	 */
	@Override
	public void validateRequiredItem(HealthInsuranceRate rate) {
		if (StringUtil.isNullOrEmpty(rate.getOfficeCode().v(), true) || rate.getApplyRange() == null
				|| rate.getMaxAmount() == null || CollectionUtil.isEmpty(rate.getRateItems())
				|| rate.getRateItems().size() != INSURANCE_RATE_ITEM_COUNT
				|| CollectionUtil.isEmpty(rate.getRoundingMethods())
				|| rate.getRoundingMethods().size() != HEALTH_INSURANCE_ROUNDING_COUNT) {
			throw new BusinessException("ER001");
		}
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService#getRepository()
	 */
	@Override
	public SimpleHistoryRepository<HealthInsuranceRate> getRepository() {
		return this.healthInsuranceRateRepo;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService#createInitalHistory(java.lang.String, java.lang.String, nts.arc.time.YearMonth)
	 */
	@Override
	public HealthInsuranceRate createInitalHistory(String companyCode, String officeCode,
			YearMonth startTime) {
		List<HealthInsuranceRate> listHealthOfOffice = this.healthInsuranceRateRepo
				.findAllOffice(companyCode, officeCode);
		List<HealthInsuranceRate> obj = listHealthOfOffice.stream()
				.filter(c -> c.getApplyRange().getStartMonth().equals(startTime))
				.collect(Collectors.toList());
		if (!obj.isEmpty()) {
			throw new BusinessException("ER011");
		}
		return HealthInsuranceRate.createWithIntial(companyCode, new OfficeCode(officeCode),
				startTime);
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
	protected void onCopyHistory(String companyCode, String masterCode,
			HealthInsuranceRate copiedHistory, HealthInsuranceRate newHistory) {

		// Get listAvgEarn of copiedHistory.
		List<HealthInsuranceAvgearn> listHealthInsuranceAvgearn = healthInsuranceAvgearnRepository
				.findById(copiedHistory.getHistoryId());

		// Update newHistoryId.
		List<HealthInsuranceAvgearn> updatedList = listHealthInsuranceAvgearn.stream().map(item -> {
			return item.copyWithNewHistoryId(newHistory.getHistoryId());
		}).collect(Collectors.toList());

		this.healthInsuranceAvgearnRepository.update(updatedList, companyCode,
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
	protected void onCreateHistory(String companyCode, String masterCode,
			HealthInsuranceRate newHistory) {

		// Get listHealthAvgEarnLimit.
		List<HealthAvgEarnLimit> listHealthAvgEarnLimit = avgEarnLimitRepo
				.findAll(companyCode);

		// Create HealthInsuranceAvgearn list with initial values.
		List<HealthInsuranceAvgearn> newList = listHealthAvgEarnLimit.stream()
				.map(setting -> {
					return HealthInsuranceAvgearn.createWithIntial(newHistory.getHistoryId(),
							setting.getGrade(), setting.getAvgEarn(), setting.getSalLimit());
				}).collect(Collectors.toList());

		this.healthInsuranceAvgearnRepository.update(newList, companyCode,
				newHistory.getOfficeCode().v());
	}
}
