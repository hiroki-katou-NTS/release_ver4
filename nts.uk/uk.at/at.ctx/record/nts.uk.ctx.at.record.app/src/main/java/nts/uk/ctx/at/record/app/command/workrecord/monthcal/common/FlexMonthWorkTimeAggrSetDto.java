/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.command.workrecord.monthcal.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.AggregateSetting;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.AggregateTimeSetting;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.CarryforwardSetInShortageFlex;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.FlexAggregateMethod;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.ShortageFlexSetting;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.FlexMonthWorkTimeAggrSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.FlexMonthWorkTimeAggrSetGetMemento;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class FlexMonthWorkTimeAggrSetDto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlexMonthWorkTimeAggrSetDto {

	/** The aggr method. */
	private Integer aggrMethod;

	/** The insuffic set. */
	private Integer insufficSet;

	/** The legal aggr set. */
	private Integer legalAggrSet;

	/** The include over time. */
	private Boolean includeOverTime;

	/**
	 * To domain.
	 *
	 * @return the flex month work time aggr set
	 */
	public FlexMonthWorkTimeAggrSet toDomain() {
		return new FlexMonthWorkTimeAggrSet(new GetMementoImpl(this));
	}

	/**
	 * The Class GetMementoImpl.
	 */
	private class GetMementoImpl implements FlexMonthWorkTimeAggrSetGetMemento {

		/** The dto. */
		private FlexMonthWorkTimeAggrSetDto dto;

		/**
		 * Instantiates a new gets the memento impl.
		 *
		 * @param dto
		 *            the dto
		 */
		public GetMementoImpl(FlexMonthWorkTimeAggrSetDto dto) {
			super();
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.
		 * FlexMonthWorkTimeAggrSetGetMemento#getAggrMethod()
		 */
		@Override
		public FlexAggregateMethod getAggrMethod() {
			return EnumAdaptor.valueOf(this.dto.getAggrMethod(), FlexAggregateMethod.class);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.
		 * FlexMonthWorkTimeAggrSetGetMemento#getInsufficSet()
		 */
		@Override
		public ShortageFlexSetting getInsufficSet() {
			return ShortageFlexSetting.of(EnumAdaptor.valueOf(this.dto.getInsufficSet(),
					CarryforwardSetInShortageFlex.class));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.
		 * FlexMonthWorkTimeAggrSetGetMemento#getLegalAggrSet()
		 */
		@Override
		public AggregateTimeSetting getLegalAggrSet() {
			// EA修正履歴No.1357
			return AggregateTimeSetting.of(AggregateSetting.MANAGE_DETAIL);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.
		 * FlexMonthWorkTimeAggrSetGetMemento#getIncludeOverTime()
		 */
		@Override
		public NotUseAtr getIncludeOverTime() {
			return EnumAdaptor.valueOf((this.dto.getIncludeOverTime().booleanValue()) ? 1 : 0, NotUseAtr.class);
		}

	}

}
