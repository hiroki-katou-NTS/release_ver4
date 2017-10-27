/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.shift.totaltimes;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.CountAtr;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.SummaryAtr;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalCondition;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalSubjects;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimes;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesABName;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesGetMemento;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesName;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.UseAtr;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class TotalTimesCommand.
 */

/**
 * Sets the list total subjects.
 *
 * @param listTotalSubjects the new list total subjects
 */
@Setter

/**
 * Gets the list total subjects.
 *
 * @return the list total subjects
 */
@Getter
public class TotalTimesCommand {

	/** The total count no. */
	private Integer totalCountNo;

	/** The count atr. */
	private Integer countAtr;

	/** The use atr. */
	private Integer useAtr;

	/** The total times name. */
	private String totalTimesName;

	/** The total times AB name. */
	private String totalTimesABName;

	/** The summary atr. */
	private Integer summaryAtr;

	/** The total condition. */
	private TotalConditionDto totalCondition;

	/** The list total subjects. */
	private List<TotalSubjectsDto> listTotalSubjects;

	/**
	 * To domain.
	 *
	 * @param companyId the company id
	 * @return the total times
	 */
	public TotalTimes toDomain(String companyId) {
		return new TotalTimes(new DtoGetMemento(companyId, this));
	}

	/**
	 * The Class DtoGetMemento.
	 */
	private class DtoGetMemento implements TotalTimesGetMemento {

		/** The company id. */
		private String companyId;

		/** The command. */
		private TotalTimesCommand command;

		/**
		 * Instantiates a new dto get memento.
		 *
		 * @param companyId the company id
		 * @param command the command
		 */
		public DtoGetMemento(String companyId, TotalTimesCommand command) {
			this.companyId = companyId;
			this.command = command;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesGetMemento#
		 * getCompanyId()
		 */
		@Override
		public CompanyId getCompanyId() {
			return new CompanyId(this.companyId);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesGetMemento#
		 * getTotalCountNo()
		 */
		@Override
		public Integer getTotalCountNo() {
			return this.command.getTotalCountNo();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesGetMemento#
		 * getCountAtr()
		 */
		@Override
		public CountAtr getCountAtr() {
			return CountAtr.valueOf(this.command.getCountAtr());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesGetMemento#
		 * getUseAtr()
		 */
		@Override
		public UseAtr getUseAtr() {
			return UseAtr.valueOf(this.command.getUseAtr());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesGetMemento#
		 * getTotalTimesName()
		 */
		@Override
		public TotalTimesName getTotalTimesName() {
			return new TotalTimesName(this.command.getTotalTimesName());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesGetMemento#
		 * getTotalTimesABName()
		 */
		@Override
		public TotalTimesABName getTotalTimesABName() {
			return new TotalTimesABName(this.command.getTotalTimesABName());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesGetMemento#
		 * getSummaryAtr()
		 */
		@Override
		public SummaryAtr getSummaryAtr() {
			return SummaryAtr.valueOf(this.command.getSummaryAtr());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesGetMemento#
		 * getTotalCondition()
		 */
		@Override
		public TotalCondition getTotalCondition() {
			return this.command.getTotalCondition().toDomain();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesGetMemento#
		 * getTotalSubjects()
		 */
		@Override
		public List<TotalSubjects> getTotalSubjects() {
			return this.command.listTotalSubjects.stream().map(TotalSubjectsDto::toDomain)
					.collect(Collectors.toList());
		}

	}

}
