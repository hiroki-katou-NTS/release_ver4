package nts.uk.shr.infra.file.csv;

import java.util.List;

import nts.arc.layer.infra.file.export.FileGeneratorContext;

public interface CSVReportGenerator {

	void generate(FileGeneratorContext generatorContext, CSVFileData dataSource);
	
	CsvReportWriter generate(FileGeneratorContext generatorContext, String fileName, List<String> headers);
	
	CsvReportWriter generate(FileGeneratorContext generatorContext, String fileName, List<String> headers, String encode);
}
