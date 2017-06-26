/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.service.internal;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.service.AccidentInsuranceRateService;

/**
 * The Class AccidentInsuranceRateServiceImpl.
 */
@Stateless
public class AccidentInsuranceRateServiceImpl implements AccidentInsuranceRateService {

	/** The rate item count. */
	private static final int RATE_ITEM_COUNT = 10;

	/** The accident insurance rate repo. */
	@Inject
	private AccidentInsuranceRateRepository repository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.service.
	 * LaborInsuranceOfficeService#validateRequiredItem(nts.uk.ctx.pr.core.dom.
	 * insurance.labor.LaborInsuranceOffice)
	 */
	@Override
	public void validateRequiredItem(AccidentInsuranceRate rate) {
		if (StringUtil.isNullOrEmpty(rate.getHistoryId(), true) || rate.getApplyRange() == null
			|| CollectionUtil.isEmpty(rate.getRateItems())
			|| rate.getRateItems().size() != RATE_ITEM_COUNT) {
			throw new BusinessException("ER001");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.service.
	 * AccidentInsuranceRateService#validateDateRange(nts.uk.ctx.pr.core.dom.
	 * insurance.labor.accidentrate.AccidentInsuranceRate)
	 */
	@Override
	public void validateDateRange(AccidentInsuranceRate rate) {
		if (invalidateRate(rate)) {
			throw new BusinessException("ER010");
		}

	}

	/**
	 * Gets the validate range.
	 *
	 * @param rate
	 *            the rate
	 * @return the validate range
	 */
	private boolean invalidateRate(AccidentInsuranceRate rate) {

		boolean resvalue = false;
		if (rate.getApplyRange().getStartMonth().v() > rate.getApplyRange().getEndMonth().v()) {
			resvalue = true;
		}

		// find data first
		Optional<AccidentInsuranceRate> data = this.repository.findFirstData(rate.getCompanyCode());

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
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.service.
	 * AccidentInsuranceRateService#validateDateRangeUpdate(nts.uk.ctx.pr.core.
	 * dom.insurance.labor.accidentrate.AccidentInsuranceRate)
	 */
	@Override
	public void validateDateRangeUpdate(AccidentInsuranceRate rate) {
		if (isValidRateUpdate(rate)) {
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
	private boolean isValidRateUpdate(AccidentInsuranceRate rate) {
		return isMonthDate(rate) || !checkExistRate(rate) || isLaterThanLastHistory(rate);
	}

	/**
	 * Checks if is month date.
	 *
	 * @param rate
	 *            the rate
	 * @return true, if is month date
	 */
	// start date > end date
	private boolean isMonthDate(AccidentInsuranceRate rate) {
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
	private boolean checkExistRate(AccidentInsuranceRate rate) {
		Optional<AccidentInsuranceRate> data = this.repository.findById(rate.getHistoryId());
		return data.isPresent();
	}

	/**
	 * Checks if is later than last history.
	 *
	 * @param rate
	 *            the rate
	 * @return true, if is later than last history
	 */
	private boolean isLaterThanLastHistory(AccidentInsuranceRate rate) {

		Optional<AccidentInsuranceRate> data = this.repository.findById(rate.getHistoryId());

		if (data.isPresent()) {
			Optional<AccidentInsuranceRate> dataUpdate = this.repository.findBetweenUpdate(
				rate.getCompanyCode(), data.get().getApplyRange().getStartMonth(),
				data.get().getHistoryId());

			// check first data
			return (dataUpdate.isPresent() && (dataUpdate.get().getApplyRange().getStartMonth()
				.v() >= rate.getApplyRange().getStartMonth().v()));
		}

		return true;
	}

}
