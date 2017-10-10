package nts.uk.ctx.at.request.dom.application.common.adapter.bs;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.PesionInforImport;

public interface EmployeeAdapter {
	/**
	 * get employment code by companyID, employeeID and base date
	 * @param companyId 会社ID
	 * @param employeeId　社員ID　
	 * @param baseDate　基準日
	 * @return　雇用コード
	 */
	String getEmploymentCode(String companyId, String employeeId, GeneralDate baseDate);
	
	
	/**
	 * 所属職場を含む上位職場を取得
	 *
	 * @param companyId the company id
	 * @param sid the employee id
	 * @param date the date
	 * @return the list
	 */
	// RequestList #65
	List<String> findWpkIdsBySid(String companyId, String sid, GeneralDate date);

	/**
	 * 所属職場の取得 Gets the workplace id.
	 *
	 * @param companyId the company id
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the workplace id
	 */
	String getWorkplaceId(String companyId,String employeeId, GeneralDate baseDate);
	/**
	 * Get employee Name
	 * @param sID
	 * @return
	 */
	String getEmployeeName(String sID);
	
	
	PesionInforImport getEmployeeInfor(String sID);
	
}
