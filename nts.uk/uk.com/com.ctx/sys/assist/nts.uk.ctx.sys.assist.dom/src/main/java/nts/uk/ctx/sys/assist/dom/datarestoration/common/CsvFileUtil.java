package nts.uk.ctx.sys.assist.dom.datarestoration.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.csv.CSVFormat;

import nts.arc.system.ServerSystemProperties;
import nts.gul.csv.CSVParsedResult;
import nts.gul.csv.CustomCsvReader;
import nts.gul.csv.NtsCsvReader;
import nts.gul.csv.NtsCsvRecord;

public class CsvFileUtil {

	private static final String NEW_LINE_CHAR = "\r\n";
	private static final String DATA_STORE_PATH = ServerSystemProperties.fileStoragePath();

	static List<List<String>> getAllRecord(InputStream inputStream) {
		// get csv reader
		NtsCsvReader csvReader = NtsCsvReader.newReader().withNoHeader().skipEmptyLines(true)
				.withChartSet(Charset.forName("Shift_JIS"))
				.withFormat(CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_CHAR));
		List<List<String>> result = new ArrayList<>();
		try {
			CSVParsedResult csvParsedResult = csvReader.parse(inputStream);
			List<NtsCsvRecord> allRecord = csvParsedResult.getRecords();
			getDataCSV(result, allRecord);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	private static void getDataCSV(List<List<String>> result, List<NtsCsvRecord> allRecord) {
		for (NtsCsvRecord record : allRecord) {
			List<String> data = new ArrayList<>();
			for (int i = 0; i < record.columnLength(); i++) {
				data.add((String) record.getColumn(i));
			}
			result.add(data);
		}
	}

	public static List<List<String>> getAllRecord(String fileId, String fileName) {
		// get csv reader
		NtsCsvReader csvReader = NtsCsvReader.newReader().withNoHeader().skipEmptyLines(true)
				.withChartSet(Charset.forName("Shift_JIS"))
				.withFormat(CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_CHAR));
		List<List<String>> result = new ArrayList<>();
		try {
			InputStream inputStream = createInputStreamFromFile(fileId, fileName);
			if (!Objects.isNull(inputStream)) {
				CSVParsedResult csvParsedResult = csvReader.parse(inputStream);
				List<NtsCsvRecord> allRecord = csvParsedResult.getRecords();
				getDataCSV(result, allRecord);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static List<String> getCsvHeader(String fileName, String fileId) {
		// get csv reader
		NtsCsvReader csvReader = NtsCsvReader.newReader().skipEmptyLines(true)
				.withChartSet(Charset.forName("Shift_JIS"));
		try {
			InputStream inputStream = createInputStreamFromFile(fileId, fileName);
			if (!Objects.isNull(inputStream)) {
				List<String> data = new ArrayList<>();
				Consumer<CSVParsedResult> csvResult = (c) -> {
					NtsCsvRecord header = c.getRecords().get(0);
					for (int i = 0; i < header.columnLength(); i++) {
						data.add((String) header.getColumn(i));
					}
				};
				Function<NtsCsvRecord, Boolean> customCheckRow = (row) -> {
					return true;
				};
				CustomCsvReader csvCustomReader = csvReader.createCustomCsvReader(inputStream);
				csvCustomReader.setNoHeader(true);
				csvCustomReader.readByStep(2, 1, csvResult, customCheckRow);
				return data;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new ArrayList<>();
	}

	public static Set<String> getListSid(String fileId, String fileName) {
		NtsCsvReader csvReader = NtsCsvReader.newReader().skipEmptyLines(true)
				.withChartSet(Charset.forName("Shift_JIS"));
		try {
			InputStream inputStream = createInputStreamFromFile(fileId, fileName);
			CustomCsvReader csvCustomReader = csvReader.createCustomCsvReader(inputStream);
			csvCustomReader.setNoHeader(true);
			if (!Objects.isNull(inputStream)) {
				Set<String> listSid = new HashSet<>();
				Consumer<CSVParsedResult> csvResult = (c) -> {
					for (int i = 0; i < c.getRecords().size(); i++) {
						if (c.getRecords().get(i).getColumn(1) != null) {
							listSid.add(c.getRecords().get(i).getColumn(1).toString().trim());
						}
					}
				};
				
				Function<NtsCsvRecord, Boolean> customCheckRow = (row) -> {
					if (row.getColumn(1).equals("CMF003_H_SID") || row.getColumn(1).equals("") || row.getColumn(1).equals(" "))
						return false;
					return true;
				};

				csvCustomReader.readByStep(3000, csvResult,customCheckRow);
				csvCustomReader.close();
				return listSid;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new HashSet<>();
	}

	static InputStream createInputStreamFromFile(String fileId, String fileName) {
		String filePath = getExtractDataStoragePath(fileId) + "//" + fileName + ".csv";
		try {
			return new FileInputStream(new File(filePath));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public static String getExtractDataStoragePath(String fileId) {
		return DATA_STORE_PATH + "//packs//" + fileId;
	}
}
