package nts.uk.ctx.at.record.ws.dailyimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.arc.task.AsyncTaskInfo;
import nts.uk.ctx.at.record.app.command.dailyimport.DailyDataImportExecuteHandler;
import nts.uk.ctx.at.record.app.command.dailyimport.DailyDataImportExexuteCommand;
import nts.uk.ctx.at.record.app.command.dailyimport.DailyDataImportTruncateDataHandler;
import nts.uk.shr.infra.file.csv.CSVExportService;
import nts.uk.shr.infra.file.csv.CSVFileData;

@Path("daily/import")
@Produces("application/json")
public class DailyDataImportWebService extends WebService{

	@Inject
	private DailyDataImportExecuteHandler executer;
	
	@Inject
	private DailyDataImportTruncateDataHandler truncater;
	
	@Inject
	private CSVExportService exportService;
	
	@POST
	@Path("import")
	public AsyncTaskInfo execute(DailyDataImportExexuteCommand command) {
		return executer.handle(command);
	}
	
	@POST
	@Path("truncate")
	public void truncate() {
		truncater.handle(null);
	}
	
	@POST
	@Path("export/errors")
	public ExportServiceResult exportErrors(DailyImportErrorExportQuery query) {
		CSVFileData fileData = new CSVFileData("エラー一覧.CSV", Arrays.asList("従業員コード", "対象日", "項目名", "メッセージ"), new ArrayList<>());
		query.getErrors().stream().forEach(e -> {
			Map<String, Object> record = new HashMap<>();
			record.put("従業員コード", "'" + e.getEmployeeCode());
			record.put("対象日", e.getYmd() == null ? "" : e.getYmd().toString("yyyy/MM/dd"));
			record.put("項目名", e.getItems().isEmpty() ? "" : e.getItems().stream().collect(Collectors.joining(" ")));
			record.put("メッセージ", e.getMessage());
			fileData.getDatas().add(record);
		});
		return exportService.start(fileData);
	}
}
