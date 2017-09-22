package nts.uk.file.com.app;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.EmployeeUnregisterOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.unregisterapproval.EmployeeUnregisterApprovalRoot;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class EmployeeUnregisterOutputExportService extends ExportService<GeneralDate> {

	@Inject
	private EmployeeUnregisterApprovalRoot empUnregister;

	@Inject
	private EmployeeUnregisterOutputGenerator employgenerator;

	@Override
	protected void handle(ExportServiceContext<GeneralDate> context) {

		String companyId = AppContexts.user().companyId();

		// get query parameters
		GeneralDate value = context.getQuery();

		// create data source		
		List<EmployeeUnregisterOutput> items = this.empUnregister.lstEmployeeUnregister(companyId, value);
		if(CollectionUtil.isEmpty(items)) {
			throw new BusinessException("Msg_7");
		}
		HeaderEmployeeUnregisterOutput header = this.setHeader();
		val dataSource = new EmployeeUnregisterOutputDataSoure(header, items);

		// generate file
		this.employgenerator.generate(context.getGeneratorContext(), dataSource);
	}

	private HeaderEmployeeUnregisterOutput setHeader() {
		HeaderEmployeeUnregisterOutput header = new HeaderEmployeeUnregisterOutput();
		header.setNameCompany("A");
		header.setTitle("承認ルート未登録社員一覧");
		header.setEmployee("対象者");
		header.setWorkplaceCode("所属職場コード");
		header.setWorkplaceName("所属職場名");
		header.setAppName("申請名");
		return header;
	}

}
