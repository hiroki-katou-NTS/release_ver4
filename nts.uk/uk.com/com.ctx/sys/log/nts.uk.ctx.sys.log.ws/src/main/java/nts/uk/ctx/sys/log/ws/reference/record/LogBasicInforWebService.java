package nts.uk.ctx.sys.log.ws.reference.record;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.sys.log.app.find.reference.record.LogBasicInfoDto;
import nts.uk.ctx.sys.log.app.find.reference.record.LogBasicInforExportService;
import nts.uk.ctx.sys.log.app.find.reference.record.LogBasicInformationFinder;
import nts.uk.ctx.sys.log.app.find.reference.record.LogParams;
import nts.uk.ctx.sys.log.app.find.reference.record.LogScreenIParam;

/*
 * author: thuongtv
 */

@Path("ctx/sys/log/record-reference")
@Produces("application/json")
public class LogBasicInforWebService extends WebService {

	@Inject
	LogBasicInformationFinder logBasicInfoFinder;

	@Inject
	private LogBasicInforExportService exportService;

	@POST
	@Path("get-log-basic-info-by-modify-date")
	public List<LogBasicInfoDto> getLogBasicInfoByModifyDate(LogParams logParams) {
		// get LogbasicInfor
		List<LogBasicInfoDto> lstLoginBasicInfor = logBasicInfoFinder.findByOperatorsAndDate(logParams);
		return lstLoginBasicInfor;
	}

	@POST
	@Path("export-csv")
	public ExportServiceResult generate(LogScreenIParam query) {
		return this.exportService.start(query);
	}

}
