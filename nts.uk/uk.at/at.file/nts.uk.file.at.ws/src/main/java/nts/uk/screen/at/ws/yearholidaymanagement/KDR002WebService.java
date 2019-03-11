package nts.uk.screen.at.ws.yearholidaymanagement;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.uk.file.at.app.export.yearholidaymanagement.OutputYearHolidayManagementExportService;
import nts.uk.file.at.app.export.yearholidaymanagement.OutputYearHolidayManagementQuery;

@Path("screen/at/yearholidaymanagement")
@Produces("application/json")
public class KDR002WebService {

	@Inject
	private OutputYearHolidayManagementExportService service;

	@POST
	@Path("export")
	public ExportServiceResult exportData(OutputYearHolidayManagementQuery query) {
		return service.start(query);
	}

}
