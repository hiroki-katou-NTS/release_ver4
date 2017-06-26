/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.wagetable;

import nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommand;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;

/**
 * The Interface Generator.
 */
public interface Generator {
	
	/**
	 * Generate.
	 *
	 * @param context the context
	 * @param data the data
	 */
	void generate(AsposeCellsReportContext context, WtUpdateCommand data);
}
