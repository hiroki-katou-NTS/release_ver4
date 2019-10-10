package nts.uk.ctx.at.record.ac.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordImport;
import nts.uk.ctx.at.request.pub.screen.ApplicationExport;
import nts.uk.ctx.at.request.pub.screen.ApplicationPub;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ApplicationRecordAcAdapter implements ApplicationRecordAdapter{

	@Inject
	private ApplicationPub applicationPub;
	
	@Override
	public List<ApplicationRecordImport> getApplicationBySID(List<String> employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		List<ApplicationExport> applicationExportList = applicationPub.getApplicationBySID(employeeID, startDate,
				endDate);
		return applicationExportList.stream()
				.map(e -> new ApplicationRecordImport(e.getAppDate(), e.getAppType(), e.getEmployeeID(), e.getAppTypeName(), e.getReflectState()))
				.collect(Collectors.toList());
	}

}
