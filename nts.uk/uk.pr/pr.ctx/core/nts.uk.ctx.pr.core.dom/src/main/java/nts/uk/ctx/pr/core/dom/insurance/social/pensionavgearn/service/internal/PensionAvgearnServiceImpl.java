/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.service.internal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.uk.ctx.pr.core.dom.base.service.internal.RoundingNumberImpl;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnValue;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.PensionAvgEarnLimit;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.PensionAvgEarnLimitRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.service.PensionAvgearnService;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.FundRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionChargeRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionPremiumRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRate;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRounding;

/**
 * The Class PensionAvgearnServiceImpl.
 */
@Stateless
public class PensionAvgearnServiceImpl implements PensionAvgearnService {

	/** The avg earn level master setting repository. */
	@Inject
	private PensionAvgEarnLimitRepository avgEarnLimitRepo;

	/** The rouding service. */
	@Inject
	private RoundingNumberImpl roudingService;

	/** The Constant OneThousand. */
	public static final BigDecimal OneThousand = BigDecimal.valueOf(1000);

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.service.
	 * PensionRateService#validateRequiredItem(nts.uk.ctx.pr.core.dom.insurance.
	 * social.pensionrate.PensionRate)
	 */
	@Override
	public void validateRequiredItem(PensionAvgearn pensionAvgearn) {
		// Validate required item
		if (pensionAvgearn.getChildContributionAmount() == null
				|| pensionAvgearn.getCompanyFund() == null
				|| pensionAvgearn.getCompanyFundExemption() == null
				|| pensionAvgearn.getCompanyPension() == null
				|| pensionAvgearn.getPersonalFund() == null
				|| pensionAvgearn.getPersonalFundExemption() == null
				|| pensionAvgearn.getPersonalPension() == null) {
			throw new BusinessException("ER001");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.service.
	 * PensionAvgearnService#calculateListPensionAvgearn(nts.uk.ctx.pr.core.dom.
	 * insurance.social.pensionrate.PensionRate)
	 */
	@Override
	public List<PensionAvgearn> calculateListPensionAvgearn(PensionRate pensionRate) {
		// Get listPensionAvgEarnLimit.
		List<PensionAvgEarnLimit> listPensionAvgEarnLimit = this.avgEarnLimitRepo
				.findAll(pensionRate.getCompanyCode());

		// Calculate listPensionAvgearn.
		List<PensionAvgearn> listPensionAvgearn = listPensionAvgEarnLimit.stream()
				.map(setting -> new PensionAvgearn(
						new PensionAvgearnGetMementoImpl(pensionRate.getHistoryId(), setting,
								pensionRate.getFundRateItems(), pensionRate.getPremiumRateItems(),
								pensionRate.getChildContributionRate().v(),
								pensionRate.getRoundingMethods())))
				.collect(Collectors.toList());
		return listPensionAvgearn;
	}

	/**
	 * The Class PensionAvgearnGetMementoImpl.
	 */
	private class PensionAvgearnGetMementoImpl implements PensionAvgearnGetMemento {

		/** The history id. */
		private String historyId;

		/** The level code. */
		private int grade;

		/** The avg earn. */
		private Long avgEarn;

		/** The upper limit. */
		private Long upperLimit;

		/** The master rate. */
		private BigDecimal masterRate;

		/** The rate items. */
		private Set<FundRateItem> rateItems;

		/** The premium rate items. */
		private Set<PensionPremiumRateItem> premiumRateItems;

		/** The child contribution rate. */
		private BigDecimal childContributionRate;

		/** The setting. */
		private PensionAvgearnSetting setting;

		/**
		 * Instantiates a new pension avgearn get memento impl.
		 *
		 * @param historyId
		 *            the history id
		 * @param setting
		 *            the setting
		 * @param rateItems
		 *            the rate items
		 * @param premiumRateItems
		 *            the premium rate items
		 * @param childContributionRate
		 *            the child contribution rate
		 * @param roundingMethods
		 *            the rounding methods
		 */
		public PensionAvgearnGetMementoImpl(String historyId, PensionAvgEarnLimit setting,
				Set<FundRateItem> rateItems, Set<PensionPremiumRateItem> premiumRateItems,
				BigDecimal childContributionRate, Set<PensionRateRounding> roundingMethods) {
			this.grade = setting.getGrade();
			this.avgEarn = setting.getAvgEarn();
			this.upperLimit = setting.getSalLimit();
			this.masterRate = BigDecimal.valueOf(setting.getAvgEarn()).divide(OneThousand);
			this.rateItems = rateItems;
			this.premiumRateItems = premiumRateItems;
			this.historyId = historyId;
			this.childContributionRate = childContributionRate;
			this.setting = new PensionAvgearnSetting();
			this.setting.setMasterRate(this.masterRate);
			this.setting.setPensionRateItems(this.premiumRateItems);
			this.setting.setRateItems(this.rateItems);
			PensionRateRounding salaryRoundingMethod = roundingMethods.stream()
					.filter(item -> item.getPayType() == PaymentType.Salary).findFirst()
					.orElseThrow(() -> new RuntimeException("No such RoungdingMethod."));
			this.setting
					.setCompanyRoundAtr(salaryRoundingMethod.getRoundAtrs().getCompanyRoundAtr());
			this.setting
					.setPersonalRoundAtr(salaryRoundingMethod.getRoundAtrs().getPersonalRoundAtr());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getHistoryId()
		 */
		@Override
		public String getHistoryId() {
			return this.historyId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getGrade()
		 */
		@Override
		public Integer getGrade() {
			return this.grade;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getAvgEarn()
		 */
		@Override
		public Long getAvgEarn() {
			return this.avgEarn;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getUpperLimit()
		 */
		@Override
		public Long getUpperLimit() {
			return this.upperLimit;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getChildContributionAmount()
		 */
		@Override
		public CommonAmount getChildContributionAmount() {
			return new CommonAmount(this.masterRate.multiply(this.childContributionRate));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getCompanyFund()
		 */
		@Override
		public PensionAvgearnValue getCompanyFund() {
			setting.setExemption(false);
			setting.setPersonal(false);
			return calculateAvgearnFundValue(setting);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getCompanyFundExemption()
		 */
		@Override
		public PensionAvgearnValue getCompanyFundExemption() {
			setting.setExemption(true);
			setting.setPersonal(false);
			return calculateAvgearnFundValue(setting);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getCompanyPension()
		 */
		@Override
		public PensionAvgearnValue getCompanyPension() {
			setting.setExemption(false);
			setting.setPersonal(false);
			return calculateAvgearnPensionValue(setting);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getPersonalFund()
		 */
		@Override
		public PensionAvgearnValue getPersonalFund() {
			this.setting.setExemption(false);
			this.setting.setPersonal(true);
			return calculateAvgearnFundValue(this.setting);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getPersonalFundExemption()
		 */
		@Override
		public PensionAvgearnValue getPersonalFundExemption() {
			this.setting.setExemption(true);
			this.setting.setPersonal(true);
			return calculateAvgearnFundValue(this.setting);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
		 * PensionAvgearnGetMemento#getPersonalPension()
		 */
		@Override
		public PensionAvgearnValue getPersonalPension() {
			this.setting.setExemption(false);
			this.setting.setPersonal(true);
			return calculateAvgearnPensionValue(this.setting);
		}

	}

	/**
	 * The Class PensionAvgearnSetting.
	 */
	@Setter
	private class PensionAvgearnSetting {

		/** The master rate. */
		private BigDecimal masterRate;

		/** The rate items. */
		private Set<FundRateItem> rateItems;

		/** The pension rate items. */
		private Set<PensionPremiumRateItem> pensionRateItems;

		/** The company round atr. */
		private RoundingMethod companyRoundAtr;

		/** The personal round atr. */
		private RoundingMethod personalRoundAtr;

		/** The is personal. */
		private boolean isPersonal;

		/** The is exemption. */
		private boolean isExemption;
	}

	/**
	 * Calculate avgearn pension value.
	 *
	 * @param setting
	 *            the setting
	 * @return the pension avgearn value
	 */
	private PensionAvgearnValue calculateAvgearnPensionValue(PensionAvgearnSetting setting) {
		PensionAvgearnValue value = new PensionAvgearnValue();
		setting.pensionRateItems.forEach(item -> {
			if (item.getPayType() == PaymentType.Salary) {
				switch (item.getGenderType()) {
				case Female:
					value.setFemaleAmount(new CommonAmount(calculatePensionRate(item, setting)));
					break;
				case Male:
					value.setMaleAmount(new CommonAmount(calculatePensionRate(item, setting)));
					break;
				case Unknow:
					value.setUnknownAmount(new CommonAmount(calculatePensionRate(item, setting)));
					break;
				default:
					// Do nothing because all cases are covered.
					break;
				}
			}
		});

		// Return calculated value
		return value;
	}

	/**
	 * Calculate avgearn fund value.
	 *
	 * @param setting
	 *            the setting
	 * @return the pension avgearn value
	 */
	private PensionAvgearnValue calculateAvgearnFundValue(PensionAvgearnSetting setting) {
		PensionAvgearnValue value = new PensionAvgearnValue();
		setting.rateItems.forEach(item -> {
			if (item.getPayType() == PaymentType.Salary) {
				switch (item.getGenderType()) {
				case Female:
					value.setFemaleAmount(new CommonAmount(calculateFundRate(item, setting)));
					break;
				case Male:
					value.setMaleAmount(new CommonAmount(calculateFundRate(item, setting)));
					break;
				case Unknow:
					value.setUnknownAmount(new CommonAmount(calculateFundRate(item, setting)));
					break;
				default:
					// Do nothing because all cases are covered.
					break;
				}
			}
		});

		// Return calculated value
		return value;
	}

	/**
	 * Calculate fund rate.
	 *
	 * @param fundRateItem
	 *            the fund rate item
	 * @param setting
	 *            the setting
	 * @return the big decimal
	 */
	private BigDecimal calculateFundRate(FundRateItem fundRateItem, PensionAvgearnSetting setting) {
		PensionChargeRateItem chargeRate = fundRateItem.getBurdenChargeRate();
		if (setting.isExemption) {
			chargeRate = fundRateItem.getExemptionChargeRate();
		}
		return this.calculateChargeRate(chargeRate, setting);
	}

	/**
	 * Calculate pension rate.
	 *
	 * @param pensionRateItem
	 *            the pension rate item
	 * @param setting
	 *            the setting
	 * @return the big decimal
	 */
	private BigDecimal calculatePensionRate(PensionPremiumRateItem pensionRateItem,
			PensionAvgearnSetting setting) {
		PensionChargeRateItem chargeRate = pensionRateItem.getChargeRate();
		return this.calculateChargeRate(chargeRate, setting);
	}

	/**
	 * Calculate charge rate.
	 *
	 * @param chargeRate
	 *            the charge rate
	 * @param setting
	 *            the setting
	 * @return the big decimal
	 */
	private BigDecimal calculateChargeRate(PensionChargeRateItem chargeRate,
			PensionAvgearnSetting setting) {
		if (setting.isPersonal) {
			return this.roudingService.pensionRounding(setting.personalRoundAtr,
					setting.masterRate.multiply(chargeRate.getPersonalRate().v()));
		}
		return this.roudingService.pensionRounding(setting.companyRoundAtr,
				setting.masterRate.multiply(chargeRate.getCompanyRate().v()));
	}
}
