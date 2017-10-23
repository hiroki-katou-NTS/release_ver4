/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.monthlyattendanceitem;

import java.util.List;

/**
 * The Interface MonthlyAttendanceItemRepository.
 */
public interface MonthlyAttendanceItemRepository {

	/**
	 * Find by atr.
	 *
	 * @param companyId the company id
	 * @param itemAtr the item atr
	 * @return the list
	 */
	List<MonthlyAttendanceItem> findByAtr(String companyId, int itemAtr);

	/**
	 * Find all.
	 *
	 * @param companyId the company id
	 * @return the list
	 */
	List<MonthlyAttendanceItem> findAll(String companyId);

}
