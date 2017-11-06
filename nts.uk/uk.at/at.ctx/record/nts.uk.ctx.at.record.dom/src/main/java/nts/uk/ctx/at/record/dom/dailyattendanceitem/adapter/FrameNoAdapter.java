/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.dailyattendanceitem.adapter;

import java.util.List;

public interface FrameNoAdapter {
	
	List<FrameNoAdapterDto> getFrameNo(List<Integer> attendanceItemIds);

	/**
	 * Gets the by any item.
	 *
	 * @param typeOfItem the type of item
	 * @return the by any item
	 * 
	 * @author anhnm
	 */
	List<FrameNoAdapterDto> getByAnyItem(int typeOfItem);
}
