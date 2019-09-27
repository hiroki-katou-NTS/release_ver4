package nts.uk.ctx.sys.log.app.find.reference.record;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class LogBasicInforAllExportService extends ExportService<LogParamsVer1> {
	
	@Inject
	private LogBasicInformationAllFinder finder;

	@Override
	protected void handle(ExportServiceContext<LogParamsVer1> context) {
		LogParamsVer1 params = context.getQuery();
		String employeeCode = AppContexts.user().employeeCode();
		try {
			this.finder.prepareData(employeeCode, params, context);
		} catch (BusinessException e) {
			throw e;
		}
		
	}
}
