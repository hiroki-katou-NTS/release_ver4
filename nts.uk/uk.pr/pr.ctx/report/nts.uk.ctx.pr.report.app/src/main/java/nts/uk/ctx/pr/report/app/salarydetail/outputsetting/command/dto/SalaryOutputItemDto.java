/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.outputsetting.command.dto;

import lombok.Setter;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryItemType;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItem;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItemGetMemento;

/**
 * The Class SalaryOutputItemDto.
 */
@Setter
public class SalaryOutputItemDto {

	/** The code. */
	private String code;

	/** The is aggregate item. */
	private Boolean isAggregateItem;

	/** The order number. */
	private int orderNumber;

	/**
	 * To domain.
	 *
	 * @return the salary output item
	 */
	public SalaryOutputItem toDomain() {
		return new SalaryOutputItem(new SalaryOutputItemGetMementoImpl(this));
	}

	/**
	 * The Class SalaryOutputItemGetMementoImpl.
	 */
	public class SalaryOutputItemGetMementoImpl implements SalaryOutputItemGetMemento {

		/** The dto. */
		private SalaryOutputItemDto dto;

		/**
		 * Instantiates a new salary output item get memento impl.
		 *
		 * @param dto the dto
		 */
		public SalaryOutputItemGetMementoImpl(SalaryOutputItemDto dto) {
			super();
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
		 * SalaryOutputItemGetMemento#getLinkageCode()
		 */
		@Override
		public String getLinkageCode() {
			return this.dto.code;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
		 * SalaryOutputItemGetMemento#getType()
		 */
		@Override
		public SalaryItemType getType() {
			return this.dto.isAggregateItem ? SalaryItemType.Aggregate : SalaryItemType.Master;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
		 * SalaryOutputItemGetMemento#getOrderNumber()
		 */
		@Override
		public int getOrderNumber() {
			return this.dto.orderNumber;
		}

	}
}
