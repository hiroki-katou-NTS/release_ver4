package nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.unregisterapproval;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.employee.EmployeeApproveDto;
/**
 * 01.承認ルート未登録の社員を取得する
 * @author dudt
 *
 */
public interface EmployeeUnregisterApprovalRoot {
	/**
	 * 01.承認ルート未登録の社員を取得する
	 * @param companyId
	 * @param baseDate
	 * @return
	 */
	List<EmployeeApproveDto> lstEmployeeUnregister(String companyId, GeneralDate baseDate);
}
