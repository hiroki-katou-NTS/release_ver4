package nts.uk.file.pr.app.export.residentialtax;

import java.util.List;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentTaxReportData;

public interface ResidentialTaxGenerator {
	/**
     * Generate.
     *
     * @param fileContext the file context
     * @param reportData the social insurance report data
     */
    void generate(FileGeneratorContext fileContext, List<ResidentTaxReportData> reportData);
}
