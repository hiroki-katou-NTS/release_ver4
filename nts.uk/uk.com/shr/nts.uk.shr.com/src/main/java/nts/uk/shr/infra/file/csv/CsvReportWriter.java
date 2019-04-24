package nts.uk.shr.infra.file.csv;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.SneakyThrows;

public class CsvReportWriter {

	private static final String COLUMN_SEPARATOR = ",";
	private static final String DEFAULT_ENCODE = "MS932";

	private List<String> headers;

	private OutputStream os;

	private BufferedWriter writer;
	
	private String encode;

	public CsvReportWriter(OutputStream os, List<String> headers) {
		this(os, headers, DEFAULT_ENCODE);
	}
	
	public CsvReportWriter(OutputStream os, List<String> headers, String encode) {
		this.os = os;
		this.writer = new BufferedWriter(new OutputStreamWriter(os, Charset.forName(encode)));
		this.encode = encode;
		processNewHeaders(headers);
	}

	private void processNewHeaders(Collection<String> headers) {
		this.headers = new ArrayList<>(headers);
		if (!this.headers.isEmpty()) {
			drawHeader();
		}
	}

	public void writeALine(Map<String, Object> line) {
		if (this.headers.isEmpty()) {
			processNewHeaders(line.keySet());
		}
		drawARecord(headers, writer, h -> getCellValueByColumn(line, h));
	}

	@SneakyThrows
	private void drawHeader() {
		if(this.encode.toLowerCase().equals("utf8") || this.encode.toLowerCase().equals("utf-8")){
			writer.write('\uFEFF');
		}
		drawARecord(this.headers, writer, h -> h);
	}

	private String getCellValueByColumn(Map<String, Object> data, String h) {
		Object value = data.get(h);
		if (value == null) {
			return "";
		}
		return value.toString();
	}

	@SneakyThrows
	private void drawARecord(List<String> headers, BufferedWriter bof, Function<String, String> mapper) {
		bof.write(headers.stream().map(mapper).collect(Collectors.joining(COLUMN_SEPARATOR)));
		bof.newLine();
	}

	@SneakyThrows
	public void destroy() {
		this.writer.close();
		this.os.close();
		this.os = null;
		this.writer = null;
	}
}
