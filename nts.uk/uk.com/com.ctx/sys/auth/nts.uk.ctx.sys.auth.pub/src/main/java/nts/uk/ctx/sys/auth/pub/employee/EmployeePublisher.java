package nts.uk.ctx.sys.auth.pub.employee;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface EmployeePublisher {
	/** RequestList338 */
	Optional<NarrowEmpByReferenceRange> findByEmpId(List<String> sID, int roleType, GeneralDate referenceDate);

	/** RequestList314 */
	Optional<EmpWithRangeLogin> findByCompanyIDAndEmpCD(String companyID, String employeeCD);
	
	/** RequestList315 */
	Optional<EmpWithRangeLogin> getByComIDAndEmpCD(String companyID , String employeeCD , GeneralDate referenceDate);
	
	/** RequestList334 [No.334]基準日、指定社員から参照可能な職場リストを取得する **/
	List<String> getListWorkPlaceID(String employeeID , GeneralDate referenceDate);
	
	/** RequestList218 [No.218]職場、基準日からアラーム通知先の社員を取得する  **/
	List<String> getListEmployeeId(String workplaceId, GeneralDate referenceDate);
	
	/** RequestList526 [No.526]就業担当者(社員IDList)を取得する **/
	List<String> getListEmpID(String companyID , GeneralDate referenceDate);
}
