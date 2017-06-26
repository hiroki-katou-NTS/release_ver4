/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.insurance.salary;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.insurance.data.SocialInsuMLayoutReportData;

/**
 * The Interface SocialInsuMergeLayoutGenerator.
 */

public interface SocialInsuMergeLayoutGenerator {

    /**
     * Generate.
     *
     * @param fileContext the file context
     * @param reportData the report data
     */
    void generate(FileGeneratorContext fileContext, SocialInsuMLayoutReportData reportData);
}
