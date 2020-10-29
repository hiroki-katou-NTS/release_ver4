package nts.uk.ctx.at.function.ws.outputworkstatustable;


import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.function.app.command.outputworkstatustable.CreateConfigdetailCommand;
import nts.uk.ctx.at.function.app.command.outputworkstatustable.CreateConfigdetailCommandHandler;
import nts.uk.ctx.at.function.app.command.outputworkstatustable.UpdateSettingDetailCommand;
import nts.uk.ctx.at.function.app.command.outputworkstatustable.UpdateSettingDetailCommandHandler;
import nts.uk.ctx.at.function.app.query.outputworkstatustable.GetDetailOutputSettingWorkStatusQuery;
import nts.uk.ctx.at.function.app.query.outputworkstatustable.GetOutputItemSettingQuery;
import nts.uk.ctx.at.function.app.query.outputworkstatustable.WorkStatusOutputDto;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.WorkStatusOutputSettings;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.SettingClassificationCommon;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("at/function/kwr/003/b")
@Produces("application/json")
public class GetOutputItemSettingWebService extends WebService {

    @Inject
    GetOutputItemSettingQuery settingQuery;

    @Inject
    GetDetailOutputSettingWorkStatusQuery detailOutputSettingWorkStatusQuery;

    @Inject
    private CreateConfigdetailCommandHandler createConfigdetailCommandHandler;

    @Inject
    private UpdateSettingDetailCommandHandler updateSettingDetailCommandHandler;

    @POST
    @Path("listWorkStatus")
    public List<WorkStatusOutputDto> getListWorkStatus (int setting){
        return settingQuery.getListWorkStatus(EnumAdaptor.valueOf(setting, SettingClassificationCommon.class));
    }
    @POST
    @Path("detailWorkStatus")
    public WorkStatusOutputSettings getDetailWorkStatus (String settingId){
        return detailOutputSettingWorkStatusQuery.getDetail(settingId);
    }
    @POST
    @Path("create")
    public void create (CreateConfigdetailCommand command){
        this.createConfigdetailCommandHandler.handle(command);
    }
    @POST
    @Path("update")
    public void create (UpdateSettingDetailCommand command){
        this.updateSettingDetailCommandHandler.handle(command);
    }
}