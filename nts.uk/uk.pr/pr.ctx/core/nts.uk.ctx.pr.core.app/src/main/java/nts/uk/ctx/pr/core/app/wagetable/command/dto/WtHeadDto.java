/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementCount;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.WtHead;
import nts.uk.ctx.pr.core.dom.wagetable.WtHeadGetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.WtHeadSetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.WtName;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class WageTableHeadDto.
 */
@Getter
@Setter
public class WtHeadDto {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/** The mode. */
	private Integer mode;

	/** The memo. */
	private String memo;

	/** The elements. */
	private List<WtElementDto> elements;

	/**
	 * To domain.
	 *
	 * @return the wt head
	 */
	public WtHead toDomain() {
		WtHeadDto dto = this;

		// Transfer data
		WtHead wageTableHead = new WtHead(new WhdGetMemento(dto));

		return wageTableHead;
	}

	/**
	 * From domain.
	 *
	 * @param wtHead
	 *            the wt head
	 * @return the wt head dto
	 */
	public WtHeadDto fromDomain(WtHead wtHead) {
		WtHeadDto dto = this;

		wtHead.saveToMemento(new WhdSetMemento(dto));

		return dto;
	}

	/**
	 * The Class WageTableHeadDtoMemento.
	 */
	private class WhdGetMemento implements WtHeadGetMemento {

		/** The dto. */
		private WtHeadDto dto;

		/**
		 * Instantiates a new wage table head dto memento.
		 *
		 * @param companyCode
		 *            the company code
		 * @param dto
		 *            the dto
		 */
		public WhdGetMemento(WtHeadDto dto) {
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.WageTableHeadGetMemento#
		 * getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return AppContexts.user().companyCode();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.WageTableHeadGetMemento#getCode()
		 */
		@Override
		public WtCode getCode() {
			return new WtCode(this.dto.getCode());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.WageTableHeadGetMemento#getName()
		 */
		@Override
		public WtName getName() {
			return new WtName(this.dto.getName());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.WageTableHeadGetMemento#getMemo()
		 */
		@Override
		public Memo getMemo() {
			return new Memo(this.dto.getMemo());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.WtHeadGetMemento#getMode()
		 */
		@Override
		public ElementCount getMode() {
			return ElementCount.valueOf(this.dto.getMode());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.core.dom.wagetable.WtHeadGetMemento#getElements()
		 */
		@Override
		public List<WtElement> getElements() {
			return this.dto.getElements().stream()
					.map(item -> new WtElement(DemensionNo.valueOf(item.getDemensionNo()),
							ElementType.valueOf(item.getType()), item.getReferenceCode()))
					.collect(Collectors.toList());
		}
	}

	/**
	 * The Class WageTableHistoryDtoGetMemento.
	 */
	private class WhdSetMemento implements WtHeadSetMemento {

		/** The dto. */
		private WtHeadDto dto;

		/**
		 * Instantiates a new wage table history dto get memento.
		 *
		 * @param wageTableCode
		 *            the wage table code
		 * @param dto
		 *            the dto
		 */
		public WhdSetMemento(WtHeadDto dto) {
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.WtHeadSetMemento#setCompanyCode(nts.
		 * uk. ctx.core.dom.company.CompanyCode)
		 */
		@Override
		public void setCompanyCode(String companyCode) {
			// Do nothing.
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.WtHeadSetMemento#setCode(nts.uk.ctx.
		 * pr. core.dom.wagetable.WtCode)
		 */
		@Override
		public void setCode(WtCode code) {
			this.dto.code = code.v();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.WtHeadSetMemento#setName(nts.uk.ctx.
		 * pr. core.dom.wagetable.WtName)
		 */
		@Override
		public void setName(WtName name) {
			this.dto.name = name.v();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.WtHeadSetMemento#setMemo(nts.uk.shr.
		 * com. primitive.Memo)
		 */
		@Override
		public void setMemo(Memo memo) {
			this.dto.memo = memo.v();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.WtHeadSetMemento#setMode(nts.uk.ctx.
		 * pr. core.dom.wagetable.ElementCount)
		 */
		@Override
		public void setMode(ElementCount mode) {
			this.dto.mode = mode.value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.wagetable.WtHeadSetMemento#setElements(java.
		 * util. List)
		 */
		@Override
		public void setElements(List<WtElement> elements) {
			this.dto.elements = elements.stream()
					.map(item -> WtElementDto.builder().demensionNo(item.getDemensionNo().value)
							.type(item.getType().value).referenceCode(item.getReferenceCode())
							.build())
					.collect(Collectors.toList());
		}
	}

}
