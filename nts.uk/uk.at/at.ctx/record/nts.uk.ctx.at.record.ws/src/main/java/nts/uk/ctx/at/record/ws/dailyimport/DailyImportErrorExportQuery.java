package nts.uk.ctx.at.record.ws.dailyimport;

import java.util.List;

import lombok.Getter;
import nts.uk.ctx.at.record.app.service.dailyimport.RecordImportError;

@Getter
public class DailyImportErrorExportQuery {

	private List<RecordImportError> errors;
}
