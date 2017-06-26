/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import java.util.List;

import nts.uk.ctx.pr.report.dom.salarydetail.SalaryCategory;

/**
 * The Interface SalaryCategorySettingSetMemento.
 */
public interface SalaryCategorySettingSetMemento {

	/**
	 * Sets the salary category.
	 *
	 * @param salaryCategory the new salary category
	 */
	 void setSalaryCategory(SalaryCategory salaryCategory);

	/**
	 * Sets the salary output items.
	 *
	 * @param listSalaryOutputItem the new salary output items
	 */
	 void setSalaryOutputItems(List<SalaryOutputItem>  listSalaryOutputItem);
}
