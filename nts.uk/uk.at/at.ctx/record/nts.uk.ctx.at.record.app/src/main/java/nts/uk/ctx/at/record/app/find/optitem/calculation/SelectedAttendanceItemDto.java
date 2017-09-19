/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
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
	// 勤怠項目ID
	private String id;

	/** The operator. */
	// 演算子
	private int operator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.optitem.calculation.
	 * SelectedAttendanceItemSetMemento#setAttItemId(java.lang.String)
	 */
	@Override
	public void setAttItemId(String id) {
		this.id = id;
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
