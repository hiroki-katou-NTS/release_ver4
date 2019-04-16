package nts.uk.shr.infra.file.csv;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import nts.arc.layer.infra.file.export.FileGenerator;
import nts.arc.layer.infra.file.export.FileGeneratorContext;

@Stateless
public class AsposeCSVGenerator extends FileGenerator implements CSVReportGenerator {


	@Override
	@SneakyThrows
	public void generate(FileGeneratorContext generatorContext, CSVFileData dataSource) {
		List<String> headers = dataSource.getHeaders();
		List<Map<String, Object>> datas = dataSource.getDatas();
		OutputStream os = createNewFile(generatorContext, dataSource.getFileName());
		CsvReportWriter writer = new CsvReportWriter(os, headers);
		
		datas.stream().forEach(data -> {
			
			writer.writeALine(data);
		});
		
		writer.destroy();
	}

	@Override
	@SneakyThrows
	public CsvReportWriter generate(FileGeneratorContext generatorContext, String fileName, List<String> headers) {
		return new CsvReportWriter(createNewFile(generatorContext, fileName), headers);
	}

	@Override
	@SneakyThrows
	public CsvReportWriter generate(FileGeneratorContext generatorContext, String fileName, List<String> headers, String encode) {
		return new CsvReportWriter(createNewFile(generatorContext, fileName), headers, encode);
	}
}
