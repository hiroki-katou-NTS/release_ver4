package nts.uk.ctx.at.function.ac.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.application.ApplicationAdapter;
import nts.uk.ctx.at.function.dom.adapter.application.ApplicationImport;
import nts.uk.ctx.at.function.dom.adapter.application.importclass.ApplicationDeadlineImport;
import nts.uk.ctx.at.request.pub.screen.ApplicationDeadlineExport;
import nts.uk.ctx.at.request.pub.screen.ApplicationExport;
import nts.uk.ctx.at.request.pub.screen.ApplicationPub;

@Stateless
public class ApplicationAcAdapter implements ApplicationAdapter {

	@Inject
	private ApplicationPub applicationPub;

	@Override
	public List<ApplicationImport> getApplicationBySID(List<String> employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		// (, appType, employeeID, appTypeName))
		List<ApplicationExport> applicationExportList = applicationPub.getApplicationBySID(employeeID, startDate,
				endDate);
		return applicationExportList.stream()
				.map(e -> new ApplicationImport(e.getAppDate(), e.getAppType(), e.getEmployeeID(), e.getAppTypeName()))
				.collect(Collectors.toList());
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ApplicationDeadlineImport getApplicationDeadline(String companyID, Integer closureID) {
		ApplicationDeadlineExport export = applicationPub.getApplicationDeadline(companyID, closureID);
		return new ApplicationDeadlineImport(export.isUseApplicationDeadline(),export.getDateDeadline());
	}

}
