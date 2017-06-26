/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import java.util.Arrays;

import lombok.Getter;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSettingGetMemento;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstPrintSet;

/**
 * The Class JpaSalaryPrintSettingGetMemento.
 */
public class JpaSalaryPrintSettingGetMemento implements SalaryPrintSettingGetMemento {

	/** The entity. */
	@Getter
	private QlsptPaylstPrintSet entity;

	/**
	 * Instantiates a new jpa salary print setting get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaSalaryPrintSettingGetMemento(QlsptPaylstPrintSet entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.entity.getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getShowPayment()
	 */
	@Override
	public Boolean getShowPayment() {
		return OutputSetting.valueOf(this.entity.getShowPayment()).bol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumPersonSet()
	 */
	@Override
	public Boolean getSumPersonSet() {
		return OutputSetting.valueOf(this.entity.getSumPersonSet()).bol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumMonthPersonSet()
	 */
	@Override
	public Boolean getSumMonthPersonSet() {
		return OutputSetting.valueOf(this.entity.getSumMonthPersonSet()).bol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumEachDeprtSet()
	 */
	@Override
	public Boolean getSumEachDeprtSet() {
		return OutputSetting.valueOf(this.entity.getSumEachDeprtSet()).bol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumMonthDeprtSet()
	 */
	@Override
	public Boolean getSumMonthDeprtSet() {
		return OutputSetting.valueOf(this.entity.getSumMonthDeprtSet()).bol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumDepHrchyIndexSet()
	 */
	@Override
	public Boolean getSumDepHrchyIndexSet() {
		return OutputSetting.valueOf(this.entity.getSumDepHrchyIndexSet()).bol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getSumMonthDepHrchySet()
	 */
	@Override
	public Boolean getSumMonthDepHrchySet() {
		return OutputSetting.valueOf(this.entity.getSumMonthDepHrchySet()).bol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex1()
	 */
	@Override
	public Boolean getHrchyIndex1() {
		return this.getHrchyOptionFromDb(HierarchicalIndex.HIERARCHICAL_INDEX_1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex2()
	 */
	@Override
	public Boolean getHrchyIndex2() {
		return this.getHrchyOptionFromDb(HierarchicalIndex.HIERARCHICAL_INDEX_2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex3()
	 */
	@Override
	public Boolean getHrchyIndex3() {
		return this.getHrchyOptionFromDb(HierarchicalIndex.HIERARCHICAL_INDEX_3);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex4()
	 */
	@Override
	public Boolean getHrchyIndex4() {
		return this.getHrchyOptionFromDb(HierarchicalIndex.HIERARCHICAL_INDEX_4);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex5()
	 */
	@Override
	public Boolean getHrchyIndex5() {
		return this.getHrchyOptionFromDb(HierarchicalIndex.HIERARCHICAL_INDEX_5);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex6()
	 */
	@Override
	public Boolean getHrchyIndex6() {
		return this.getHrchyOptionFromDb(HierarchicalIndex.HIERARCHICAL_INDEX_6);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex7()
	 */
	@Override
	public Boolean getHrchyIndex7() {
		return this.getHrchyOptionFromDb(HierarchicalIndex.HIERARCHICAL_INDEX_7);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex8()
	 */
	@Override
	public Boolean getHrchyIndex8() {
		return this.getHrchyOptionFromDb(HierarchicalIndex.HIERARCHICAL_INDEX_8);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getHrchyIndex9()
	 */
	@Override
	public Boolean getHrchyIndex9() {
		return this.getHrchyOptionFromDb(HierarchicalIndex.HIERARCHICAL_INDEX_9);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getTotalSet()
	 */
	@Override
	public Boolean getTotalSet() {
		return OutputSetting.valueOf(this.entity.getTotalSet()).bol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingGetMemento#getMonthTotalSet()
	 */
	@Override
	public Boolean getMonthTotalSet() {
		return OutputSetting.valueOf(this.entity.getMonthTotalSet()).bol;
	}

	/**
	 * Convert hrchy option.
	 *
	 * @param index
	 *            the index
	 * @return true, if successful
	 */
	private boolean getHrchyOptionFromDb(HierarchicalIndex index) {
		return Arrays.asList(HierarchicalIndex.valueOf(this.entity.getHrchyIndex1()),
				HierarchicalIndex.valueOf(this.entity.getHrchyIndex2()),
				HierarchicalIndex.valueOf(this.entity.getHrchyIndex3()),
				HierarchicalIndex.valueOf(this.entity.getHrchyIndex4()),
				HierarchicalIndex.valueOf(this.entity.getHrchyIndex5())).contains(index);
	}

}
