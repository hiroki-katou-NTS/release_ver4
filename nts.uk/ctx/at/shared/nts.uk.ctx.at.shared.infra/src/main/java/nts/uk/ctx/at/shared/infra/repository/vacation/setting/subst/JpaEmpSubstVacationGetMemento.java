/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.subst;

import nts.uk.ctx.at.shared.dom.vacation.setting.ApplyPermission;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationGetMemento;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.SubstVacationSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.VacationExpiration;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.subst.KsvstEmpSubstVacation;

/**
 * The Class JpaEmpSubstVacationGetMemento.
 */
public class JpaEmpSubstVacationGetMemento implements EmpSubstVacationGetMemento {

	/** The type value. */
	private KsvstEmpSubstVacation typeValue;

	/**
	 * Instantiates a new jpa emp subst vacation get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaEmpSubstVacationGetMemento(KsvstEmpSubstVacation typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
	 * EmpSubstVacationGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return this.typeValue.getKclstEmpSubstVacationPK().getCid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
	 * EmpSubstVacationGetMemento#getEmpContractTypeCode()
	 */
	@Override
	public String getEmpContractTypeCode() {
		return this.typeValue.getKclstEmpSubstVacationPK().getContractTypeCd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
	 * EmpSubstVacationGetMemento#getSetting()
	 */
	@Override
	public SubstVacationSetting getSetting() {
		return new SubstVacationSetting(ManageDistinct.valueOf(this.typeValue.getIsManage()),
				VacationExpiration.valueOf(this.typeValue.getExpirationDateSet()),
				ApplyPermission.valueOf(this.typeValue.getAllowPrepaidLeave()));
	}

}
