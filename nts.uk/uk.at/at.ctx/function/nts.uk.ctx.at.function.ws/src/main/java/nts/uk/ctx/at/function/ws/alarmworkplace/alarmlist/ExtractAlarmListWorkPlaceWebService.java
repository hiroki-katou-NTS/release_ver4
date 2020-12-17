package nts.uk.ctx.at.function.ws.alarmworkplace.alarmlist;

import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.arc.task.AsyncTaskInfo;
import nts.uk.ctx.at.function.app.command.alarmworkplace.alarmlist.ExtractAlarmListWorkPlaceCommandHandler;
import nts.uk.ctx.at.function.app.command.alarmworkplace.alarmlist.ExtractAlarmListWorkPlaceCommand;
import nts.uk.ctx.at.function.app.command.alarmworkplace.alarmlist.SendEmailAlarmListWorkPlaceCommand;
import nts.uk.ctx.at.function.app.command.alarmworkplace.alarmlist.SendEmailAlarmListWorkPlaceCommandHandler;
import nts.uk.ctx.at.function.app.command.alarmworkplace.extractprocessstatus.CreateAlarmListExtractProcessStatusWorkplaceCommand;
import nts.uk.ctx.at.function.app.command.alarmworkplace.extractprocessstatus.CreateAlarmListExtractProcessStatusWorkplaceCommandHandler;
import nts.uk.ctx.at.function.app.command.alarmworkplace.extractprocessstatus.UpdateAlarmListExtractProcessStatusWorkplaceCommand;
import nts.uk.ctx.at.function.app.command.alarmworkplace.extractprocessstatus.UpdateAlarmListExtractProcessStatusWorkplaceCommandHandler;
import nts.uk.ctx.at.function.app.export.alarmworkplace.alarmlist.AlarmWorkPlaceExportData;
import nts.uk.ctx.at.function.app.export.alarmworkplace.alarmlist.AlarmWorkPlaceExportService;
import nts.uk.ctx.at.function.app.find.alarmworkplace.alarmlist.CheckConditionDto;
import nts.uk.ctx.at.function.app.find.alarmworkplace.alarmlist.ExtractAlarmListWorkPlaceFinder;
import nts.uk.ctx.at.function.app.find.alarmworkplace.alarmlist.InitActiveAlarmListDto;
import nts.uk.ctx.at.function.dom.alarmworkplace.extractresult.dto.AlarmListExtractResultWorkplaceDto;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * KAL011-アラームリスト(職場別)
 */
@Path("at/function/alarm-workplace/alarm-list")
@Produces("application/json")
public class ExtractAlarmListWorkPlaceWebService extends WebService {
    @Inject
    private ExtractAlarmListWorkPlaceFinder extractAlarmListWorkPlaceFinder;
    @Inject
    private CreateAlarmListExtractProcessStatusWorkplaceCommandHandler createAlarmListExtractProcessStatusWorkplaceCommandHandler;
    @Inject
    private ExtractAlarmListWorkPlaceCommandHandler extractAlarmListWorkPlaceCommandHandler;
    @Inject
    private SendEmailAlarmListWorkPlaceCommandHandler sendEmailCommandHandler;
    @Inject
    private UpdateAlarmListExtractProcessStatusWorkplaceCommandHandler updateAlarmListExtractProcessStatusWorkplaceCommandHandler;

    @Inject
    private AlarmWorkPlaceExportService alarmWorkPlaceExportService;

    @POST
    @Path("init")
    public InitActiveAlarmListDto initActiveAlarmList() {
        return extractAlarmListWorkPlaceFinder.initActiveAlarmList();
    }

    @POST
    @Path("get-check-conditions/{code}/{ym}")
    public List<CheckConditionDto> getCheckConditions(@PathParam("code") String code, @PathParam("ym") Integer ym) {
        return extractAlarmListWorkPlaceFinder.getCheckConditions(code, ym);
    }

    @POST
    @Path("extract/start")
    public JavaTypeResult<String> extractStarting() {
        return new JavaTypeResult<>(createAlarmListExtractProcessStatusWorkplaceCommandHandler.handle(new CreateAlarmListExtractProcessStatusWorkplaceCommand()));
    }

    @POST
    @Path("extract/execute")
    public AsyncTaskInfo extractAlarm(ExtractAlarmListWorkPlaceCommand command) {
        return extractAlarmListWorkPlaceCommandHandler.handle(command);
    }

    @POST
    @Path("extract/update-status")
    public void extractUpdateStatus(UpdateAlarmListExtractProcessStatusWorkplaceCommand command) {
        updateAlarmListExtractProcessStatusWorkplaceCommandHandler.handle(command);
    }

    @POST
    @Path("extract/get-result/{processId}")
    public List<AlarmListExtractResultWorkplaceDto> extractGetResult(String processId) {
        return extractAlarmListWorkPlaceFinder.getExtractResult(processId);
    }

    @POST
    @Path("send-email")
    public JavaTypeResult<String> sendEmailStarting(SendEmailAlarmListWorkPlaceCommand command) {
        return new JavaTypeResult<String>(sendEmailCommandHandler.handle(command));
    }

    @POST
    @Path("export-alarm-data")
    public ExportServiceResult generate(AlarmWorkPlaceExportData data) {
        return this.alarmWorkPlaceExportService.start(data);
    }
}
