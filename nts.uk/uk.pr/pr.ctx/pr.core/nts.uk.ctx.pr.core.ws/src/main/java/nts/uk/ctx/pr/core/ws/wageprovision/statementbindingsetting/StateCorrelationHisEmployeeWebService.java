package nts.uk.ctx.pr.core.ws.wageprovision.statementbindingsetting;


import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.wageprovision.statementbindingsetting.AddOrUpdateStateCorrelationHisEmployeeCommandHandler;
import nts.uk.ctx.pr.core.app.command.wageprovision.statementbindingsetting.StateCorrelationHisEmployeeContainerCommand;
import nts.uk.ctx.pr.core.app.find.wageprovision.statementbindingsetting.*;
import nts.uk.shr.com.context.AppContexts;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("core/wageprovision/statementbindingsetting")
@Produces("application/json")
public class StateCorrelationHisEmployeeWebService extends WebService {

    @Inject
    private StateCorrelationHisEmployeeFinder stateCorrelationHisEmployeeFinder;

    @Inject
    private AddOrUpdateStateCorrelationHisEmployeeCommandHandler addOrUpdateStateCorrelationHisEmployeeCommandHandler;

    @POST
    @Path("getStateCorrelationHisEmployeeById")
    public List<StateCorrelationHisEmployeeDto> getStateCorrelationHisEmployeeById(){
        String cid = AppContexts.user().companyId();
        return stateCorrelationHisEmployeeFinder.getStateCorrelationHisEmployeeById(cid);
    }

    @POST
    @Path("registerStateCorrelationHisEmployee")
    public void  registerStateCorrelationHisEmployee(StateCorrelationHisEmployeeContainerCommand command){
        addOrUpdateStateCorrelationHisEmployeeCommandHandler.handle(command);
    }

    @POST
    @Path("getStateLinkSettingMasterByHisId/{hisId}/{startYearMonth}")
    public List<StateCorrelationHisEmployeeSettingDto> getStateLinkSettingMasterByHisId(@PathParam("hisId") String hisId, @PathParam("startYearMonth") int startYearMonth){
        String cid = AppContexts.user().companyId();
        return stateCorrelationHisEmployeeFinder.getStateLinkSettingMasterByHisId(cid,hisId,startYearMonth);
    }

}
