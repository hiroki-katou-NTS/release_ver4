package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import nts.uk.ctx.pr.report.dom.salarydetail.SalaryItemType;

/**
 * The Interface SalaryOutputItemGetMemento.
 */
public interface SalaryOutputItemGetMemento {

	/**
	 * Gets the linkage code.
	 *
	 * @return the linkage code
	 */
	 String getLinkageCode();

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	SalaryItemType getType();
}
