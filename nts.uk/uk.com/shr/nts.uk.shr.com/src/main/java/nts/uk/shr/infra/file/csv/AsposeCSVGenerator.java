package nts.uk.shr.infra.file.csv;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import nts.arc.layer.infra.file.export.FileGenerator;
import nts.arc.layer.infra.file.export.FileGeneratorContext;

@Stateless
public class AsposeCSVGenerator extends FileGenerator implements CSVReportGenerator {

	private static final String DEFAULT_ENCODE = "Shift_JIS";

	private static final String COLUMN_SEPARATOR = ",";

	@Override
	@SneakyThrows
	public void generate(FileGeneratorContext generatorContext, CSVFileData dataSource) {
		List<String> headers = dataSource.getHeaders();
		List<Map<String, Object>> datas = dataSource.getDatas();
		try(OutputStream os = createNewFile(generatorContext, dataSource.getFileName())){
			try(BufferedWriter bof = new BufferedWriter(new OutputStreamWriter(os, Charset.forName(DEFAULT_ENCODE)))) {
				
				drawHeaderPart(headers, bof);
				
				drawBodyPart(headers, datas, bof);
			}
		}
	}

	private void drawBodyPart(List<String> headers, List<Map<String, Object>> datas, BufferedWriter bof) {
		datas.stream().forEach(data -> {
			
			drawARecord(headers, bof, h -> getCellValueByColumn(data, h));
		});
	}

	private String getCellValueByColumn(Map<String, Object> data, String h) {
		Object value = data.get(h);
		if(value == null){
			return "";
		}
		return value.toString();
	}

	private void drawHeaderPart(List<String> headers, BufferedWriter bof) {

		drawARecord(headers, bof, h -> h);
	}

	@SneakyThrows
	private void drawARecord(List<String> headers, BufferedWriter bof, Function<String, String> mapper) {
		bof.write(headers.stream().map(mapper).collect(Collectors.joining(COLUMN_SEPARATOR)));
		bof.newLine();
	}
}
