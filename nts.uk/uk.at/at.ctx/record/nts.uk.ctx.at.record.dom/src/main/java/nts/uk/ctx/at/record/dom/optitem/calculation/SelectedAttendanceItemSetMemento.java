/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optitem.calculation;

/**
 * The Interface SelectedAttendanceItemSetMemento.
 */
public interface SelectedAttendanceItemSetMemento {

	/**
	 * Sets the att item id.
	 *
	 * @param id the new att item id
	 */
	void setAttItemId(String id);

	/**
	 * Sets the operator.
	 *
	 * @param operator the new operator
	 */
	void setOperator(AddSubOperator operator);
}
