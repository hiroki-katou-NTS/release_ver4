package nts.uk.file.pr.app.export.residentialtax;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.residentialtax.data.InhabitantTaxChecklistBReport;

public interface InhabitantTaxChecklistBGenerator {

	/**
     * Generate.
     *
     * @param fileContext the file context
     * @param reportData the social insurance report data
     */
    void generate(FileGeneratorContext fileContext, InhabitantTaxChecklistBReport reportData);
}
