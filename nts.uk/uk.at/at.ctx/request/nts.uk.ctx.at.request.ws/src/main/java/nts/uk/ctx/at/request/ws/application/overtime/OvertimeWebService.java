package nts.uk.ctx.at.request.ws.application.overtime;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.Value;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.request.app.command.application.overtime.CheckBeforeRegisterOvertime;
import nts.uk.ctx.at.request.app.command.application.overtime.CheckConvertPrePost;
import nts.uk.ctx.at.request.app.command.application.overtime.CreateOvertimeCommand;
import nts.uk.ctx.at.request.app.command.application.overtime.CreateOvertimeCommandHandler;
import nts.uk.ctx.at.request.app.command.application.overtime.UpdateOvertimeCommand;
import nts.uk.ctx.at.request.app.command.application.overtime.UpdateOvertimeCommandHandler;
import nts.uk.ctx.at.request.app.find.application.overtime.AppOvertimeFinder;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OverTimeDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeCheckResultDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeInputDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.ParamCaculationOvertime;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.ParamChangeAppDate;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;

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
	public OverTimeDto getOvertimeByUIType(Param param) {
		return this.overtimeFinder.getOvertimeByUIType(param.getUrl(), param.getAppDate(), param.getUiType());
	}
	
	@POST
	@Path("findByChangeAppDate")
	public OverTimeDto findByChangeAppDate(ParamChangeAppDate param) {
		return this.overtimeFinder.findByChangeAppDate(param.getAppDate(), param.getPrePostAtr());
	}
	@POST
	@Path("checkConvertPrePost")
	public OverTimeDto convertPrePost(ParamChangeAppDate param) {
		return this.checkConvertPrePost.convertPrePost(param.getPrePostAtr(),param.getAppDate());
	}
	@POST
	@Path("getCaculationResult")
	public List<OvertimeInputDto> getCaculationResult(ParamCaculationOvertime param) {
		return this.overtimeFinder.getCaculationValue(param.getOvertimeInputDtos(), param.getPrePostAtr(), param.getAppDate());
	}
	
	
	@POST
	@Path("create")
	public void createOvertime(CreateOvertimeCommand command){
		createHandler.handle(command);
	}
	@POST
	@Path("checkBeforeRegister")
	public OvertimeCheckResultDto checkBeforeRegister(CreateOvertimeCommand command){
		return checkBefore.CheckBeforeRegister(command);
	}
	
	@POST
	@Path("findByAppID")
	public OverTimeDto findByChangeAppDate(String appID) {
		return this.overtimeFinder.findDetailByAppID(appID);
	}
	
	@POST
	@Path("update")
	public void update(UpdateOvertimeCommand command) {
		this.updateOvertimeCommandHandler.handle(command);
	}
}

@Value
class Param{
	private String url;
	private String appDate;
	private int uiType;
}