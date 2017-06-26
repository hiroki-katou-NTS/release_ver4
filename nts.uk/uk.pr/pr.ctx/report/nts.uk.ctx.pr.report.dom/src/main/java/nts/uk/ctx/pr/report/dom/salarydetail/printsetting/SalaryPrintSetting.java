/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.printsetting;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class SalaryPrintSetting.
 */
@Getter
public class SalaryPrintSetting extends DomainObject {

	/** The company code. */
	private String companyCode;

	/** The show payment. */
	@Setter
	private Boolean showPayment;

	/** The sum person set. */
	@Setter
	private Boolean sumPersonSet;

	/** The sum month person set. */
	@Setter
	private Boolean sumMonthPersonSet;

	/** The sum each deprt set. */
	@Setter
	private Boolean sumEachDeprtSet;

	/** The sum month deprt set. */
	@Setter
	private Boolean sumMonthDeprtSet;

	/** The sum dep hrchy index set. */
	@Setter
	private Boolean sumDepHrchyIndexSet;

	/** The sum month dep hrchy set. */
	@Setter
	private Boolean sumMonthDepHrchySet;

	/** The hrchy index 1. */
	@Setter
	private Boolean hrchyIndex1;

	/** The hrchy index 2. */
	@Setter
	private Boolean hrchyIndex2;

	/** The hrchy index 3. */
	@Setter
	private Boolean hrchyIndex3;

	/** The hrchy index 4. */
	@Setter
	private Boolean hrchyIndex4;

	/** The hrchy index 5. */
	@Setter
	private Boolean hrchyIndex5;

	/** The hrchy index 6. */
	@Setter
	private Boolean hrchyIndex6;

	/** The hrchy index 7. */
	@Setter
	private Boolean hrchyIndex7;

	/** The hrchy index 8. */
	@Setter
	private Boolean hrchyIndex8;

	/** The hrchy index 9. */
	@Setter
	private Boolean hrchyIndex9;

	/** The total set. */
	@Setter
	private Boolean totalSet;

	/** The month total set. */
	@Setter
	private Boolean monthTotalSet;

	/**
	 * Instantiates a new salary print setting.
	 */
	private SalaryPrintSetting() {
		super();
	}

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new salary print setting.
	 *
	 * @param memento the memento
	 */
	public SalaryPrintSetting(SalaryPrintSettingGetMemento memento) {
		this.companyCode = memento.getCompanyCode();
		this.showPayment = memento.getShowPayment();
		this.sumPersonSet = memento.getSumPersonSet();
		this.sumMonthPersonSet = memento.getSumMonthPersonSet();
		this.sumEachDeprtSet = memento.getSumEachDeprtSet();
		this.sumMonthDeprtSet = memento.getSumMonthDeprtSet();
		this.sumDepHrchyIndexSet = memento.getSumDepHrchyIndexSet();
		this.sumMonthDepHrchySet = memento.getSumMonthDepHrchySet();
		this.hrchyIndex1 = memento.getHrchyIndex1();
		this.hrchyIndex2 = memento.getHrchyIndex2();
		this.hrchyIndex3 = memento.getHrchyIndex3();
		this.hrchyIndex4 = memento.getHrchyIndex4();
		this.hrchyIndex5 = memento.getHrchyIndex5();
		this.hrchyIndex6 = memento.getHrchyIndex6();
		this.hrchyIndex7 = memento.getHrchyIndex7();
		this.hrchyIndex8 = memento.getHrchyIndex8();
		this.hrchyIndex9 = memento.getHrchyIndex9();
		this.totalSet = memento.getTotalSet();
		this.monthTotalSet = memento.getMonthTotalSet();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(SalaryPrintSettingSetMemento memento) {
		memento.setCompanyCode(this.companyCode);
		memento.setHrchyIndex1(this.hrchyIndex1);
		memento.setHrchyIndex2(this.hrchyIndex2);
		memento.setHrchyIndex3(this.hrchyIndex3);
		memento.setHrchyIndex4(this.hrchyIndex4);
		memento.setHrchyIndex5(this.hrchyIndex5);
		memento.setHrchyIndex6(this.hrchyIndex6);
		memento.setHrchyIndex7(this.hrchyIndex7);
		memento.setHrchyIndex8(this.hrchyIndex8);
		memento.setHrchyIndex9(this.hrchyIndex9);
		memento.setMonthTotalSet(this.monthTotalSet);
		memento.setTotalSet(this.totalSet);
		memento.setShowPayment(this.showPayment);
		memento.setSumDepHrchyIndexSet(this.sumDepHrchyIndexSet);
		memento.setSumEachDeprtSet(this.sumEachDeprtSet);
		memento.setSumMonthDepHrchySet(this.sumMonthDepHrchySet);
		memento.setSumPersonSet(this.sumPersonSet);
		memento.setSumMonthPersonSet(this.sumMonthPersonSet);
		memento.setSumMonthDeprtSet(this.sumMonthDeprtSet);
	}

	/**
	 * Creates the with intial.
	 *
	 * @param companyCode the company code
	 * @return the salary print setting
	 */
	public static final SalaryPrintSetting createWithIntial(String companyCode) {
		SalaryPrintSetting setting = new SalaryPrintSetting();
		setting.companyCode = companyCode;
		setting.hrchyIndex1 = false;
		setting.hrchyIndex2 = false;
		setting.hrchyIndex3 = false;
		setting.hrchyIndex4 = false;
		setting.hrchyIndex5 = false;
		setting.hrchyIndex6 = false;
		setting.hrchyIndex7 = false;
		setting.hrchyIndex8 = false;
		setting.hrchyIndex9 = false;
		setting.monthTotalSet = false;
		setting.showPayment = false;
		setting.sumDepHrchyIndexSet = false;
		setting.sumEachDeprtSet = false;
		setting.sumMonthDepHrchySet = false;
		setting.sumMonthDeprtSet = false;
		setting.sumMonthPersonSet = false;
		setting.sumPersonSet = false;
		setting.totalSet = false;
		return setting;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalaryPrintSetting other = (SalaryPrintSetting) obj;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		return true;
	}
}
