/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.InsuranceAmount;

/**
 * The Class HealthInsuranceAvgearn.
 */
@Getter
@EqualsAndHashCode(callSuper = true, of = { "historyId", "grade" })
public class HealthInsuranceAvgearn extends DomainObject {

	/** The history id. */
	private String historyId;

	/** The level code. */
	private Integer grade;

	/** The avg earn. */
	private Long avgEarn;

	/** The upper limit. */
	private Long upperLimit;

	/** The company avg. */
	private HealthInsuranceAvgearnValue companyAvg;

	/** The personal avg. */
	private HealthInsuranceAvgearnValue personalAvg;

	/**
	 * Instantiates a new HealthInsuranceAvgearn.
	 */
	private HealthInsuranceAvgearn(String historyId) {
		super();
		this.historyId = historyId;
	}

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new health insurance rate.
	 *
	 * @param memento
	 *            the memento
	 */
	public HealthInsuranceAvgearn(HealthInsuranceAvgearnGetMemento memento) {
		this.historyId = memento.getHistoryId();
		this.grade = memento.getGrade();
		this.avgEarn = memento.getAvgEarn();
		this.upperLimit = memento.getUpperLimit();
		this.companyAvg = memento.getCompanyAvg();
		this.personalAvg = memento.getPersonalAvg();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(HealthInsuranceAvgearnSetMemento memento) {
		memento.setHistoryId(this.historyId);
		memento.setGrade(this.grade);
		memento.setAvgEarn(this.avgEarn);
		memento.setUpperLimit(this.upperLimit);
		memento.setCompanyAvg(this.companyAvg);
		memento.setPersonalAvg(this.personalAvg);
	}

	/**
	 * Copy with new history id.
	 *
	 * @param newHistoryId
	 *            the new history id
	 * @return the health insurance avgearn
	 */
	public HealthInsuranceAvgearn copyWithNewHistoryId(String newHistoryId) {
		HealthInsuranceAvgearn healthInsuranceAvgearn = new HealthInsuranceAvgearn(newHistoryId);
		healthInsuranceAvgearn.historyId = newHistoryId;
		healthInsuranceAvgearn.grade = this.grade;
		healthInsuranceAvgearn.avgEarn = this.avgEarn;
		healthInsuranceAvgearn.upperLimit = this.upperLimit;
		healthInsuranceAvgearn.companyAvg = this.companyAvg;
		healthInsuranceAvgearn.personalAvg = this.personalAvg;
		return healthInsuranceAvgearn;
	}

	/**
	 * Creates the with intial.
	 *
	 * @param newHistoryId
	 *            the new history id
	 * @param grade
	 *            the level code
	 * @return the health insurance avgearn
	 */
	public static HealthInsuranceAvgearn createWithIntial(String newHistoryId, Integer grade,
			Long avgEarn, Long upperLimit) {
		// Create new object
		HealthInsuranceAvgearn healthInsAvgearn = new HealthInsuranceAvgearn(newHistoryId);
		CommonAmount defComAmount = new CommonAmount(BigDecimal.ZERO);
		InsuranceAmount defInsAmount = new InsuranceAmount(BigDecimal.ZERO);

		// Set data
		healthInsAvgearn.historyId = newHistoryId;
		healthInsAvgearn.grade = grade;
		healthInsAvgearn.avgEarn = avgEarn;
		healthInsAvgearn.upperLimit = upperLimit;
		healthInsAvgearn.companyAvg = new HealthInsuranceAvgearnValue(defInsAmount, defComAmount,
				defComAmount, defInsAmount);
		healthInsAvgearn.personalAvg = new HealthInsuranceAvgearnValue(defInsAmount, defComAmount,
				defComAmount, defInsAmount);

		// Return
		return healthInsAvgearn;
	}

}
