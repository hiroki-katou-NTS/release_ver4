/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.annualpaidleave;

import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSettingGetMemento;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.ManageAnnualSetting;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.annualpaidleave.KmfmtAnnualPaidLeave;

/**
 * The Class JpaAnnualPaidLeaveSettingGetMemento.
 */
public class JpaAnnualPaidLeaveSettingGetMemento implements AnnualPaidLeaveSettingGetMemento {

	/** The entity. */
	@Inject
	private KmfmtAnnualPaidLeave entity;

	/**
	 * Instantiates a new jpa annual paid leave setting get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaAnnualPaidLeaveSettingGetMemento(KmfmtAnnualPaidLeave entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.vacation.setting.annualpaidleave.
	 * AnnualPaidLeaveSettingGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return entity.getCid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.vacation.setting.annualpaidleave.
	 * AnnualPaidLeaveSettingGetMemento#getYearManageType()
	 */
	@Override
	public ManageDistinct getYearManageType() {
		return ManageDistinct.valueOf(entity.getManageAtr());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.vacation.setting.annualpaidleave.
	 * AnnualPaidLeaveSettingGetMemento#getYearManageSetting()
	 */
	@Override
	public ManageAnnualSetting getYearManageSetting() {
		return null;
	}
}
