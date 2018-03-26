package nts.uk.ctx.at.request.ws.application.appabsence;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.Value;
import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.request.app.command.application.appabsence.CreatAppAbsenceCommand;
import nts.uk.ctx.at.request.app.command.application.appabsence.CreatAppAbsenceCommandHandler;
import nts.uk.ctx.at.request.app.find.application.appabsence.AppAbsenceFinder;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.AppAbsenceDto;

@Path("at/request/application/appforleave")
@Produces("application/json")
public class AppForLeaveWebService extends WebService{
	@Inject
	private AppAbsenceFinder appForLeaveFinder;
	@Inject
	private CreatAppAbsenceCommandHandler creatAppAbsenceCommandHandler;
	
	@POST
	@Path("getAppForLeaveStart")
	public AppAbsenceDto getAppForLeaveStart(Param param) {
		return this.appForLeaveFinder.getAppForLeave(param.getAppDate(),param.getEmployeeID());
	}
	@POST
	@Path("getAllAppForLeave")
	public AppAbsenceDto getAppForLeaveAll(ParamGetALL param) {
		return this.appForLeaveFinder.getAllDisplay(param.getStartAppDate(),param.isDisplayHalfDayValue(),param.getEmployeeID(),param.getHolidayType(),param.getAlldayHalfDay());
	}
	@POST
	@Path("findChangeAppdate")
	public AppAbsenceDto findChangeAppdate(ParamGetALL param) {
		return this.appForLeaveFinder.getChangeAppDate(param.getStartAppDate(),param.isDisplayHalfDayValue(),param.getEmployeeID(),param.getWorkTypeCode(),param.getHolidayType(),param.getAlldayHalfDay(),param.getPrePostAtr());
	}
	@POST
	@Path("getChangeAllDayHalfDay")
	public AppAbsenceDto getChangeAllDayHalfDay(ParamGetALL param) {
		return this.appForLeaveFinder.getChangeByAllDayOrHalfDay(param.getStartAppDate(),param.isDisplayHalfDayValue(),param.getEmployeeID(),param.getHolidayType(),param.getAlldayHalfDay());
	}
	@POST
	@Path("findChangeDisplayHalfDay")
	public AppAbsenceDto getChangeDisplayHalfDay(ParamGetALL param) {
		return this.appForLeaveFinder.getChangeDisplayHalfDay(param.getStartAppDate(),param.isDisplayHalfDayValue(),param.getEmployeeID(),param.getWorkTypeCode(),param.getHolidayType(),param.getAlldayHalfDay());
	}
	@POST
	@Path("findChangeWorkType")
	public AppAbsenceDto getChangeWorkType(ParamGetALL param) {
		return this.appForLeaveFinder.getChangeWorkType(param.getStartAppDate(),param.getEmployeeID(),param.getWorkTypeCode(),param.getHolidayType(),param.getWorkTimeCode());
	}
	@POST
	@Path("getListWorkTime")
	public List<String> getListWorkTime(ParamGetALL param) {
		return this.appForLeaveFinder.getListWorkTimeCodes(param.getStartAppDate(),param.getEmployeeID());
	}
	@POST
	@Path("getWorkingHours")
	public AppAbsenceDto getWorkingHours(ParamGetALL param) {
		return this.appForLeaveFinder.getWorkingHours(param.getWorkTimeCode(),param.getWorkTypeCode(),param.getHolidayType());
	}
	@POST
	@Path("insert")
	public JavaTypeResult<String> insert(CreatAppAbsenceCommand param) {
		JavaTypeResult<String> result  =  new JavaTypeResult<String>(creatAppAbsenceCommandHandler.handle(param));
		return result;
	}
	@POST
	@Path("getByAppID")
	public AppAbsenceDto getByAppID(String appID) {
		return this.appForLeaveFinder.getByAppID(appID);
	}
	
}

@Value
class Param{
	private String appDate;
	private String employeeID;
}

@Value
class ParamGetALL{
	private String startAppDate;
	private String endAppDate;
	private String employeeID;
	private boolean displayHalfDayValue;
	private Integer holidayType;
	private int alldayHalfDay;
	private String workTypeCode;
	private int prePostAtr;
	private String workTimeCode;
}
