/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.insurance;

import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento;
import nts.uk.ctx.pr.report.infra.entity.insurance.QismtChecklistPrintSet;
import nts.uk.ctx.pr.report.infra.util.JpaUtil;

/**
 * The Class JpaChecklistPrintSettingSetMemento.
 */
public class JpaChecklistPrintSettingSetMemento implements ChecklistPrintSettingSetMemento {

	/** The entity. */
	private QismtChecklistPrintSet entity;

	/**
	 * Instantiates a new jpa checklist print setting set memento.
	 *
	 * @param companyCode the company code
	 * @param entity the entity
	 */
	public JpaChecklistPrintSettingSetMemento(QismtChecklistPrintSet entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setCompanyCode(java.lang.String)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		this.entity.setCcd(companyCode);
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setShowCategoryInsuranceItem(java.lang.Boolean)
	 */
	@Override
	public void setShowCategoryInsuranceItem(Boolean showCategoryInsuranceItem) {
		this.entity.setIsShowBreakdownItem(JpaUtil.boolean2Short(showCategoryInsuranceItem));
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setShowDeliveryNoticeAmount(java.lang.Boolean)
	 */
	@Override
	public void setShowDeliveryNoticeAmount(Boolean showDeliveryNoticeAmount) {
		this.entity.setIsShowTotalPayMny(JpaUtil.boolean2Short(showDeliveryNoticeAmount));
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setShowDetail(java.lang.Boolean)
	 */
	@Override
	public void setShowDetail(Boolean showDetail) {
		this.entity.setIsShowPersonSumMny(JpaUtil.boolean2Short(showDetail));
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setShowOffice(java.lang.Boolean)
	 */
	@Override
	public void setShowOffice(Boolean showOffice) {
		this.entity.setIsShowTotalMnyCol(JpaUtil.boolean2Short(showOffice));
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setShowTotal(java.lang.Boolean)
	 */
	@Override
	public void setShowTotal(Boolean showTotal) {
		this.entity.setIsShowOfficeSumMny(JpaUtil.boolean2Short(showTotal));
	}

}
