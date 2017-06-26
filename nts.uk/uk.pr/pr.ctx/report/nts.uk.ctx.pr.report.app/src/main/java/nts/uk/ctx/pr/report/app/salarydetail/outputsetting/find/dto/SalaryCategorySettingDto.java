/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.outputsetting.find.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryCategory;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySettingSetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItem;

/**
 * The Class SalaryCategorySettingDto.
 */
@Builder
public class SalaryCategorySettingDto implements SalaryCategorySettingSetMemento {

	/** The category. */
	public SalaryCategory category;

	/** The output items. */
	public List<SalaryOutputSettingItemDto> outputItems;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryCategorySettingSetMemento#setSalaryCategory(nts.uk.ctx.pr.report.
	 * dom.salarydetail.SalaryCategory)
	 */
	@Override
	public void setSalaryCategory(SalaryCategory salaryCategory) {
		this.category = salaryCategory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryCategorySettingSetMemento#setSalaryOutputItems(java.util.List)
	 */
	@Override
	public void setSalaryOutputItems(List<SalaryOutputItem> listSalaryOutputItem) {
		this.outputItems = listSalaryOutputItem.stream().map(item -> {
			SalaryOutputSettingItemDto itemDto = SalaryOutputSettingItemDto.builder().build();
			item.saveToMemento(itemDto);
			return itemDto;
		}).sorted((a, b) -> Integer.compare(a.orderNumber, b.orderNumber)).collect(Collectors.toList());
	}

}
