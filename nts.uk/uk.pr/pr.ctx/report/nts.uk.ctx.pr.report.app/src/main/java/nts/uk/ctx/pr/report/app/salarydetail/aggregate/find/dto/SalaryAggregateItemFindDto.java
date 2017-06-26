/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.aggregate.find.dto;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemHeader;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemName;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemSetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.TaxDivision;
import nts.uk.ctx.pr.report.dom.salarydetail.item.SalaryItem;

/**
 * The Class SalaryAggregateItemFindDto.
 */

@Getter
@Setter
public class SalaryAggregateItemFindDto implements SalaryAggregateItemSetMemento {

	/** The salary aggregate item code. */
	private String salaryAggregateItemCode;

	/** The salary aggregate item name. */
	private String salaryAggregateItemName;

	/** The tax division. */
	private TaxDivision taxDivision;

	/** The sub item codes. */
	private List<SalaryItemDto> subItemCodes;

	/**
	 * Default data.
	 *
	 * @param dto
	 *            the dto
	 */
	public void defaultData(SalaryAggregateItemInDto dto) {
		this.salaryAggregateItemCode = dto.getAggregateItemCode();
		this.salaryAggregateItemName = dto.getAggregateItemCode();
		this.taxDivision = TaxDivision.valueOf(dto.getTaxDivision());
		this.subItemCodes = Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemSetMemento#setSalaryAggregateItemName(nts.uk.ctx.pr.
	 * report.dom.salarydetail.aggregate.SalaryAggregateItemName)
	 */
	@Override
	public void setSalaryAggregateItemName(SalaryAggregateItemName salaryAggregateItemName) {
		this.salaryAggregateItemName = salaryAggregateItemName.v();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemSetMemento#setSubItemCodes(java.util.Set)
	 */
	@Override
	public void setSubItemCodes(Set<SalaryItem> subItemCodes) {
		this.subItemCodes = subItemCodes.stream().map(item -> {
			SalaryItemDto salaryItemDto = new SalaryItemDto();
			salaryItemDto.setCode(item.getSalaryItemCode());
			salaryItemDto.setName(item.getSalaryItemName());
			return salaryItemDto;
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemSetMemento#setSalaryAggregateItemHeader(nts.uk.ctx.pr.
	 * report.dom.salarydetail.aggregate.SalaryAggregateItemHeader)
	 */
	@Override
	public void setSalaryAggregateItemHeader(SalaryAggregateItemHeader header) {
		this.salaryAggregateItemCode = header.getAggregateItemCode().v();
		this.taxDivision = header.getTaxDivision();
	}

}
