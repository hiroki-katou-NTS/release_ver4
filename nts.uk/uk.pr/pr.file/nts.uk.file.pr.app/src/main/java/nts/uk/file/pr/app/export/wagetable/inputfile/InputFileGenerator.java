/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wagetable.inputfile;

import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommand;

/**
 * The Interface InputFileGenerator.
 */
public interface InputFileGenerator {
	
	/**
	 * Generate.
	 *
	 * @param fileContext the file context
	 * @param inputData the input data
	 */
	void generate(ExportServiceContext<WtUpdateCommand> context);
}
