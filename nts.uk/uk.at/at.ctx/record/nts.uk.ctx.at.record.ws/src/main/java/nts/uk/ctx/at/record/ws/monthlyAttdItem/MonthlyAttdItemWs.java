/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.ws.monthlyAttdItem;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.find.dailyperformanceformat.AttdItemLinkRequest;
import nts.uk.ctx.at.record.app.find.dailyperformanceformat.dto.AttdItemDto;
import nts.uk.ctx.at.record.app.find.monthlyattditem.MonthlyAttendanceItemFinder;

/**
 * The Class MonthlyAttdItemWs.
 */
@Path("at/record/attendanceitem/monthly")
@Produces("application/json")
public class MonthlyAttdItemWs extends WebService {

	/** The finder. */
	@Inject
	private MonthlyAttendanceItemFinder finder;

	/**
	 * Find by any item.
	 *
	 * @param request the request
	 * @return the list
	 */
	@POST
	@Path("findbyanyitem")
	public List<AttdItemDto> findByAnyItem(AttdItemLinkRequest request) {
		return this.finder.findByAnyItem(request);
	}
}
