/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.dom.attendanceItemAndFrameLinking.repository;

import java.util.List;

import nts.uk.ctx.at.function.dom.attendanceItemAndFrameLinking.AttendanceItemLinking;
import nts.uk.ctx.at.function.dom.attendanceItemAndFrameLinking.enums.TypeOfItem;

public interface AttendanceItemLinkingRepository {
	
	List<AttendanceItemLinking> getByAttendanceId(List<Integer> attendanceItemIds);
	
	/**
	 * Gets the by any item category.
	 *
	 * @param type the type
	 * @return the by any item category
	 * 
	 * @author anhnm
	 */
	List<AttendanceItemLinking> getByAnyItemCategory(TypeOfItem type);
}
