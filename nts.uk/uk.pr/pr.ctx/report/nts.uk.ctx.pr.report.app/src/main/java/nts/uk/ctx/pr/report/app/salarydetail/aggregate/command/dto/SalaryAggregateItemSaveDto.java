/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.aggregate.command.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;
import nts.uk.ctx.pr.report.app.salarydetail.aggregate.find.dto.SalaryItemDto;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItem;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemCode;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemGetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemHeader;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemHeaderGetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemName;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.TaxDivision;
import nts.uk.ctx.pr.report.dom.salarydetail.item.SalaryItem;

/**
 * The Class SalaryAggregateItemSaveDto.
 */
@Data
public class SalaryAggregateItemSaveDto {

	/** The salary aggregate item code. */
	private String salaryAggregateItemCode;

	/** The salary aggregate item name. */
	private String salaryAggregateItemName;

	/** The sub item codes. */
	private List<SalaryItemDto> subItemCodes;

	/** The tax division. */
	private int taxDivision;

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the salary aggregate item
	 */
	public SalaryAggregateItem toDomain(String companyCode) {
		return new SalaryAggregateItem(new SalaryAggregateItemGetMementoImpl(companyCode, this));
	}

	/**
	 * The Class SalaryAggregateItemGetMementoImpl.
	 */
	public class SalaryAggregateItemGetMementoImpl implements SalaryAggregateItemGetMemento {

		/** The company code. */
		private String companyCode;

		/** The dto. */
		private SalaryAggregateItemSaveDto dto;

		/**
		 * Instantiates a new salary aggregate item get memento impl.
		 *
		 * @param companyCode
		 *            the company code
		 * @param dto
		 *            the dto
		 */
		public SalaryAggregateItemGetMementoImpl(String companyCode, SalaryAggregateItemSaveDto dto) {
			this.dto = dto;
			this.companyCode = companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
		 * SalaryAggregateItemGetMemento#getSalaryAggregateItemName()
		 */
		@Override
		public SalaryAggregateItemName getSalaryAggregateItemName() {
			return new SalaryAggregateItemName(this.dto.salaryAggregateItemName);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
		 * SalaryAggregateItemGetMemento#getSubItemCodes()
		 */
		@Override
		public Set<SalaryItem> getSubItemCodes() {
			return this.dto.getSubItemCodes().stream().map(itemCode -> {
				SalaryItem salaryItem = new SalaryItem();
				salaryItem.setSalaryItemCode(itemCode.getCode());
				salaryItem.setSalaryItemName(itemCode.getName());
				return salaryItem;
			}).collect(Collectors.toSet());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
		 * SalaryAggregateItemGetMemento#getSalaryAggregateItem()
		 */
		@Override
		public SalaryAggregateItemHeader getSalaryAggregateItemHeader() {
			return new SalaryAggregateItemHeader(
					new SalaryAggregateItemHeaderGetMementoImpl(this.companyCode, this.dto));
		}

		/**
		 * The Class SalaryAggregateItemHeaderGetMementoImpl.
		 */
		private class SalaryAggregateItemHeaderGetMementoImpl implements SalaryAggregateItemHeaderGetMemento {

			/** The dto. */
			private SalaryAggregateItemSaveDto dto;

			/** The company code. */
			private String companyCode;

			/**
			 * Instantiates a new salary aggregate item header get memento impl.
			 *
			 * @param companyCode
			 *            the company code
			 * @param dto
			 *            the dto
			 */
			public SalaryAggregateItemHeaderGetMementoImpl(String companyCode, SalaryAggregateItemSaveDto dto) {
				this.companyCode = companyCode;
				this.dto = dto;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
			 * SalaryAggregateItemHeaderGetMemento#getCompanyCode()
			 */
			@Override
			public String getCompanyCode() {
				return this.companyCode;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
			 * SalaryAggregateItemHeaderGetMemento#getSalaryAggregateItemCode()
			 */
			@Override
			public SalaryAggregateItemCode getSalaryAggregateItemCode() {
				return new SalaryAggregateItemCode(this.dto.salaryAggregateItemCode);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
			 * SalaryAggregateItemHeaderGetMemento#getTaxDivision()
			 */
			@Override
			public TaxDivision getTaxDivision() {
				return TaxDivision.valueOf(this.dto.taxDivision);
			}

		}

	}

}
