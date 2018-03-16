package nts.uk.ctx.exio.app.find.exi.csvimport;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.infra.file.storage.StoredFileStreamService;
import nts.uk.ctx.exio.dom.exi.service.FileUtil;

@Stateless
public class CsvImportDataFinder {
	@Inject
	private StoredFileStreamService fileStreamService;

	public int getNumberOfLine(String fileId) {
		int totalRecord = 0;
		try {
			// get input stream by fileId
			InputStream inputStream = this.fileStreamService.takeOutFromFileId(fileId);

			totalRecord = FileUtil.getNumberOfLine(inputStream);
			inputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return totalRecord;
	}

	public List<CsvMappingDataDto> getRecordByIndex(String fileId, int dataLineNum, int startLine) {
		List<CsvMappingDataDto> result = new ArrayList<>();
		try {
			// get input stream by fileId
			InputStream inputStream = this.fileStreamService.takeOutFromFileId(fileId);

			List<List<String>> data  = FileUtil.getRecordByIndex(inputStream, dataLineNum, startLine);
			inputStream.close();
			for (int i = 0; i < data.size(); i++) {
				result.add(new CsvMappingDataDto(i + 1, data.get(i).get(0), data.get(i).get(1)));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<String> getRecord(GettingCsvDataDto info) {
		List<String> result;
		try {
			// get input stream by fileId
			InputStream inputStream = this.fileStreamService.takeOutFromFileId(info.getFileId());
			result = FileUtil.getRecord(inputStream, info.getColumns(), info.getIndex());
			inputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}
