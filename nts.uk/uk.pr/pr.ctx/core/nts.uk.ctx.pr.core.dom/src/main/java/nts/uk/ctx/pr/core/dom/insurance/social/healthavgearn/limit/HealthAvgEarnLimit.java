/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit;

import lombok.Data;

/**
 * The Class HealthAvgEarnLimit.
 */
@Data
public class HealthAvgEarnLimit {

	/** The grade. */
	private Integer grade;

	/** The avg earn. */
	private Long avgEarn;

	/** The sal limit. */
	private Long salLimit;

	/**
	 * Instantiates a new avg earn level master setting.
	 */
	public HealthAvgEarnLimit() {
		super();
	}

	/**
	 * Instantiates a new avg earn level master setting.
	 *
	 * @param grade the grade
	 * @param avgEarn the avg earn
	 * @param salLimit the sal limit
	 */
	public HealthAvgEarnLimit(Integer grade, Long avgEarn, Long salLimit) {
		super();
		this.grade = grade;
		this.avgEarn = avgEarn;
		this.salLimit = salLimit;
	}

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new labor insurance office.
	 *
	 * @param memento
	 *            the memento
	 */
	public HealthAvgEarnLimit(HealthAvgEarnLimitGetMemento memento) {
		this.grade = memento.getGrade();
		this.avgEarn = memento.getAvgEarn();
		this.salLimit = memento.getSalLimit();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(HealthAvgEarnLimitSetMemento memento) {
		memento.setGrade(this.grade);
		memento.setAvgEarn(this.avgEarn);
		memento.setSalLimit(this.salLimit);
	}

}
