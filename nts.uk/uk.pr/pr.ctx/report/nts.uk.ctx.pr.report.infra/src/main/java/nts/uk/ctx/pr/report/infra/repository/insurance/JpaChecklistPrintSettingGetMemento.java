/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.insurance;

import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento;
import nts.uk.ctx.pr.report.infra.entity.insurance.QismtChecklistPrintSet;
import nts.uk.ctx.pr.report.infra.util.JpaUtil;

/**
 * The Class JpaChecklistPrintSettingGetMemento.
 */
public class JpaChecklistPrintSettingGetMemento implements ChecklistPrintSettingGetMemento {

	/** The entity. */
	private QismtChecklistPrintSet entity;

	/**
	 * Instantiates a new jpa checklist print setting get memento.
	 *
	 * @param entity the entity
	 */
	public JpaChecklistPrintSettingGetMemento(QismtChecklistPrintSet entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
	 * getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.entity.getCcd();
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
	 * getShowCategoryInsuranceItem()
	 */
	@Override
	public Boolean getShowCategoryInsuranceItem() {
		return JpaUtil.short2Boolean(this.entity.getIsShowBreakdownItem());
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
	 * getShowDeliveryNoticeAmount()
	 */
	@Override
	public Boolean getShowDeliveryNoticeAmount() {
		return JpaUtil.short2Boolean(this.entity.getIsShowTotalPayMny());
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
	 * getShowDetail()
	 */
	@Override
	public Boolean getShowDetail() {
		return JpaUtil.short2Boolean(this.entity.getIsShowPersonSumMny());
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
	 * getShowOffice()
	 */
	@Override
	public Boolean getShowOffice() {
		return JpaUtil.short2Boolean(this.entity.getIsShowTotalMnyCol());
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
	 * getShowTotal()
	 */
	@Override
	public Boolean getShowTotal() {
		return JpaUtil.short2Boolean(this.entity.getIsShowOfficeSumMny());
	}

}
