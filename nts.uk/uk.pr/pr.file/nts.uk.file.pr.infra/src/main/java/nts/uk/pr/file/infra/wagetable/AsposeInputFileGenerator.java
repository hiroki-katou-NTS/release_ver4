/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.wagetable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommand;
import nts.uk.file.pr.app.export.wagetable.inputfile.InputFileGenerator;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * The Class AsposeInputFileGenerator.
 */
@Stateless
public class AsposeInputFileGenerator extends AsposeCellsReportGenerator implements InputFileGenerator {

	/** The Constant TEMPLATE_PATH. */
	private static final String TEMPLATE_PATH = "report/QMM016_InputFile.xlsx";

	/** The Constant OUTPUT_NAME. */
	private static final String OUTPUT_NAME = "賃金テープル.xlsx";

	/** The factory. */
	@Inject
	private GeneratorFactory factory;

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.file.pr.app.export.wagetable.inputfile.InputFileGenerator#generate
	 * (nts.arc.layer.app.file.export.ExportServiceContext)
	 */
	@Override
	public void generate(ExportServiceContext<WtUpdateCommand> context) {
		FileGeneratorContext rptContext = context.getGeneratorContext();
		try (AsposeCellsReportContext ctx = this.createContext(TEMPLATE_PATH)) {
			Generator generator = this.factory.createGenerator(context.getQuery().getCode());
			generator.generate(ctx, context.getQuery());
			ctx.saveAsExcel(this.createNewFile(rptContext, this.getReportName(OUTPUT_NAME)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
