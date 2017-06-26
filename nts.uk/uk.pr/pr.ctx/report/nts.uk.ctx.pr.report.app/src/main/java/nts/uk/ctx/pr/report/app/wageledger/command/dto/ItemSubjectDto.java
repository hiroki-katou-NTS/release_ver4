/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.command.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemCode;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubject;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectGetMemento;

/**
 * The Class ItemSubjectDto.
 */
@Setter
@Getter
public class ItemSubjectDto {
	
	/** The category. */
	private WLCategory category;

	/** The payment type. */
	private PaymentType paymentType;

	/** The code. */
	private String code;

	/**
	 * To domain.
	 *
	 * @param companyCode the company code
	 * @return the WL item subject
	 */
	public WLItemSubject toDomain(String companyCode) {
		return new WLItemSubject(new ItemSubjectGetMemento(this, companyCode));
	}

	/**
	 * The Class ItemSubjectGetMemento.
	 */
	public class ItemSubjectGetMemento implements WLItemSubjectGetMemento {

		/** The command. */
		private ItemSubjectDto command;

		/** The company code. */
		private String companyCode;

		/**
		 * Instantiates a new item subject get memento.
		 *
		 * @param command the command
		 * @param companyCode the company code
		 */
		public ItemSubjectGetMemento(ItemSubjectDto command, String companyCode) {
			super();
			this.command = command;
			this.companyCode = companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectGetMemento
		 * #getCategory()
		 */
		@Override
		public WLCategory getCategory() {
			return this.command.category;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectGetMemento
		 * #getPaymentType()
		 */
		@Override
		public PaymentType getPaymentType() {
			return this.command.paymentType;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectGetMemento
		 * #getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return this.companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectGetMemento
		 * #getCode()
		 */
		@Override
		public WLAggregateItemCode getCode() {
			return new WLAggregateItemCode(this.command.code);
		}

	}
}
