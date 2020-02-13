/**
 * 10:16:23 AM Mar 13, 2018
 */
package nts.uk.ctx.at.record.ws.workrecord.approvalmanagement;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.find.approvalmanagement.OneMonthApprovalStatusFinder;
import nts.uk.ctx.at.record.app.find.approvalmanagement.OneMonthApprovalStatusRequest;
import nts.uk.ctx.at.record.dom.approvalmanagement.dtos.OneMonthApprovalStatusDto;
//import nts.arc.time.calendar.period.DatePeriod;

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
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public OneMonthApprovalStatusDto startScreen(OneMonthApprovalStatusRequest param) {
		return oneMonthApprovalStatusFinder.getOneMonthApprovalStatus(param.getClosureIdParam(),
				param.getYearMonth());
	}

	@POST
	@Path("extractApprovalStatusData")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public OneMonthApprovalStatusDto extractApprovalStatusData(OneMonthApprovalStatusRequest request) {
		return oneMonthApprovalStatusFinder.getOneMonthApprovalStatus(request.getClosureIdParam(),
				request.getYearMonth());
	}
	
	@POST
	@Path("getdaterange/{closureId}/{currentYearMonth}")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public OneMonthApprovalStatusDto getDateRange(@PathParam("closureId") int closureId,@PathParam("currentYearMonth") int currentYearMonth) {
		return oneMonthApprovalStatusFinder.getDatePeriod(closureId,currentYearMonth);
	}
	
	@POST
	@Path("changeCondition")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public OneMonthApprovalStatusDto changeCondition(OneMonthApprovalStatusRequest request) {
		return oneMonthApprovalStatusFinder.changeConditionExtract(request.getClosureIdParam(), request.getYearMonth());
	}

}
