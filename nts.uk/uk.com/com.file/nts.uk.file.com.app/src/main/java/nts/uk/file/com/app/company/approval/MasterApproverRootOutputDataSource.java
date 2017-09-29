package nts.uk.file.com.app.company.approval;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.MasterApproverRootOutput;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MasterApproverRootOutputDataSource {
	private MasterApproverRootOutput masterApproverRootOutput;
	private boolean isCheckCompany;
	private boolean isCheckPerson;
	private boolean isCheckWorplace;
	
}
