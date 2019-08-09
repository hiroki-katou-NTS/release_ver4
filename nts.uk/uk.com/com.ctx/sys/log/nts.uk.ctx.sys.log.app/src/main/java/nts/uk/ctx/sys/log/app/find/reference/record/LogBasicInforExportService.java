package nts.uk.ctx.sys.log.app.find.reference.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.log.app.find.reference.LogOutputItemDto;
import nts.uk.ctx.sys.log.dom.reference.RecordTypeEnum;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.file.csv.CSVFileData;
import nts.uk.shr.infra.file.csv.CSVReportGenerator;

@Stateless
public class LogBasicInforExportService extends ExportService<LogParams> {
	@Inject
	private CSVReportGenerator generator;

	private static final String FILE_EXTENSION = ".csv";
	private static final String PRIMAKY = "primarykey";
    private static final String PARENTKEY = "parentkey";
	private static final String PGID = "CLI003";
	private List<String> listHeader = new ArrayList<>();

	private void getTextHeader(LogParams params) {
		listHeader = new ArrayList<>();
		List<LogOutputItemDto> lstOutputItemDto = params.getLstHeaderDto();
		for (LogOutputItemDto logOutputItemDto : lstOutputItemDto) {
            if(!PRIMAKY.equals(logOutputItemDto.getItemName()) && !PARENTKEY.equals(logOutputItemDto.getItemName())){
				listHeader.add(logOutputItemDto.getItemName());
			}
		}
	}
	private List<String> getTextSubHeaderPersion(LogBasicInfoDto logBasicInfoDto) {
		List<String> lstHeader = new ArrayList<>();
		List<LogOutputItemDto> lstOutputItemDto = logBasicInfoDto.getLstLogOutputItemDto();
		for (LogOutputItemDto logOutputItemDto : lstOutputItemDto) {
			lstHeader.add(logOutputItemDto.getItemName());
		}
		return lstHeader;
	}

	private List<Map<String, Object>> getLogRecordData(LogParams params) {
		List<Map<String, Object>> dataSource = new ArrayList<>();
		List<LogBasicInfoDto> data = params.getLstLogBasicInfoDto();
		for (LogBasicInfoDto d : data) {
			Map<String, Object> row = new HashMap<>();
			row.put(listHeader.get(0), d.getEmployeeCodeLogin());
			row.put(listHeader.get(1), d.getUserNameLogin());
			row.put(listHeader.get(2), d.getModifyDateTime());
			row.put(listHeader.get(3), d.getMethodName());
			row.put(listHeader.get(4), d.getLoginStatus());
			row.put(listHeader.get(5), d.getNote());
			dataSource.add(row);
		}
		return dataSource;
	}
	private List<Map<String, Object>> getStartUpData(LogParams params) {
		List<Map<String, Object>> dataSource = new ArrayList<>();
		List<LogBasicInfoDto> data = params.getLstLogBasicInfoDto();
		for (LogBasicInfoDto d : data) {
			Map<String, Object> row = new HashMap<>();
			row.put(listHeader.get(0), d.getEmployeeCodeLogin());
			row.put(listHeader.get(1), d.getUserNameLogin());
			row.put(listHeader.get(2), d.getModifyDateTime());
			row.put(listHeader.get(3), d.getMenuName());
			row.put(listHeader.get(4), d.getNote());
			dataSource.add(row);
		}
		return dataSource;
	}
	
