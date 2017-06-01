/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.vacation.setting.sixtyhours.command;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.SixtyHourExtra;
import nts.uk.ctx.at.shared.dom.vacation.setting.TimeDigestiveUnit;
import nts.uk.ctx.at.shared.dom.vacation.setting.sixtyhours.Com60HourVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.sixtyhours.Com60HourVacationGetMemento;
import nts.uk.ctx.at.shared.dom.vacation.setting.sixtyhours.SixtyHourVacationSetting;

/**
 * The Class ComSubstVacationSaveCommand.
 */
@Getter
@Setter
public class Com60HourVacationSaveCommand extends SixtyHourVacationSaveBaseCommand {

	/**
	 * To domain.
	 *
	 * @param companyId
	 *            the company id
	 * @return the com subst vacation
	 */
	public Com60HourVacation toDomain(String companyId) {
		return new Com60HourVacation(new Com60HGetMementoImpl(companyId, this));
	}

	/**
	 * The Class ComSvGetMementoImpl.
	 */
	private class Com60HGetMementoImpl implements Com60HourVacationGetMemento {

		/** The company id. */
		private String companyId;

		/** The dto. */
		private Com60HourVacationSaveCommand dto;

		/**
		 * Instantiates a new cg set memento.
		 *
		 * @param dto
		 *            the dto
		 */
		public Com60HGetMementoImpl(String companyId, Com60HourVacationSaveCommand dto) {
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
		 * ComSubstVacationGetMemento#getSetting()
		 */
		@Override
		public SixtyHourVacationSetting getSetting() {
			return new SixtyHourVacationSetting(ManageDistinct.valueOf(this.dto.getIsManage()),
					SixtyHourExtra.valueOf(this.dto.getSixtyHourExtra()),
					TimeDigestiveUnit.valueOf(this.dto.getDigestiveUnit()));
		}
	}

}
