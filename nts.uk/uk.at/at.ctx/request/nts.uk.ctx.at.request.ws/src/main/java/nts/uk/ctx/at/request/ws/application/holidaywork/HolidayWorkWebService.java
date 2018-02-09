package nts.uk.ctx.at.request.ws.application.holidaywork;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.Value;
import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.request.app.command.application.holidaywork.CheckBeforeRegisterHolidayWork;
import nts.uk.ctx.at.request.app.command.application.holidaywork.CreateHolidayWorkCommand;
import nts.uk.ctx.at.request.app.command.application.holidaywork.CreateHolidayWorkCommandHandler;
import nts.uk.ctx.at.request.app.find.application.holidaywork.AppHolidayWorkFinder;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.AppHolidayWorkDto;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.ParamCalculationHolidayWork;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeCheckResultDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.ParamChangeAppDate;
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
	
	@POST
	@Path("getHolidayWorkByUI")
	public AppHolidayWorkDto getOvertimeByUIType(Param param) {
		return this.appHolidayWorkFinder.getAppHolidayWork(param.getAppDate(), param.getUiType());
	}
	@POST
	@Path("findChangeAppDate")
	public AppHolidayWorkDto findChangeAppDate(ParamChangeAppDate param) {
		return this.appHolidayWorkFinder.findChangeAppDate(param.getAppDate(), param.getPrePostAtr(),param.getSiftCD(),param.getOvertimeHours());
	}
	@POST
	@Path("getcalculationresult")
	public List<CaculationTime> getCalculationTime(ParamCalculationHolidayWork param){
		return this.appHolidayWorkFinder.getCaculationValue(param.getBreakTimes(), param.getPrePostAtr(), param.getAppDate(), param.getSiftCD(), param.getWorkTypeCode(), param.getEmployeeID(), param.getInputDate() == null ? null :GeneralDateTime.fromString(param.getInputDate(), "yyyy/MM/dd HH:mm"));
	}
	@POST
	@Path("create")
	public JavaTypeResult<String> createOvertime(CreateHolidayWorkCommand command){
		JavaTypeResult<String>  test = new JavaTypeResult<String>(createHolidayWorkCommandHandler.handle(command)); 
		return test;
	}
	@POST
	@Path("checkBeforeRegister")
	public OvertimeCheckResultDto checkBeforeRegister(CreateHolidayWorkCommand command){
		return checkBeforeRegisterHolidayWork.CheckBeforeRegister(command);
	}
	
}
@Value
class Param{
	private String appDate;
	private int uiType;
}