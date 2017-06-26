/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.printsetting.command;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSettingGetMemento;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SalaryPrintSettingSaveCommand.
 */
@Getter
@Setter
public class SalaryPrintSettingSaveCommand implements SalaryPrintSettingGetMemento {

	/** The show payment. */
	private Boolean showPayment;

	/** The sum person set. */
	private Boolean sumPersonSet;

	/** The sum month person set. */
	private Boolean sumMonthPersonSet;

	/** The sum each deprt set. */
	private Boolean sumEachDeprtSet;

	/** The sum month deprt set. */
	private Boolean sumMonthDeprtSet;

	/** The sum dep hrchy index set. */
	private Boolean sumDepHrchyIndexSet;

	/** The sum month dep hrchy set. */
	private Boolean sumMonthDepHrchySet;

	/** The hrchy index 1. */
	private Boolean hrchyIndex1;

	/** The hrchy index 2. */
	private Boolean hrchyIndex2;

	/** The hrchy index 3. */
	private Boolean hrchyIndex3;

	/** The hrchy index 4. */
	private Boolean hrchyIndex4;

	/** The hrchy index 5. */
	private Boolean hrchyIndex5;

	/** The hrchy index 6. */
	private Boolean hrchyIndex6;

	/** The hrchy index 7. */
	private Boolean hrchyIndex7;

	/** The hrchy index 8. */
	private Boolean hrchyIndex8;

	/** The hrchy index 9. */
	private Boolean hrchyIndex9;

	/** The total set. */
	private Boolean totalSet;

	/** The month total set. */
	private Boolean monthTotalSet;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getShowPayment()
	 */
	@Override
	public Boolean getShowPayment() {
		return this.showPayment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumPersonSet()
	 */
	@Override
	public Boolean getSumPersonSet() {
		return this.sumPersonSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumMonthPersonSet()
	 */
	@Override
	public Boolean getSumMonthPersonSet() {
		return this.sumMonthPersonSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumEachDeprtSet()
	 */
	@Override
	public Boolean getSumEachDeprtSet() {
		return this.sumEachDeprtSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumMonthDeprtSet()
	 */
	@Override
	public Boolean getSumMonthDeprtSet() {
		return this.sumMonthDeprtSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumDepHrchyIndexSet()
	 */
	@Override
	public Boolean getSumDepHrchyIndexSet() {
		return this.sumDepHrchyIndexSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumMonthDepHrchySet()
	 */
	@Override
	public Boolean getSumMonthDepHrchySet() {
		return this.sumMonthDepHrchySet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex1()
	 */
	@Override
	public Boolean getHrchyIndex1() {
		return this.hrchyIndex1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex2()
	 */
	@Override
	public Boolean getHrchyIndex2() {
		return this.hrchyIndex2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex3()
	 */
	@Override
	public Boolean getHrchyIndex3() {
		return this.hrchyIndex3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex4()
	 */
	@Override
	public Boolean getHrchyIndex4() {
		return this.hrchyIndex4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex5()
	 */
	@Override
	public Boolean getHrchyIndex5() {
		return this.hrchyIndex5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex6()
	 */
	@Override
	public Boolean getHrchyIndex6() {
		return this.hrchyIndex6;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex7()
	 */
	@Override
	public Boolean getHrchyIndex7() {
		return this.hrchyIndex7;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex8()
	 */
	@Override
	public Boolean getHrchyIndex8() {
		return this.hrchyIndex8;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex9()
	 */
	@Override
	public Boolean getHrchyIndex9() {
		return this.hrchyIndex9;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getTotalSet()
	 */
	@Override
	public Boolean getTotalSet() {
		return this.totalSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getMonthTotalSet()
	 */
	@Override
	public Boolean getMonthTotalSet() {
		return this.monthTotalSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return AppContexts.user().companyCode();
	}

}
