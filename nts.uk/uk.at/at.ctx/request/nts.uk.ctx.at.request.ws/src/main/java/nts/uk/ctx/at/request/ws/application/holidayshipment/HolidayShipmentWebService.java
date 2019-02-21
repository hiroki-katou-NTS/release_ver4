package nts.uk.ctx.at.request.ws.application.holidayshipment;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;

import lombok.Value;
import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.ApproveHolidayShipmentCommandHandler;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.CancelHolidayShipmentCommandHandler;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.ChangeAbsDateToHolidayCommandHandler;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.DeleteHolidayShipmentCommandHandler;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.DenyHolidayShipmentCommandHandler;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.HolidayShipmentCommand;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.ReleaseHolidayShipmentCommandHandler;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.SaveChangeAbsDateCommandHandler;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.SaveHolidayShipmentCommand;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.SaveHolidayShipmentCommandHandler;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.UpdateHolidayShipmentCommandHandler;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.HolidayShipmentScreenAFinder;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.HolidayShipmentScreenBFinder;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.HolidayShipmentScreenCFinder;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.HolidayShipmentDto;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.WorkTimeInfoDto;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ApproveProcessResult;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;

@Path("at/request/application/holidayshipment")
@Produces("application/json")
public class HolidayShipmentWebService extends WebService {

	@Inject
	private HolidayShipmentScreenAFinder screenAFinder;
	@Inject
	private HolidayShipmentScreenCFinder screenCFinder;
	@Inject
	private HolidayShipmentScreenBFinder screenBFinder;
	@Inject
	private SaveHolidayShipmentCommandHandler saveHandler;
	@Inject
	private UpdateHolidayShipmentCommandHandler updateHandler;
	@Inject
	private DeleteHolidayShipmentCommandHandler deleteHanler;
	@Inject
	private CancelHolidayShipmentCommandHandler cancelHanler;
	@Inject
	private ChangeAbsDateToHolidayCommandHandler changeDateAbsToHolidayHanler;
	@Inject
	private ApproveHolidayShipmentCommandHandler approveHandler;
	@Inject
	private DenyHolidayShipmentCommandHandler denyHandler;
	@Inject
	private ReleaseHolidayShipmentCommandHandler releaseHandler;
	@Inject
	private SaveChangeAbsDateCommandHandler changeAbsDateHander;

	@POST
	@Path("start")
	public HolidayShipmentDto startPage(StartScreenAParam param) {
		return this.screenAFinder.startPageA(param.getSIDs(), param.getAppDate(), param.getUiType());
	}

	@POST
	@Path("change_work_type")
	public WorkTimeInfoDto changeWorkType(ChangeWorkTypeParam param) {
		return this.screenAFinder.changeWorkType(param.getWkTypeCD(), param.getWkTimeCD());
	}

	@POST
	@Path("update")
	public void update(SaveHolidayShipmentCommand command) {
		updateHandler.handle(command);
	}

	@POST
	@Path("change_day")
	public HolidayShipmentDto changeDay(ChangeDateParam param) {
		return this.screenAFinder.changeAppDate(param.getTakingOutDate(), param.getHolidayDate(), param.getComType(),
				param.getUiType());
	}

	@POST
	@Path("get_selected_working_hours")
	public WorkTimeInfoDto getSelectedWorkingHours(ChangeWorkTypeParam param) {
		return this.screenAFinder.getSelectedWorkingHours(param.getWkTypeCD(), param.getWkTimeCD());
	}

	@POST
	@Path("save")
	public JavaTypeResult<ProcessResult> save(SaveHolidayShipmentCommand command) {
		return new JavaTypeResult<ProcessResult>(saveHandler.handle(command));
	}

	@POST
	@Path("find_by_id")
	public HolidayShipmentDto findByID(StartScreenBParam param) {
		return this.screenBFinder.findByID(param.getAppID());
	}

	@POST
	@Path("remove")
	public JavaTypeResult<ProcessResult> remove(HolidayShipmentCommand command) {
		return new JavaTypeResult<ProcessResult>(this.deleteHanler.handle(command));
	}

	@POST
	@Path("cancel")
	public void cancel(HolidayShipmentCommand command) {
		this.cancelHanler.handle(command);
	}

	@POST
	@Path("approve")
	public JavaTypeResult<ApproveProcessResult> approve(HolidayShipmentCommand command) {
		return new JavaTypeResult<ApproveProcessResult>(this.approveHandler.handle(command));
	}

	@POST
	@Path("deny")
	public JavaTypeResult<ProcessResult> deny(HolidayShipmentCommand command) {
		return new JavaTypeResult<ProcessResult>(this.denyHandler.handle(command));
	}

	@POST
	@Path("release")
	public JavaTypeResult<ProcessResult> release(HolidayShipmentCommand command) {
		return new JavaTypeResult<ProcessResult>(this.releaseHandler.handle(command));
	}

	@POST
	@Path("start_c")
	public HolidayShipmentDto startPageC(StartScreenCParam param) {
		return this.screenCFinder.startPage(param.getSID(), param.getAppDate(), param.getUiType());
	}

	@POST
	@Path("change_abs_date")
	public JavaTypeResult<ProcessResult> changeDateC(SaveHolidayShipmentCommand command) {
		return new JavaTypeResult<ProcessResult>(this.changeAbsDateHander.handle(command));
	}

	@POST
	@Path("change_abs_date_to_holiday")
	public JavaTypeResult<Integer> changeAbsDate(SaveHolidayShipmentCommand command) {
		return new JavaTypeResult<Integer>(this.changeDateAbsToHolidayHanler.handle(command));
	}

}

@Value
class StartScreenAParam {
	private List<String> sIDs;
	private GeneralDate appDate;
	private int uiType;
}

@Value
class StartScreenBParam {
	private String appID;
}

@Value
class StartScreenCParam {
	private String sID;
	private GeneralDate appDate;
	private int uiType;
}

@Value
class ChangeWorkTypeParam {
	private String wkTypeCD;
	private String wkTimeCD;
}

@Value
class ChangeDateParam {

	private String holidayDate;

	private String takingOutDate;

	/**
	 * 申請組み合わせ
	 */
	private int comType;
	private int uiType;

	public GeneralDate getHolidayDate() {
		return !StringUtils.isEmpty(holidayDate) ? GeneralDate.fromString(holidayDate, "yyyy/MM/dd") : null;
	}

	public GeneralDate getTakingOutDate() {
		return !StringUtils.isEmpty(takingOutDate) ? GeneralDate.fromString(takingOutDate, "yyyy/MM/dd") : null;
	}

}
