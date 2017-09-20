/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.command.optitem.calculation;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.optitem.calculation.ItemSelectionGetMemento;
import nts.uk.ctx.at.record.dom.optitem.calculation.MinusSegment;
import nts.uk.ctx.at.record.dom.optitem.calculation.SelectedAttendanceItem;

/**
 * The Class ItemSelectionDto.
 */
@Getter
@Setter
public class ItemSelectionDto implements ItemSelectionGetMemento {

	/** The minus segment. */
	private int minusSegment;

	/** The selected attendance items. */
	private List<SelectedAttendanceItemDto> attendanceItems;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.ItemSelectionGetMemento#
	 * getMinusSegment()
	 */
	@Override
	public MinusSegment getMinusSegment() {
		return EnumAdaptor.valueOf(this.minusSegment, MinusSegment.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.ItemSelectionGetMemento#
	 * getListSelectedAttendanceItem()
	 */
	@Override
	public List<SelectedAttendanceItem> getListSelectedAttendanceItem() {
		return this.attendanceItems.stream().map(item -> new SelectedAttendanceItem(item))
				.collect(Collectors.toList());
	}
}
