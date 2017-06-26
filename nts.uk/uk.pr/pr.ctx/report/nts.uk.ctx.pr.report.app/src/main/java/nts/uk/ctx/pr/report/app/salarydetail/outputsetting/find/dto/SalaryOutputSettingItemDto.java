/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.outputsetting.find.dto;

import lombok.Builder;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryItemType;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItemSetMemento;

/**
 * The Class SalaryOutputSettingItemDto.
 */
@Builder
public class SalaryOutputSettingItemDto implements SalaryOutputItemSetMemento {

	/** The code. */
	public String code;

	/** The name. */
	public String name;

	/** The is aggregate item. */
	public Boolean isAggregateItem;

	/** The order number. */
	public int orderNumber;

	@Override
	public void setLinkageCode(String linkageCode) {
		this.code = linkageCode;
	}

	@Override
	public void setType(SalaryItemType salaryItemType) {
		this.isAggregateItem = SalaryItemType.Aggregate == salaryItemType;
	}

	@Override
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

}
