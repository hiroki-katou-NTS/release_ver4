/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.insurance.salary;

import java.util.List;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.insurance.data.SocialInsuReportData;

/**
 * The Interface SocialInsuranceGenerator.
 */
public interface SocialInsuGenerator {

    /**
     * Generate.
     *
     * @param fileContext the file context
     * @param listReport the list report
     */
    void generate(FileGeneratorContext fileContext, List<SocialInsuReportData> listReport);
}
