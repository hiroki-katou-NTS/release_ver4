/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtItemGetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtItemSetMemento;

/**
 * The Class WageTableItemDto.
 */
@Getter
@Setter
public class WtItemDto {

	/** The element 1 id. */
	private String element1Id;

	/** The element 2 id. */
	private String element2Id;

	/** The element 3 id. */
	private String element3Id;

	/** The amount. */
	private BigDecimal amount;

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the wage table history
	 */
	public WtItem toDomain() {
		WtItemDto dto = this;

		// Transfer data
		WtItem wtItem = new WtItem(new WtItemDtoGetMemento(dto));

		return wtItem;
	}

	public WtItemDto fromDomain(WtItem wtHistory) {
		WtItemDto dto = this;

		wtHistory.saveToMemento(new WtItemDtoSetMemento(dto));

		return dto;
	}

	private class WtItemDtoSetMemento implements WtItemSetMemento {

		/** The type value. */
		protected WtItemDto dto;

		/**
		 * Instantiates a new jpa accident insurance rate get memento.
		 *
		 * @param typeValue
		 *            the type value
		 */
		public WtItemDtoSetMemento(WtItemDto dto) {
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtItemSetMemento#
		 * setElement1Id( nts.uk.ctx.pr.core.dom.wagetable.ElementId)
		 */
		@Override
		public void setElement1Id(ElementId element1Id) {
			this.dto.element1Id = element1Id.v();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtItemSetMemento#
		 * setElement2Id( nts.uk.ctx.pr.core.dom.wagetable.ElementId)
		 */
		@Override
		public void setElement2Id(ElementId element2Id) {
			this.dto.element2Id = element2Id.v();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WtItemSetMemento#
		 * setElement3Id( nts.uk.ctx.pr.core.dom.wagetable.ElementId)
		 */
		@Override
		public void setElement3Id(ElementId element3Id) {
			this.dto.element3Id = element3Id.v();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.history.WtItemSetMemento#setAmount(
		 * java. math.BigDecimal)
		 */
		@Override
		public void setAmount(BigDecimal amount) {
			this.dto.amount = amount;
		}
	}

	private class WtItemDtoGetMemento implements WtItemGetMemento {

		/** The type value. */
		protected WtItemDto dto;

		/**
		 * Instantiates a new jpa accident insurance rate get memento.
		 *
		 * @param typeValue
		 *            the type value
		 */
		public WtItemDtoGetMemento(WtItemDto dto) {
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemGetMemento#
		 * getElement1Id()
		 */
		@Override
		public ElementId getElement1Id() {
			return new ElementId(this.dto.element1Id);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemGetMemento#
		 * getElement2Id()
		 */
		@Override
		public ElementId getElement2Id() {
			return new ElementId(this.dto.element2Id);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemGetMemento#
		 * getElement3Id()
		 */
		@Override
		public ElementId getElement3Id() {
			return new ElementId(this.dto.element3Id);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemGetMemento#
		 * getAmount()
		 */
		@Override
		public BigDecimal getAmount() {
			return this.dto.amount;
		}
	}
}