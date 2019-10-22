package nts.uk.ctx.pr.report.ws.printconfig.socinsurnoticreset;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.uk.ctx.pr.report.app.find.printconfig.socialinsurnoticreset.EmpAddChangeInfoExportPDFService;
import nts.uk.ctx.pr.report.app.find.printconfig.socialinsurnoticreset.EmpAddChangeInfoExportQuery;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("ctx/pr/report/printconfig/changeAdd")
@Produces("application/json")
public class EmpAddChangeInfoWebService {

    @Inject
    private EmpAddChangeInfoExportPDFService service;

    @POST
    @Path("/exportData")
    public ExportServiceResult exportData(EmpAddChangeInfoExportQuery query){
        return service.start(query);
    }
}