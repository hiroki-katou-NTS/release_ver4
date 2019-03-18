package nts.uk.ctx.at.function.app.export.alarm;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.uk.ctx.at.function.dom.alarm.export.AlarmListGenerator;
import nts.uk.ctx.at.function.dom.alarm.sendemail.ValueExtractAlarmDto;

@Stateless
public class AlarmExportService extends ExportService<AlarmExportQuery> {
	@Inject
	private AlarmListGenerator generator;
	@Override
	protected void handle(ExportServiceContext<AlarmExportQuery> context) {
		AlarmExportQuery query = context.getQuery();
		List<ValueExtractAlarmDto> dataSource = query.getData();
		// invoke generator
		this.generator.generateExcelScreen(context.getGeneratorContext(), dataSource,query.getCurrentAlarmCode());
	}
}
