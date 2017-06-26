/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.service.internal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.uk.ctx.pr.core.dom.base.service.RoundingNumber;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.InsuranceAmount;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnValue;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimit;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimitRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.service.HealthInsuranceAvgearnService;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRounding;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.InsuranceRateItem;

/**
 * The Class HealthInsuranceAvgearnServiceImpl.
 */
@Stateless
public class HealthInsuranceAvgearnServiceImpl implements HealthInsuranceAvgearnService {

	/** The avg earn level master setting repository. */
	@Inject
	private HealthAvgEarnLimitRepository avgEarnLimitRepo;

	/** The health insurance avgearn repository. */
	@Inject
	private HealthInsuranceAvgearnRepository healthInsuranceAvgearnRepository;

	/** The rounding number. */
	@Inject
	private RoundingNumber roundingNumber;

	/** The Constant OneThousand. */
	public static final BigDecimal OneThousand = BigDecimal.valueOf(1000);

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.service.
	 * HealthInsuranceAvgearnService#validateRequiredItem(nts.uk.ctx.pr.core.dom
	 * .insurance.social.healthavgearn.HealthInsuranceAvgearn)
	 */
	@Override
	public void validateRequiredItem(HealthInsuranceAvgearn healthInsuranceAvgearn) {
		if (healthInsuranceAvgearn.getCompanyAvg() == null
				|| healthInsuranceAvgearn.getPersonalAvg() == null) {
			throw new BusinessException("ER001");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.service.
	 * HealthInsuranceAvgearnService#updateHealthInsuranceRateAvgEarn(nts.uk.ctx
	 * .pr.core.dom.insurance.social.healthrate.HealthInsuranceRate)
	 */
	@Override
	public void updateHealthInsuranceRateAvgEarn(HealthInsuranceRate healthInsuranceRate) {
		List<HealthAvgEarnLimit> listAvgEarnLevelMasterSetting = avgEarnLimitRepo
				.findAll(healthInsuranceRate.getCompanyCode());
		// convert to Domain
		List<HealthInsuranceAvgearn> healthInsuranceAvgearns = listAvgEarnLevelMasterSetting
				.stream().map(item -> {
					return new HealthInsuranceAvgearn(
							new HiaGetMemento(item, healthInsuranceRate.getRateItems(),
									healthInsuranceRate.getRoundingMethods(),
									healthInsuranceRate.getHistoryId()));
				}).collect(Collectors.toList());
		healthInsuranceAvgearnRepository.update(healthInsuranceAvgearns,
				healthInsuranceRate.getCompanyCode(), healthInsuranceRate.getOfficeCode().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.service.
	 * HealthInsuranceAvgearnService#calculateAvgearnValue(java.util.Set,
	 * java.math.BigDecimal, java.util.Set, boolean)
	 */
	@Override
	public HealthInsuranceAvgearnValue calculateAvgearnValue(
			Set<HealthInsuranceRounding> roundingMethods, BigDecimal masterRate,
			Set<InsuranceRateItem> rateItems, boolean isPersonal) {
		HealthInsuranceRounding salaryRound = new HealthInsuranceRounding();

		HealthInsuranceRounding bonusRound = new HealthInsuranceRounding();
		roundingMethods.forEach(item -> {
			if (item.getPayType().equals(PaymentType.Salary)) {
				salaryRound.setPayType(item.getPayType());
				salaryRound.setRoundAtrs(item.getRoundAtrs());
			}
			if (item.getPayType().equals(PaymentType.Bonus)) {
				bonusRound.setPayType(item.getPayType());
				bonusRound.setRoundAtrs(item.getRoundAtrs());
			}
		});

		HealthInsuranceAvgearnValue value = new HealthInsuranceAvgearnValue();
		rateItems.forEach(rateItem -> {
			if (rateItem.getPayType() == PaymentType.Salary) {
				BigDecimal val = BigDecimal.ZERO;
				BigDecimal val2 = BigDecimal.ZERO;

				// check if personal
				if (isPersonal) {
					// for general and nursing
					val = roundingNumber.healthRounding(
							salaryRound.getRoundAtrs().getPersonalRoundAtr(),
							calculateChargeRate(masterRate, rateItem, isPersonal), 1);
					val2 = roundingNumber.healthRounding(
							salaryRound.getRoundAtrs().getPersonalRoundAtr(),
							calculateChargeRate(masterRate, rateItem, isPersonal), 3);
				} else// company
				{
					val = roundingNumber.healthRounding(
							salaryRound.getRoundAtrs().getCompanyRoundAtr(),
							calculateChargeRate(masterRate, rateItem, isPersonal), 1);
					val2 = roundingNumber.healthRounding(
							salaryRound.getRoundAtrs().getCompanyRoundAtr(),
							calculateChargeRate(masterRate, rateItem, isPersonal), 3);
				}

				switch (rateItem.getInsuranceType()) {
				case Basic:
					value.setHealthBasicMny(new InsuranceAmount(val2));
					break;
				case General:
					value.setHealthGeneralMny(new CommonAmount(val));
					break;
				case Nursing:
					value.setHealthNursingMny(new CommonAmount(val));
					break;
				case Special:
					value.setHealthSpecificMny(new InsuranceAmount(val2));
					break;
				}
			}
		});
		return value;

	}

	/**
	 * Calculate charge rate.
	 *
	 * @param masterRate
	 *            the master rate
	 * @param rateItem
	 *            the rate item
	 * @param isPersonal
	 *            the is personal
	 * @return the big decimal
	 */
	private BigDecimal calculateChargeRate(BigDecimal masterRate, InsuranceRateItem rateItem,
			boolean isPersonal) {
		if (isPersonal) {
			return masterRate.multiply(rateItem.getChargeRate().getPersonalRate().v())
					.divide(OneThousand);
		}
		return masterRate.multiply(rateItem.getChargeRate().getCompanyRate().v())
				.divide(OneThousand);
	}

	/**
	 * The Class HiaGetMemento.
	 */
	private class HiaGetMemento implements HealthInsuranceAvgearnGetMemento {

		/** The setting. */
		private HealthAvgEarnLimit setting;

		/** The rate items. */
		private Set<InsuranceRateItem> rateItems;

		/** The rounding methods. */
		private Set<HealthInsuranceRounding> roundingMethods;

		/** The history id. */
		private String historyId;

		/**
		 * Instantiates a new hia get memento.
		 *
		 * @param setting
		 *            the setting
		 * @param rateItems
		 *            the rate items
		 * @param roundingMethods
		 *            the rounding methods
		 * @param historyId
		 *            the history id
		 */
		public HiaGetMemento(HealthAvgEarnLimit setting, Set<InsuranceRateItem> rateItems,
				Set<HealthInsuranceRounding> roundingMethods, String historyId) {
			this.setting = setting;
			this.rateItems = rateItems;
			this.roundingMethods = roundingMethods;
			this.historyId = historyId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getHistoryId()
		 */
		@Override
		public String getHistoryId() {
			return this.historyId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getGrade()
		 */
		@Override
		public Integer getGrade() {
			return this.setting.getGrade();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getCompanyAvg()
		 */
		@Override
		public HealthInsuranceAvgearnValue getCompanyAvg() {
			return calculateAvgearnValue(this.roundingMethods,
					BigDecimal.valueOf(this.setting.getAvgEarn()), this.rateItems, false);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getPersonalAvg()
		 */
		@Override
		public HealthInsuranceAvgearnValue getPersonalAvg() {
			return calculateAvgearnValue(this.roundingMethods,
					BigDecimal.valueOf(this.setting.getAvgEarn()), this.rateItems, true);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getAvgEarn()
		 */
		@Override
		public Long getAvgEarn() {
			return this.setting.getAvgEarn();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
		 * HealthInsuranceAvgearnGetMemento#getUpperLimit()
		 */
		@Override
		public Long getUpperLimit() {
			return this.setting.getSalLimit();
		}

	}
}
