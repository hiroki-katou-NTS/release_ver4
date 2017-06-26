/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.screen.ws.qpp;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.uk.file.pr.app.export.insurance.salary.SocialInsuQuery;
import nts.uk.file.pr.app.export.insurance.salary.SocialInsuReportService;

/**
 * The Class QPP018WebService.
 */
@Path("/screen/pr/QPP018")
@Produces("application/json")
public class QPP018WebService extends WebService {

    /** The report service. */
    @Inject
    private SocialInsuReportService reportService;
    
    /**
     * Export data to pdf.
     *
     * @param query the query
     * @return the export service result
     */
    @POST
    @Path("saveAsPdf")
    public ExportServiceResult exportDataToPdf(SocialInsuQuery query) {
         return reportService.start(query);
    }
}
