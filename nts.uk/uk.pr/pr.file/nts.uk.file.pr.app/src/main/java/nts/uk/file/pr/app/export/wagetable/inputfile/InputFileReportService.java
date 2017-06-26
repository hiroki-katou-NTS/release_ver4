/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wagetable.inputfile;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommand;

/**
 * The Class InputFileReportService.
 */
@Stateless
public class InputFileReportService extends ExportService<WtUpdateCommand> {

	/** The generator. */
	@Inject
	private InputFileGenerator generator;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.file.export.ExportService#handle(nts.arc.layer.app.file.export.ExportServiceContext)
	 */
	@Override
	protected void handle(ExportServiceContext<WtUpdateCommand> context) {
		this.generator.generate(context);
	}
}
