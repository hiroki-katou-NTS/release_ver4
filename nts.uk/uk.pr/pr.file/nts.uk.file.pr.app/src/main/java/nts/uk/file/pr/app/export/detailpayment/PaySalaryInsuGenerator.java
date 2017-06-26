/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
/**
 * 
 */
package nts.uk.file.pr.app.export.detailpayment;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.detailpayment.data.PaymentSalaryReportData;

/**
 * The Interface PaySalaryInsuGenerator.
 */
public interface PaySalaryInsuGenerator {

    /**
     * Generate.
     *
     * @param fileContext the file context
     * @param reportData the report data
     */
    void generate(FileGeneratorContext fileContext, PaymentSalaryReportData reportData);
}
