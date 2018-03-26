package nts.uk.ctx.exio.dom.exi.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;

import nts.gul.csv.CSVParsedResult;
import nts.gul.csv.NtsCsvReader;
import nts.gul.csv.NtsCsvRecord;

public class FileUtil {
	
	private static final String NEW_LINE_CHAR = "\r\n";

	public static int getNumberOfLine(InputStream inputStream) {
		// get csv reader
		NtsCsvReader csvReader = NtsCsvReader.newReader().withNoHeader().skipEmptyLines(true)
				.withFormat(CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_CHAR));

		int count = 0;
		try {
			CSVParsedResult csvParsedResult = csvReader.parse(inputStream);
			count = csvParsedResult.getRecords().size();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return count;
	}

	public static List<List<String>> getRecordByIndex(InputStream inputStream, int dataLineNum, int startLine) {
		// get csv reader
		NtsCsvReader csvReader = NtsCsvReader.newReader().withNoHeader().skipEmptyLines(true)
				.withFormat(CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_CHAR));
		List<List<String>> result = new ArrayList<>();
		try {
			CSVParsedResult csvParsedResult = csvReader.parse(inputStream);
			NtsCsvRecord colHeader = csvParsedResult.getRecords().get(dataLineNum - 1);
			NtsCsvRecord record = csvParsedResult.getRecords().get(startLine - 1);
			for (int i = 0; i < record.columnLength(); i++) {
				List<String> data = new ArrayList<>();
				data.add((String) colHeader.getColumn(i));
				data.add((String) record.getColumn(i));
				result.add(data);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static List<String> getRecord(InputStream inputStream, int[] columns, int index) {
		// get csv reader
		NtsCsvReader csvReader = NtsCsvReader.newReader().withNoHeader().skipEmptyLines(true)
				.withFormat(CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_CHAR));
		List<String> result = new ArrayList<>();
		try {
			CSVParsedResult csvParsedResult = csvReader.parse(inputStream);
			NtsCsvRecord record = csvParsedResult.getRecords().get(index);
			for (int i = 0; i < columns.length; i++) {
				result.add((String) record.getColumn(columns[i]));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
