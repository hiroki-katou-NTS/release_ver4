package nts.uk.ctx.at.record.ws.monthlyclosureupdate;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.arc.task.AsyncTaskInfo;
import nts.uk.ctx.at.record.app.command.monthlyclosureupdate.CheckMonthlyClosureCommandHandler;
import nts.uk.ctx.at.record.app.command.monthlyclosureupdate.ConfirmCompleteMonthlyUpdateCommandHandler;
import nts.uk.ctx.at.record.app.command.monthlyclosureupdate.ExecuteMonthlyClosureCommandHandler;
import nts.uk.ctx.at.record.app.command.monthlyclosureupdate.MonthlyClosureResponse;
import nts.uk.ctx.at.record.app.find.monthlyclosureupdate.Kmw006aResultDto;
import nts.uk.ctx.at.record.app.find.monthlyclosureupdate.Kmw006cDto;
import nts.uk.ctx.at.record.app.find.monthlyclosureupdate.Kmw006fResultDto;
import nts.uk.ctx.at.record.app.find.monthlyclosureupdate.MonthlyClosureUpdateFinder;
import nts.uk.ctx.at.record.app.find.monthlyclosureupdate.MonthlyClosureUpdateLogDto;
import nts.uk.ctx.at.record.app.find.monthlyclosureupdate.exportcsv.MonthlyClosureUpdateLogExportService;
import nts.uk.ctx.at.record.app.find.monthlyclosureupdate.exportcsv.MontlyClosureUpdateExportDto;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordImport;

/**
 * 
 * @author HungTT
 *
 */

@Path("at/record/monthlyclosure")
@Produces("application/json")
public class MonthlyClosureUpdateWebService extends WebService {

	@Inject
	private ExecuteMonthlyClosureCommandHandler executeHandler;

	@Inject
	private CheckMonthlyClosureCommandHandler checkHandler;

	@Inject
	private MonthlyClosureUpdateFinder monthlyClosureFinder;

	@Inject
	private ConfirmCompleteMonthlyUpdateCommandHandler completeHandler;
	
	@Inject
	private MonthlyClosureUpdateLogExportService exportService;

	@POST
	@Path("execution")
	public AsyncTaskInfo executeMonthlyClosureUpdate(MonthlyClosureResponse command) {
		return executeHandler.handle(command);
	}

	@POST
	@Path("checkStatus/{closureId}")
	public MonthlyClosureResponse checkMonthlyClosureUpdate(@PathParam("closureId") int closureId) {
		return checkHandler.handle(closureId);
	}

	@POST
	@Path("getMonthlyClosure/{monthlyClosureId}")
	public MonthlyClosureUpdateLogDto checkMonthlyClosureUpdate(@PathParam("monthlyClosureId") String id) {
		return monthlyClosureFinder.findById(id);
	}

	@POST
	@Path("completeConfirm/{monthlyClosureId}")
	public void completeConfirm(@PathParam("monthlyClosureId") String id) {
		completeHandler.handle(id);
	}

	@POST
	@Path("getResults/{monthlyClosureId}/{atr}")
	public Kmw006fResultDto getResults(@PathParam("monthlyClosureId") String id, @PathParam("atr") Integer atr) {
		return monthlyClosureFinder.getClosureResult(id, atr);
	}

	@POST
	@Path("getInfors")
	public Kmw006aResultDto getInfor() {
		return monthlyClosureFinder.getClosureInfors();
	}

	@POST
	@Path("getLogInfors")
	public List<Kmw006cDto> getLogInfor() {
		return monthlyClosureFinder.getClosureLogInfor();
	}

	@POST
	@Path("getListEmpIdByMonthlyClosureLogId/{monthlyClosureId}/{atr}")
	public List<String> getListEmployeeId(@PathParam("monthlyClosureId") String logId, @PathParam("atr") int atr) {
		return monthlyClosureFinder.getListExecutedEmployeeId(logId, atr);
	}

	@POST
	@Path("getListEmpInfoById")
	public List<EmployeeRecordImport> getListEmployeeId(List<String> listEmpId) {
		return monthlyClosureFinder.getListEmployeeInfo(listEmpId);
	}

	@POST
	@Path("exportLog")
	public ExportServiceResult exportCsvErrorInfor(MontlyClosureUpdateExportDto command) {
		return this.exportService.start(command);
	}
	
}
