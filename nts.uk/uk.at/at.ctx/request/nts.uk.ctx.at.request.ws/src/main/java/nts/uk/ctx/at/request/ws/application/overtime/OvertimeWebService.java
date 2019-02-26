package nts.uk.ctx.at.request.ws.application.overtime;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.request.app.command.application.overtime.CheckBeforeRegisterOvertime;
import nts.uk.ctx.at.request.app.command.application.overtime.CheckConvertPrePost;
import nts.uk.ctx.at.request.app.command.application.overtime.CreateOvertimeCommand;
import nts.uk.ctx.at.request.app.command.application.overtime.CreateOvertimeCommandHandler;
import nts.uk.ctx.at.request.app.command.application.overtime.UpdateOvertimeCommand;
import nts.uk.ctx.at.request.app.command.application.overtime.UpdateOvertimeCommandHandler;
import nts.uk.ctx.at.request.app.find.application.overtime.AppOvertimeFinder;
import nts.uk.ctx.at.request.app.find.application.overtime.ParamGetOvertime;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OverTimeDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeCheckResultDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.ParamCaculationOvertime;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.ParamChangeAppDate;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.RecordWorkDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.RecordWorkParam;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime;

@Path("at/request/application/overtime")
@Produces("application/json")
public class OvertimeWebService extends WebService{

	@Inject
	private AppOvertimeFinder overtimeFinder;
	@Inject
	private CreateOvertimeCommandHandler createHandler;
	@Inject
	private CheckBeforeRegisterOvertime checkBefore;
	@Inject
	private CheckConvertPrePost checkConvertPrePost;
	
	@Inject
	private UpdateOvertimeCommandHandler updateOvertimeCommandHandler;
	
	@POST
	@Path("getOvertimeByUI")
	public OverTimeDto getOvertimeByUIType(ParamGetOvertime param) {
		return this.overtimeFinder.getOvertimeByUIType(param.getUrl(),
				param.getAppDate(),
				param.getUiType(),
				param.getTimeStart1(),
				param.getTimeEnd1(),
				param.getReasonContent(),
				param.getEmployeeIDs(),
				param.getEmployeeID());
	}
	
	@POST
	@Path("findByChangeAppDate")
	public OverTimeDto findByChangeAppDate(ParamChangeAppDate param) {
		return this.overtimeFinder.findByChangeAppDate(param.getAppDate(), 
				param.getPrePostAtr(),
				param.getSiftCD(),
				param.getOvertimeHours(),
				param.getWorkTypeCode(),
				param.getStartTime(),
				param.getEndTime(),
				param.getStartTimeRests(),
				param.getEndTimeRests(),
				param.getOvertimeAtr());
	}
	@POST
	@Path("checkConvertPrePost")
	public OverTimeDto convertPrePost(ParamChangeAppDate param) {
		return this.checkConvertPrePost.convertPrePost(param.getPrePostAtr(),param.getAppDate(),param.getSiftCD(),param.getOvertimeHours(),param.getWorkTypeCode(),
				param.getStartTime(),
				param.getEndTime(),
				param.getStartTimeRests(),
				param.getEndTimeRests());
	}
	@POST
	@Path("getCaculationResult")
	public List<CaculationTime> getCaculationResult(ParamCaculationOvertime param) {
		return this.overtimeFinder.getCaculationValue(param.getOvertimeHours(),param.getBonusTimes(),param.getPrePostAtr(), param.getAppDate(),param.getSiftCD(),param.getWorkTypeCode(),
				param.getStartTime(),
				param.getEndTime(),
				param.getStartTimeRests(),
				param.getEndTimeRests());
	}
	
	@POST
	@Path("create")
	public ProcessResult createOvertime(CreateOvertimeCommand command){
		return createHandler.handle(command); 
	}
	@POST
	@Path("checkBeforeRegister")
	public OvertimeCheckResultDto checkBeforeRegister(CreateOvertimeCommand command){
		return checkBefore.CheckBeforeRegister(command);
	}
	
	@POST
	@Path("checkBeforeUpdate")
	public OvertimeCheckResultDto checkBeforeUpdate(CreateOvertimeCommand command){
		return checkBefore.checkBeforeUpdate(command);
	}
	
	@POST
	@Path("findByAppID")
	public OverTimeDto findByChangeAppDate(String appID) {
		return this.overtimeFinder.findDetailByAppID(appID);
	}
	
	@POST
	@Path("update")
	public ProcessResult update(UpdateOvertimeCommand command) {
		return this.updateOvertimeCommandHandler.handle(command);
	}
	
	@POST
	@Path("getRecordWork")
	public RecordWorkDto getRecordWork(RecordWorkParam param) {
		return this.overtimeFinder.getRecordWork(param.employeeID, param.appDate, param.siftCD,param.prePostAtr,param.getOvertimeHours(),param.getWorkTypeCode(),
				param.getStartTimeRests(),
				param.getEndTimeRests());
	}
}
