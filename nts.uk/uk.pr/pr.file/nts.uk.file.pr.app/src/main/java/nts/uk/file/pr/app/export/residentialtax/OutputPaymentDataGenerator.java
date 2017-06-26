package nts.uk.file.pr.app.export.residentialtax;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentTaxReport;

public interface OutputPaymentDataGenerator {
	/**
     * Generate.
     *
     * @param fileContext the file context
     * @param reportData the social insurance report data
     */
    void generate(FileGeneratorContext fileContext, ResidentTaxReport reportData);
}
