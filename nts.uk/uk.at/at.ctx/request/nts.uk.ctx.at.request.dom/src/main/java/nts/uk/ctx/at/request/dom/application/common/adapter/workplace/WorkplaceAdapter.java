package nts.uk.ctx.at.request.dom.application.common.adapter.workplace;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.employee.EmployeeBasicInfoExport;
/**
 * 
 * @author hoatt
 *
 */
public interface WorkplaceAdapter {

	/**
	 * アルゴリズム「社員から職場を取得する」を実行する
	 * @param sID
	 * @param date
	 * @return
	 */
	public WkpHistImport findWkpBySid(String sID, GeneralDate date);

	Optional<EmploymentHistoryImported> getEmpHistBySid(String companyId, String employeeId, GeneralDate baseDate);
	
	List<EmployeeBasicInfoImport> findBySIds(List<String> sIds);
}
