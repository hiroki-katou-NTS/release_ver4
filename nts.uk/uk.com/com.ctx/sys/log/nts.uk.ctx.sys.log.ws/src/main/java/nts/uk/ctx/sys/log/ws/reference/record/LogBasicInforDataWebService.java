package nts.uk.ctx.sys.log.ws.reference.record;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.sys.log.app.find.reference.record.LogBasicInfoAllDto;
import nts.uk.ctx.sys.log.app.find.reference.record.LogBasicInforAllExportService;
import nts.uk.ctx.sys.log.app.find.reference.record.LogBasicInformationAllFinder;
import nts.uk.ctx.sys.log.app.find.reference.record.LogParams;
/*
 * author: huannv
 */
import nts.uk.ctx.sys.log.app.find.reference.record.LogParamsVer1;

@Path("ctx/sys/log/record-reference")
@Produces("application/json")
public class LogBasicInforDataWebService extends WebService {
	@Inject
	LogBasicInformationAllFinder logBasicInformationAllFinder;

	@Inject
	private LogBasicInforAllExportService exportService;

	@POST
	@Path("get-log-basic-info-data-by-date")
	public List<LogBasicInfoAllDto> getLogBasicInfoByModifyDate(LogParams logParams) {
		// get LogbasicInfor
		List<LogBasicInfoAllDto> lstLoginBasicInfor = logBasicInformationAllFinder.findByOperatorsAndDate(logParams);
		return lstLoginBasicInfor;
	}

	@POST
	@Path("export-csv-screeni")
	public ExportServiceResult generate(LogParamsVer1 query) {
		return this.exportService.start(query);
	}

}
