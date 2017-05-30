/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.vacation.setting.subst.command.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.vacation.setting.ApplyPermission;
import nts.uk.ctx.at.shared.dom.vacation.setting.ExpirationTime;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationGetMemento;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.SubstVacationSetting;

/**
 * The Class EmpSubstVacationDto.
 */
@Getter
@Setter
public class EmpSubstVacationDto extends SubstVacationDto {

	/** The is manage. */
	private String contractTypeCode;

	/**
	 * To domain.
	 *
	 * @param companyId
	 *            the company id
	 * @return the com subst vacation
	 */
	public EmpSubstVacation toEmpSubstDomain(String companyId) {
		return new EmpSubstVacation(new EmpSvGetMementoImpl(companyId, this));
	}

	/**
	 * The Class ComSvGetMementoImpl.
	 */
	private class EmpSvGetMementoImpl implements EmpSubstVacationGetMemento {

		/** The company id. */
		private String companyId;

		/** The dto. */
		private EmpSubstVacationDto dto;

		/**
		 * Instantiates a new cg set memento.
		 *
		 * @param dto
		 *            the dto
		 */
		public EmpSvGetMementoImpl(String companyId, EmpSubstVacationDto dto) {
			this.companyId = companyId;
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
		 * ComSubstVacationGetMemento#getCompanyId()
		 */
		@Override
		public String getCompanyId() {
			return this.companyId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
		 * EmpSubstVacationGetMemento#getEmpContractTypeCode()
		 */
		@Override
		public String getEmpContractTypeCode() {
			return this.dto.contractTypeCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
		 * ComSubstVacationGetMemento#getSetting()
		 */
		@Override
		public SubstVacationSetting getSetting() {
			return new SubstVacationSetting(ManageDistinct.valueOf(this.dto.getIsManage()),
					ExpirationTime.valueOf(this.dto.getExpirationDate()),
					ApplyPermission.valueOf(this.dto.getAllowPrepaidLeave()));
		}

	}

}
