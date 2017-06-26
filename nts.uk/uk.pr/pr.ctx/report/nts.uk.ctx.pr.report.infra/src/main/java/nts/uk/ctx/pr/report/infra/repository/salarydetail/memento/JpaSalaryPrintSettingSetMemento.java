/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import lombok.Getter;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSettingSetMemento;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstPrintSet;

/**
 * The Class JpaSalaryPrintSettingSetMemento.
 */
public class JpaSalaryPrintSettingSetMemento implements SalaryPrintSettingSetMemento {

	/** The entity. */
	@Getter
	private QlsptPaylstPrintSet entity;

	/**
	 * Instantiates a new jpa salary print setting set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaSalaryPrintSettingSetMemento(QlsptPaylstPrintSet entity) {
		this.entity = entity;

		// Set default values
		this.entity.setHrchyIndex1(HierarchicalIndex.NO_SETTING.value);
		this.entity.setHrchyIndex2(HierarchicalIndex.NO_SETTING.value);
		this.entity.setHrchyIndex3(HierarchicalIndex.NO_SETTING.value);
		this.entity.setHrchyIndex4(HierarchicalIndex.NO_SETTING.value);
		this.entity.setHrchyIndex5(HierarchicalIndex.NO_SETTING.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setSumPersonSet(boolean)
	 */
	@Override
	public void setSumPersonSet(boolean sumPersonSet) {
		this.entity.setSumPersonSet(OutputSetting.bolOf(sumPersonSet).value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setCompanyCode(java.lang.String)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		this.entity.setCcd(companyCode);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setShowPayment(boolean)
	 */
	@Override
	public void setShowPayment(boolean showPayment) {
		this.entity.setShowPayment(OutputSetting.bolOf(showPayment).value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setSumMonthPersonSet(boolean)
	 */
	@Override
	public void setSumMonthPersonSet(boolean sumMonthPersonSet) {
		this.entity.setSumMonthPersonSet(OutputSetting.bolOf(sumMonthPersonSet).value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setSumEachDeprtSet(boolean)
	 */
	@Override
	public void setSumEachDeprtSet(boolean sumEachDeprtSet) {
		this.entity.setSumEachDeprtSet(OutputSetting.bolOf(sumEachDeprtSet).value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setSumMonthDeprtSet(boolean)
	 */
	@Override
	public void setSumMonthDeprtSet(boolean sumMonthDeprtSet) {
		this.entity.setSumMonthDeprtSet(OutputSetting.bolOf(sumMonthDeprtSet).value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setSumDepHrchyIndexSet(boolean)
	 */
	@Override
	public void setSumDepHrchyIndexSet(boolean sumDepHrchyIndexSet) {
		this.entity.setSumDepHrchyIndexSet(OutputSetting.bolOf(sumDepHrchyIndexSet).value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setSumMonthDepHrchySet(boolean)
	 */
	@Override
	public void setSumMonthDepHrchySet(boolean sumMonthDepHrchySet) {
		this.entity.setSumMonthDepHrchySet(OutputSetting.bolOf(sumMonthDepHrchySet).value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setHrchyIndex1(boolean)
	 */
	@Override
	public void setHrchyIndex1(boolean hrchyIndex) {
		if (hrchyIndex) {
			this.putHrchyOptionIntoDb(HierarchicalIndex.HIERARCHICAL_INDEX_1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setHrchyIndex2(boolean)
	 */
	@Override
	public void setHrchyIndex2(boolean hrchyIndex) {
		if (hrchyIndex) {
			this.putHrchyOptionIntoDb(HierarchicalIndex.HIERARCHICAL_INDEX_2);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setHrchyIndex3(boolean)
	 */
	@Override
	public void setHrchyIndex3(boolean hrchyIndex) {
		if (hrchyIndex) {
			this.putHrchyOptionIntoDb(HierarchicalIndex.HIERARCHICAL_INDEX_3);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setHrchyIndex4(boolean)
	 */
	@Override
	public void setHrchyIndex4(boolean hrchyIndex) {
		if (hrchyIndex) {
			this.putHrchyOptionIntoDb(HierarchicalIndex.HIERARCHICAL_INDEX_4);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setHrchyIndex5(boolean)
	 */
	@Override
	public void setHrchyIndex5(boolean hrchyIndex) {
		if (hrchyIndex) {
			this.putHrchyOptionIntoDb(HierarchicalIndex.HIERARCHICAL_INDEX_5);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setHrchyIndex6(boolean)
	 */
	@Override
	public void setHrchyIndex6(boolean hrchyIndex) {
		if (hrchyIndex) {
			this.putHrchyOptionIntoDb(HierarchicalIndex.HIERARCHICAL_INDEX_6);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setHrchyIndex7(boolean)
	 */
	@Override
	public void setHrchyIndex7(boolean hrchyIndex) {
		if (hrchyIndex) {
			this.putHrchyOptionIntoDb(HierarchicalIndex.HIERARCHICAL_INDEX_7);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setHrchyIndex8(boolean)
	 */
	@Override
	public void setHrchyIndex8(boolean hrchyIndex) {
		if (hrchyIndex) {
			this.putHrchyOptionIntoDb(HierarchicalIndex.HIERARCHICAL_INDEX_8);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#sethrchyIndex(boolean)
	 */
	@Override
	public void setHrchyIndex9(boolean hrchyIndex) {
		if (hrchyIndex) {
			this.putHrchyOptionIntoDb(HierarchicalIndex.HIERARCHICAL_INDEX_9);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setTotalSet(boolean)
	 */
	@Override
	public void setTotalSet(boolean totalSet) {
		this.entity.setTotalSet(OutputSetting.bolOf(totalSet).value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.
	 * SalaryPrintSettingSetMemento#setMonthTotalSet(boolean)
	 */
	@Override
	public void setMonthTotalSet(boolean monthTotalSet) {
		this.entity.setMonthTotalSet(OutputSetting.bolOf(monthTotalSet).value);
	}

	/**
	 * Put hrchy option into db.
	 *
	 * @param index
	 *            the index
	 */
	private void putHrchyOptionIntoDb(HierarchicalIndex index) {
		// Check free slot
		if (this.isFreeSlot(this.entity.getHrchyIndex1())) {
			this.entity.setHrchyIndex1(index.value);
			return;
		}
		// Check free slot
		if (this.isFreeSlot(this.entity.getHrchyIndex2())) {
			this.entity.setHrchyIndex2(index.value);
			return;
		}
		// Check free slot
		if (this.isFreeSlot(this.entity.getHrchyIndex3())) {
			this.entity.setHrchyIndex3(index.value);
			return;
		}
		// Check free slot
		if (this.isFreeSlot(this.entity.getHrchyIndex4())) {
			this.entity.setHrchyIndex4(index.value);
			return;
		}
		// Check free slot
		if (this.isFreeSlot(this.entity.getHrchyIndex5())) {
			this.entity.setHrchyIndex5(index.value);
			return;
		}
	}

	/**
	 * Checks if is free slot.
	 *
	 * @param hrchyIndex
	 *            the hrchy index
	 * @return true, if is free slot
	 */
	private boolean isFreeSlot(Integer hrchyIndex) {
		return hrchyIndex == null
				|| HierarchicalIndex.NO_SETTING.equals(HierarchicalIndex.valueOf(hrchyIndex));
	}
}
