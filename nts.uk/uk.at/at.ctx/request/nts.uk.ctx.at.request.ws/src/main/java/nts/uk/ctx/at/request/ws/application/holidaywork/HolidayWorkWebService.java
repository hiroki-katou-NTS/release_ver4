package nts.uk.ctx.at.request.ws.application.holidaywork;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.Data;
import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.request.app.command.application.holidaywork.CheckBeforeRegisterHolidayWork;
import nts.uk.ctx.at.request.app.command.application.holidaywork.CreateHolidayWorkCommand;
import nts.uk.ctx.at.request.app.command.application.holidaywork.CreateHolidayWorkCommandHandler;
import nts.uk.ctx.at.request.app.command.application.holidaywork.UpdateHolidayWorkCommand;
import nts.uk.ctx.at.request.app.command.application.holidaywork.UpdateHolidayWorkCommandHandler;
import nts.uk.ctx.at.request.app.find.application.holidaywork.AppHolidayWorkFinder;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.AppHolidayWorkDto;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.ParamCalculationHolidayWork;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeCheckResultDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.ParamChangeAppDate;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.RecordWorkDto;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime;

@Path("at/request/application/holidaywork")
@Produces("application/json")
public class HolidayWorkWebService extends WebService{
	@Inject
	private AppHolidayWorkFinder appHolidayWorkFinder;
	@Inject
	private CheckBeforeRegisterHolidayWork checkBeforeRegisterHolidayWork;
	@Inject
	private CreateHolidayWorkCommandHandler createHolidayWorkCommandHandler;
	@Inject
	private UpdateHolidayWorkCommandHandler updateHolidayWorkCommandHandle;
	
	@POST
	@Path("getHolidayWorkByUI")
	public AppHolidayWorkDto getOvertimeByUIType(Param param) {
		return this.appHolidayWorkFinder.getAppHolidayWork(param.getAppDate(), param.getUiType(),param.getLstEmployee(),param.getPayoutType(),param.getEmployeeID());
	}
	@POST
	@Path("findChangeAppDate")
	public AppHolidayWorkDto findChangeAppDate(ParamChangeAppDate param) {
		return this.appHolidayWorkFinder.findChangeAppDate(param.getAppDate(), param.getPrePostAtr(),param.getSiftCD(),param.getOvertimeHours());
	}
	@POST
	@Path("getcalculationresult")
	public List<CaculationTime> getCalculationTime(ParamCalculationHolidayWork param){
		return this.appHolidayWorkFinder.getCaculationValue(param.getBreakTimes(),
															param.getPrePostAtr(),
															param.getAppDate(),
															param.getSiftCD(),
															param.getWorkTypeCode(),
															param.getEmployeeID(),
															param.getInputDate() == null ? null :GeneralDateTime.fromString(param.getInputDate(), "yyyy/MM/dd HH:mm"),
															param.getStartTime(),
															param.getEndTime(),
															param.getStartTimeRest(),
															param.getEndTimeRest());
	}
	@POST
	@Path("create")
	public ProcessResult createHolidayWork(CreateHolidayWorkCommand command){
		return createHolidayWorkCommandHandler.handle(command);
	}
	@POST
	@Path("checkBeforeRegister")
	public OvertimeCheckResultDto checkBeforeRegister(CreateHolidayWorkCommand command){
		return checkBeforeRegisterHolidayWork.CheckBeforeRegister(command);
	}
	@POST
	@Path("findByAppID")
	public AppHolidayWorkDto findByChangeAppID(String appID) {
		return this.appHolidayWorkFinder.getAppHolidayWorkByAppID(appID);
	}
	@POST
	@Path("checkBeforeUpdate")
	public OvertimeCheckResultDto checkBeforeUpdate(CreateHolidayWorkCommand command){
		return checkBeforeRegisterHolidayWork.checkBeforeUpdate(command);
	}
	@POST
	@Path("update")
	public ProcessResult updateHolidayWork(UpdateHolidayWorkCommand command){
		return updateHolidayWorkCommandHandle.handle(command);
	}
	@POST
	@Path("getRecordWork")
	public RecordWorkDto getRecordWork(RecordWorkParam param) {
		return this.appHolidayWorkFinder.getRecordWork(param.employeeID, param.appDate, param.siftCD,param.prePostAtr,param.getBreakTimeHours());
	}
	
}
@Data
class Param{
	private String appDate;
	private int uiType;
	private List<String> lstEmployee;
	private Integer payoutType;
	private String employeeID;
}
@Data
class RecordWorkParam {
	public String employeeID; 
	public String appDate;
	public String siftCD;
	public int prePostAtr;
	public List<CaculationTime> breakTimeHours;
}