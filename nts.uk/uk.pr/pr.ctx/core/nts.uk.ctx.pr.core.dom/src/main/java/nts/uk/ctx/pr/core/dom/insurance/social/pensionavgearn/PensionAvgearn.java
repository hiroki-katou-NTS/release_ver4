/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;

/**
 * The Class PensionAvgearn.
 */
@Getter
@EqualsAndHashCode(callSuper = true, of = { "historyId", "grade" })
public class PensionAvgearn extends DomainObject {

	/** The history id. */
	@Setter
	private String historyId;

	/** The level code. */
	private Integer grade;

	/** The avg earn. */
	private Long avgEarn;

	/** The upper limit. */
	private Long upperLimit;

	/** The child contribution amount. */
	private CommonAmount childContributionAmount;

	/** The company fund. */
	private PensionAvgearnValue companyFund;

	/** The company fund exemption. */
	private PensionAvgearnValue companyFundExemption;

	/** The company pension. */
	private PensionAvgearnValue companyPension;

	/** The personal fund. */
	private PensionAvgearnValue personalFund;

	/** The personal fund exemption. */
	private PensionAvgearnValue personalFundExemption;

	/** The personal pension. */
	private PensionAvgearnValue personalPension;

	/**
	 * Instantiates a new pension avgearn.
	 *
	 * @param historyId
	 *            the history id
	 */
	private PensionAvgearn(String historyId) {
		this.historyId = historyId;
	}

	// =================== Memento State Support Method ===================

	/**
	 * Instantiates a new pension avgearn.
	 *
	 * @param memento
	 *            the memento
	 */
	public PensionAvgearn(PensionAvgearnGetMemento memento) {
		this.historyId = memento.getHistoryId();
		this.grade = memento.getGrade();
		this.avgEarn = memento.getAvgEarn();
		this.upperLimit = memento.getUpperLimit();
		this.childContributionAmount = memento.getChildContributionAmount();
		this.companyFund = memento.getCompanyFund();
		this.companyFundExemption = memento.getCompanyFundExemption();
		this.companyPension = memento.getCompanyPension();
		this.personalFund = memento.getPersonalFund();
		this.personalFundExemption = memento.getPersonalFundExemption();
		this.personalPension = memento.getPersonalPension();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(PensionAvgearnSetMemento memento) {
		memento.setHistoryId(this.historyId);
		memento.setGrade(this.grade);
		memento.setAvgEarn(this.avgEarn);
		memento.setUpperLimit(this.upperLimit);
		memento.setChildContributionAmount(this.childContributionAmount);
		memento.setCompanyFund(this.companyFund);
		memento.setCompanyFundExemption(this.companyFundExemption);
		memento.setCompanyPension(this.companyPension);
		memento.setPersonalFund(this.personalFund);
		memento.setPersonalFundExemption(this.personalFundExemption);
		memento.setPersonalPension(this.personalPension);
	}

	/**
	 * Copy with new history id.
	 *
	 * @param newHistoryId
	 *            the new history id
	 * @return the pension avgearn
	 */
	public PensionAvgearn copyWithNewHistoryId(String newHistoryId) {
		PensionAvgearn pensionAvgearn = new PensionAvgearn(newHistoryId);
		pensionAvgearn.grade = this.grade;
		pensionAvgearn.avgEarn = this.avgEarn;
		pensionAvgearn.upperLimit = this.upperLimit;
		pensionAvgearn.personalFund = this.personalFund;
		pensionAvgearn.personalFundExemption = this.personalFundExemption;
		pensionAvgearn.personalPension = this.personalPension;
		pensionAvgearn.companyFund = this.companyFund;
		pensionAvgearn.companyFundExemption = this.companyFundExemption;
		pensionAvgearn.companyPension = this.companyPension;
		pensionAvgearn.childContributionAmount = this.childContributionAmount;
		return pensionAvgearn;
	}

	/**
	 * Creates the with intial.
	 *
	 * @param newHistoryId
	 *            the new history id
	 * @param grade
	 *            the level code
	 * @return the object
	 */
	public static PensionAvgearn createWithIntial(String newHistoryId, Integer grade, Long avgEarn,
			Long upperLimit) {
		PensionAvgearn pensionAvgearn = new PensionAvgearn(newHistoryId);
		pensionAvgearn.grade = grade;
		pensionAvgearn.avgEarn = avgEarn;
		pensionAvgearn.upperLimit = upperLimit;
		CommonAmount defaultAmount = new CommonAmount(BigDecimal.ZERO);
		PensionAvgearnValue defaultValue = new PensionAvgearnValue(defaultAmount, defaultAmount,
				defaultAmount);
		pensionAvgearn.personalFund = defaultValue;
		pensionAvgearn.personalFundExemption = defaultValue;
		pensionAvgearn.personalPension = defaultValue;
		pensionAvgearn.companyFund = defaultValue;
		pensionAvgearn.companyFundExemption = defaultValue;
		pensionAvgearn.companyPension = defaultValue;
		pensionAvgearn.childContributionAmount = defaultAmount;
		return pensionAvgearn;
	}

}
