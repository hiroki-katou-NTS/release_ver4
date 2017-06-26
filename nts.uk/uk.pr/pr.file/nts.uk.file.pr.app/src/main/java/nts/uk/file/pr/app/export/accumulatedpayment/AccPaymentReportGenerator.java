/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.accumulatedpayment;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.accumulatedpayment.data.AccPaymentDataSource;
import nts.uk.file.pr.app.export.accumulatedpayment.query.AccPaymentReportQuery;


/**
 * The Interface AccPaymentReportGenerator.
 */
public interface AccPaymentReportGenerator {
	
	/**
	 * Generate.
	 *
	 * @param generatorContext the generator context
	 * @param dataSource the data source
	 */
	void generate(FileGeneratorContext generatorContext, 
			AccPaymentDataSource dataSource, AccPaymentReportQuery query);	
}
