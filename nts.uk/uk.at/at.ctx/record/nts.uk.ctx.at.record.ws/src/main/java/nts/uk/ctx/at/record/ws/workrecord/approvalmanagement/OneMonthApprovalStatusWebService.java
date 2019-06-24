/**
 * 10:16:23 AM Mar 13, 2018
 */
package nts.uk.ctx.at.record.ws.workrecord.approvalmanagement;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.find.approvalmanagement.OneMonthApprovalStatusFinder;
import nts.uk.ctx.at.record.app.find.approvalmanagement.OneMonthApprovalStatusRequest;
import nts.uk.ctx.at.record.dom.approvalmanagement.dtos.OneMonthApprovalStatusDto;
//import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author hungnm
 *
 */
@Path("at/record/workrecord/approvalmanagement/")
@Produces("application/json")
public class OneMonthApprovalStatusWebService extends WebService {

	@Inject
	private OneMonthApprovalStatusFinder oneMonthApprovalStatusFinder;

	@POST
	@Path("startscreen")
	public OneMonthApprovalStatusDto startScreen(OneMonthApprovalStatusRequest param) {
		return oneMonthApprovalStatusFinder.getOneMonthApprovalStatus(param.getClosureIdParam(),
				param.getStartDateParam(), param.getEndDateParam());
	}

	@POST
	@Path("extractApprovalStatusData")
	public OneMonthApprovalStatusDto extractApprovalStatusData(OneMonthApprovalStatusRequest request) {
		return oneMonthApprovalStatusFinder.getOneMonthApprovalStatus(request.getClosureIdParam(),
				request.getStartDateParam(), request.getEndDateParam());
	}
	
	@POST
	@Path("getdaterange/{closureId}/{currentYearMonth}")
	public OneMonthApprovalStatusDto getDateRange(@PathParam("closureId") int closureId,@PathParam("currentYearMonth") int currentYearMonth) {
		return oneMonthApprovalStatusFinder.getDatePeriod(closureId,currentYearMonth);
	}

}
