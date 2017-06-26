/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.denominationtable;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.denominationtable.data.DenominationTableData;
import nts.uk.file.pr.app.export.denominationtable.query.DenoTableReportQuery;

/**
 * The Interface DenoTableReportGenerator.
 */
public interface DenoTableReportGenerator {

	/**
	 * Generate.
	 *
	 * @param generatorContext the generator context
	 * @param dataSource the data source
	 * @param query the query
	 */
	void generate(FileGeneratorContext generatorContext, 
			DenominationTableData dataSource, DenoTableReportQuery query);
}