	private List<Map<String, Object>> getDataCorrectLog(LogParams params) {
		List<Map<String, Object>> dataSource = new ArrayList<>();
		List<LogBasicInfoDto> data = params.getLstLogBasicInfoDto();
		int count = 0;
		for (LogBasicInfoDto d : data) {
			// check list child and generate row child
			List<LogDataCorrectRecordRefeDto> lstDataCorrect = d.getLstLogDataCorrectRecordRefeDto();
			if (!CollectionUtil.isEmpty(lstDataCorrect)) {
				if (count == 0) {
					// add header sub
					List<String> subHeadersTemp = this.getTextSubHeaderPersion(d);
					listHeader.addAll(subHeadersTemp);
					count++;
				}
				for (LogDataCorrectRecordRefeDto dataCorrectLog : lstDataCorrect) {
					// add data parent
					Map<String, Object> row = new HashMap<>();
					row.put(listHeader.get(0), d.getEmployeeCodeTaget());
					row.put(listHeader.get(1), d.getUserNameTaget());
					row.put(listHeader.get(2), d.getEmployeeCodeLogin());
					row.put(listHeader.get(3), d.getUserNameLogin());
					row.put(listHeader.get(4), d.getModifyDateTime());
					row.put(listHeader.get(5), dataCorrectLog.getTargetDate());
					row.put(listHeader.get(6), dataCorrectLog.getItemName());
					row.put(listHeader.get(7), dataCorrectLog.getValueBefore());
					row.put(listHeader.get(8), dataCorrectLog.getValueAfter());
					row.put(listHeader.get(9), dataCorrectLog.getCorrectionAttr());
					dataSource.add(row);
				}
			}
		}
		return dataSource;
	}
	
	private List<Map<String, Object>> getPersionCorrectLog(LogParams params) {
		List<Map<String, Object>> dataSource = new ArrayList<>();
		List<LogBasicInfoDto> data = params.getLstLogBasicInfoDto();
		int count = 0;
		for (LogBasicInfoDto d : data) {
			
			// check list child and generate row child
			List<LogPerCateCorrectRecordDto> lstPersionCorrect = d.getLstLogPerCateCorrectRecordDto();
			
			if (!CollectionUtil.isEmpty(lstPersionCorrect)) {
				if (count == 0) {
					// add header sub
					List<String> subHeadersTemp = this.getTextSubHeaderPersion(d);
					listHeader.add(subHeadersTemp.get(0));
					listHeader.add(subHeadersTemp.get(1));
					listHeader.add(subHeadersTemp.get(2));
					listHeader.add(subHeadersTemp.get(3));
					listHeader.add(subHeadersTemp.get(4));
					listHeader.add(subHeadersTemp.get(5));
					count++;
				}
				for (LogPerCateCorrectRecordDto persionCorrectLog : lstPersionCorrect) {
					// add data for parent
					Map<String, Object> row = new HashMap<>();
					row.put(listHeader.get(0), d.getEmployeeCodeTaget());
					row.put(listHeader.get(1), d.getUserNameTaget());
					row.put(listHeader.get(2), d.getProcessAttr());
					row.put(listHeader.get(3), d.getEmployeeCodeLogin());
					row.put(listHeader.get(4), d.getUserNameLogin());
					row.put(listHeader.get(5), d.getModifyDateTime());
					row.put(listHeader.get(6), d.getNote());
					// add data for chidrent
					row.put(listHeader.get(7), persionCorrectLog.getCategoryName());
					row.put(listHeader.get(8), persionCorrectLog.getTargetDate());
					row.put(listHeader.get(9), persionCorrectLog.getItemName());
					row.put(listHeader.get(10), persionCorrectLog.getInfoOperateAttr());
					row.put(listHeader.get(11), persionCorrectLog.getValueBefore());
					row.put(listHeader.get(12), persionCorrectLog.getValueAfter());
					dataSource.add(row);
				}
			}
		}
		return dataSource;
	}


	@Override
	protected void handle(ExportServiceContext<LogParams> context) {
		LogParams params = context.getQuery();
		this.getTextHeader(params);
		RecordTypeEnum recordTypeEnum = RecordTypeEnum.valueOf(params.getRecordType());
		List<Map<String, Object>> dataSource = new ArrayList<>();
		switch (recordTypeEnum) {
		case LOGIN:
			dataSource = getLogRecordData(params);
			break;
		case START_UP:
			dataSource = getStartUpData(params);
			break;
		case UPDATE_PERSION_INFO:
			dataSource = getPersionCorrectLog(params);
			break;
		case DATA_CORRECT:
			dataSource = getDataCorrectLog(params);
			break;

		default:
			break;
		}
		String employeeCode = AppContexts.user().employeeCode();
		CSVFileData fileData = new CSVFileData(
				PGID + "_" + GeneralDateTime.now().toString("yyyyMMddHHmmss") + "_" + employeeCode + FILE_EXTENSION, listHeader, dataSource);
		generator.generate(context.getGeneratorContext(), fileData);
	}
}
