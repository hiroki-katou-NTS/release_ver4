/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.aggregate.command;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.report.app.salarydetail.aggregate.command.dto.SalaryAggregateItemSaveDto;

/**
 * The Class CheckListPrintSettingSaveCommand.
 */
@Getter
@Setter
public class SalaryAggregateItemSaveCommand implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The labor insurance office. */
	private SalaryAggregateItemSaveDto salaryAggregateItemSaveDto;
}
