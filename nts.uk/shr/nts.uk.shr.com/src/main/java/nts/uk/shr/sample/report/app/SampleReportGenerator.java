package nts.uk.shr.sample.report.app;

import nts.arc.layer.infra.file.export.FileGeneratorContext;

public interface SampleReportGenerator {

	void generate(FileGeneratorContext generatorContext, SampleReportDataSource dataSource);
}
