package nts.uk.ctx.sys.log.app.find.reference.record;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.log.app.find.reference.LogOutputItemDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.file.csv.CSVReportGenerator;
import nts.uk.shr.infra.file.csv.CsvReportWriter;

@Stateless
public class LogBasicInforAllExportService extends ExportService<LogParamsVer1> {
	@Inject
	private CSVReportGenerator generator;
	
	@Inject
	private LogBasicInformationAllFinder finder;

	private static final String FILE_EXTENSION = ".csv";
	private static final String PRIMAKY = "primarykey";
	private static final String PGID = "CLI003";

	private List<String> getTextHeader(LogParamsVer1 params) {
		List<String> lstHeader = new ArrayList<>();
		List<LogOutputItemDto> lstOutputItemDto = params.getLstHeaderDto();
		for (LogOutputItemDto logOutputItemDto : lstOutputItemDto) {
			if (!PRIMAKY.equals(logOutputItemDto.getItemName())) {
				lstHeader.add(logOutputItemDto.getItemName());
			}
		}
		return lstHeader;
	}

	@Override
	protected void handle(ExportServiceContext<LogParamsVer1> context) {
		LogParamsVer1 params = context.getQuery();
		String employeeCode = AppContexts.user().employeeCode(); 
		this.finder.prepareData(params);
		List<String> headers = this.getTextHeader(params);
		CsvReportWriter csv = generator.generate(context.getGeneratorContext(), PGID + "_" + GeneralDateTime.now().toString("yyyyMMddHHmmss") + "_" + employeeCode + FILE_EXTENSION, headers , "UTF-8");

		CollectionUtil.split(params.getListDataExport(), 10000, sub ->{
			sub.forEach(s->{
				if(s != null) {
					csv.writeALine(s);
				}
			});
		});
		csv.destroy();
	}
}
