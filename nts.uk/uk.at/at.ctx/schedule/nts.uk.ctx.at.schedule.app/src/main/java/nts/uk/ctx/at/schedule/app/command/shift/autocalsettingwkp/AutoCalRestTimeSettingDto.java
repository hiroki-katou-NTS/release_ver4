/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.shift.autocalsettingwkp;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalRestTimeSetting;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalRestTimeSettingGetMemento;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.TimeLimitUpperLimitSetting;

/**
 * Gets the late night time.
 *
 * @return the late night time
 */
@Getter

/**
 * Sets the late night time.
 *
 * @param lateNightTime
 *            the new late night time
 */
@Setter
public class AutoCalRestTimeSettingDto {

	/** The rest time. */
	private AutoCalSettingDto restTime;

	/** The late night time. */
	// 休出深夜時間
	private AutoCalSettingDto lateNightTime;

	/**
	 * To domain.
	 *
	 * @param companyId
	 *            the company id
	 * @return the auto cal rest time setting
	 */
	public AutoCalRestTimeSetting toDomain(String companyId) {
		return new AutoCalRestTimeSetting(new DtoGetMemento(companyId, this));
	}

	/**
	 * The Class DtoGetMemento.
	 */
	private class DtoGetMemento implements AutoCalRestTimeSettingGetMemento {

		/** The company id. */
		private String companyId;

		/** The command. */
		private AutoCalRestTimeSettingDto command;

		/**
		 * Instantiates a new dto get memento.
		 *
		 * @param companyId
		 *            the company id
		 * @param command
		 *            the command
		 */
		public DtoGetMemento(String companyId, AutoCalRestTimeSettingDto command) {
			this.companyId = companyId;
			this.command = command;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
		 * AutoCalRestTimeSettingGetMemento#getRestTime()
		 */
		@Override
		public AutoCalSetting getRestTime() {
			return new AutoCalSetting(TimeLimitUpperLimitSetting.valueOf(command.getRestTime().getUpLimitOtSet()),
					AutoCalAtrOvertime.valueOf(command.getRestTime().getCalAtr()));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
		 * AutoCalRestTimeSettingGetMemento#getLateNightTime()
		 */
		@Override
		public AutoCalSetting getLateNightTime() {
			return new AutoCalSetting(TimeLimitUpperLimitSetting.valueOf(command.getLateNightTime().getUpLimitOtSet()),
					AutoCalAtrOvertime.valueOf(command.getLateNightTime().getCalAtr()));
		}
	}

}
