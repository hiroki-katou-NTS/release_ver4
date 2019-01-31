/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.find.optitem.calculation;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.optitem.calculation.AddSubOperator;
import nts.uk.ctx.at.record.dom.optitem.calculation.SelectedAttendanceItemSetMemento;

/**
 * The Class SelectedAttendanceItemDto.
 */
@Getter
@Setter
public class SelectedAttendanceItemDto implements SelectedAttendanceItemSetMemento {

	/** The attendance item id. */
	private int attendanceItemId;

	/** The operator. */
	private int operator;

	/** The attendance item name. */
	private String attendanceItemName;

	/** The operator text. */
	private String operatorText;
	
	/** The attendance item display number. */
	private Integer attendanceItemDisplayNumber;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.optitem.calculation.
	 * SelectedAttendanceItemSetMemento#setAttItemId(java.lang.String)
	 */
	@Override
	public void setAttItemId(int id) {
		this.attendanceItemId = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.optitem.calculation.
	 * SelectedAttendanceItemSetMemento#setOperator(nts.uk.ctx.at.record.dom.
	 * optitem.calculation.AddSubOperator)
	 */
	@Override
	public void setOperator(AddSubOperator operator) {
		this.operator = operator.value;
	}
}
