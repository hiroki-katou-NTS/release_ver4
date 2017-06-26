/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger;

import java.util.List;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.wageledger.data.WLOldLayoutReportData;

/**
 * The Interface WageLedgerReportGenerator.
 */
public interface WLOldLayoutReportGenerator {
	
	/**
	 * Generate with old layout.
	 *
	 * @param fileContext the file context
	 * @param reportData the report data
	 */
	void generate(FileGeneratorContext fileContext, 
			List<WLOldLayoutReportData> reportData, WageLedgerReportQuery query);
}
