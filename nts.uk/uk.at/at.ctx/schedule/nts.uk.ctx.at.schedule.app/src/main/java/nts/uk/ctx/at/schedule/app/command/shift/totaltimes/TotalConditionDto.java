/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.shift.totaltimes;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.ConditionThresholdLimit;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalCondition;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalConditionGetMemento;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.UseAtr;

/**
 * The Class TotalConditionDto.
 */
@Getter
@Setter
public class TotalConditionDto {

	/** The upper limit setting atr. */
	private Integer upperLimitSettingAtr;

	/** The lower limit setting atr. */
	private Integer lowerLimitSettingAtr;

	/** The thresold upper limit. */
	private Long thresoldUpperLimit;

	/** The thresold lower limit. */
	private Long thresoldLowerLimit;

	/**
	 * To domain.
	 *
	 * @return the total condition
	 */
	public TotalCondition toDomain() {
		return new TotalCondition(new DtoGetMemento(this));
	}

	/**
	 * The Class DtoGetMemento.
	 */
	private class DtoGetMemento implements TotalConditionGetMemento {

		/** The command. */
		private TotalConditionDto command;

		/**
		 * Instantiates a new dto get memento.
		 *
		 * @param command
		 *            the command
		 */
		public DtoGetMemento(TotalConditionDto command) {
			this.command = command;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalConditionGetMemento#
		 * getUpperLimitSettingAtr()
		 */
		@Override
		public UseAtr getUpperLimitSettingAtr() {
			return UseAtr.valueOf(this.command.getUpperLimitSettingAtr());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalConditionGetMemento#
		 * getLowerLimitSettingAtr()
		 */
		@Override
		public UseAtr getLowerLimitSettingAtr() {
			return UseAtr.valueOf(this.command.getLowerLimitSettingAtr());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalConditionGetMemento#
		 * getThresoldUpperLimit()
		 */
		@Override
		public ConditionThresholdLimit getThresoldUpperLimit() {
			return new ConditionThresholdLimit(this.command.getThresoldUpperLimit().intValue());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalConditionGetMemento#
		 * getThresoldLowerLimit()
		 */
		@Override
		public ConditionThresholdLimit getThresoldLowerLimit() {
			return new ConditionThresholdLimit(this.command.getThresoldLowerLimit().intValue());
		}

	}

}
