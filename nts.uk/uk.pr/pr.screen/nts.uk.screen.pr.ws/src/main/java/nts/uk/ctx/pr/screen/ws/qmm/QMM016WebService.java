/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.screen.ws.qmm;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommand;
import nts.uk.file.pr.app.export.wagetable.inputfile.InputFileReportService;

/**
 * The Class QMM016WebService.
 */
@Path("/screen/pr/qmm016")
public class QMM016WebService extends WebService {

	/** The input file report service. */
	@Inject
	private InputFileReportService inputFileReportService;

	/**
	 * Export input file.
	 *
	 * @return the export service result
	 */
	@POST
	@Path("inputfile")
	public ExportServiceResult exportInputFile(WtUpdateCommand data) {
		return this.inputFileReportService.start(data);
	}
}
