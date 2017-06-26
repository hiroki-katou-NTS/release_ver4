/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.service.internal;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.service.UnemployeeInsuranceRateService;

/**
 * The Class UnemployeeInsuranceRateServiceImpl.
 */
@Stateless
public class UnemployeeInsuranceRateServiceImpl implements UnemployeeInsuranceRateService {

	/** The Constant RATE_ITEM_COUNT. */
	public static final int RATE_ITEM_COUNT = 3;

	/** The repository. */
	@Inject
	private UnemployeeInsuranceRateRepository repository;

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.service.
	 * UnemployeeInsuranceRateService#validateRequiredItem(nts.uk.ctx.pr.core.
	 * dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate)
	 */
	@Override
	public void validateRequiredItem(UnemployeeInsuranceRate rate) {
		if (StringUtil.isNullOrEmpty(rate.getHistoryId(), true)
			|| CollectionUtil.isEmpty(rate.getRateItems()) || rate.getRateItems() == null
			|| rate.getRateItems().size() != RATE_ITEM_COUNT) {
			throw new BusinessException("ER001");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.service.
	 * UnemployeeInsuranceRateService#validateDateRange(nts.uk.ctx.pr.core.dom.
	 * insurance.labor.unemployeerate.UnemployeeInsuranceRate)
	 */
	@Override
	public void validateDateRange(UnemployeeInsuranceRate rate) {
		if (this.invalidateRate(rate)) {
			throw new BusinessException("ER010");
		}
	}

	/**
	 * Invalidate rate.
	 *
	 * @param rate
	 *            the rate
	 * @return true, if successful
	 */
	private boolean invalidateRate(UnemployeeInsuranceRate rate) {
		boolean resvalue = false;
		if (rate.getApplyRange().getStartMonth().v() > rate.getApplyRange().getEndMonth().v()) {
			resvalue = true;
		}

		// find data first
		Optional<UnemployeeInsuranceRate> data = this.repository
			.findFirstData(rate.getCompanyCode());

		// check exist
		if (data.isPresent() && data.get().getApplyRange().getStartMonth().nextMonth().v() > rate
			.getApplyRange().getStartMonth().v()) {
			resvalue = true;
		}

		return resvalue;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.service.
	 * UnemployeeInsuranceRateService#validateDateRangeUpdate(nts.uk.ctx.pr.core
	 * .dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate)
	 */
	@Override
	public void validateDateRangeUpdate(UnemployeeInsuranceRate rate) {
		if (this.isValidRateUpdate(rate)) {
			throw new BusinessException("ER023");
		}
	}

	/**
	 * Checks if is valid rate update.
	 *
	 * @param rate
	 *            the rate
	 * @return true, if is valid rate update
	 */
	private boolean isValidRateUpdate(UnemployeeInsuranceRate rate) {
		return (isMonthDate(rate) || !checkExistRate(rate) || isLaterThanLastHistory(rate));
	}

	/**
	 * Checks if is month date.
	 *
	 * @param rate
	 *            the rate
	 * @return true, if is month date
	 */
	// start date > end date
	private boolean isMonthDate(UnemployeeInsuranceRate rate) {
		return rate.getApplyRange().getStartMonth().v() > rate.getApplyRange().getEndMonth().v();
	}

	/**
	 * Check exist rate.
	 *
	 * @param rate
	 *            the rate
	 * @return true, if successful
	 */
	// check exist rate
	private boolean checkExistRate(UnemployeeInsuranceRate rate) {
		Optional<UnemployeeInsuranceRate> data = this.repository.findById(rate.getCompanyCode(),
			rate.getHistoryId());
		return data.isPresent();
	}

	/**
	 * Checks if is later than last history.
	 *
	 * @param rate
	 *            the rate
	 * @return true, if is later than last history
	 */
	private boolean isLaterThanLastHistory(UnemployeeInsuranceRate rate) {

		Optional<UnemployeeInsuranceRate> data = this.repository.findById(rate.getCompanyCode(),
			rate.getHistoryId());

		if (data.isPresent()) {
			Optional<UnemployeeInsuranceRate> dataUpdate = this.repository.findBetweenUpdate(
				rate.getCompanyCode(), data.get().getApplyRange().getStartMonth(),
				data.get().getHistoryId());

			// check first data
			return (dataUpdate.isPresent() && (dataUpdate.get().getApplyRange().getStartMonth()
				.v() >= rate.getApplyRange().getStartMonth().v()));
		}
		return true;
	}

}