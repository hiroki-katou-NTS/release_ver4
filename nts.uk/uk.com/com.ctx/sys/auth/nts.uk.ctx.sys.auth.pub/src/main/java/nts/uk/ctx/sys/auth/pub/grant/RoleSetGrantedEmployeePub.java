package nts.uk.ctx.sys.auth.pub.grant;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface RoleSetGrantedEmployeePub {

	/**
	 * request no. 139: 指定職場の承認者一覧を取得する
	 * @param workplaceId
	 * @param date period
	 * @return List<employee ID>
	 */
	public List<String> findEmpGrantedInWorkplace(String workplaceId, DatePeriod period);
	
	/**
	 * request [No.305]指定社員が基準日に承認権限を持っているかチェックする 
	 * @param companyId
	 * @param employeeID
	 * @param date
	 * @return
	 */
	public boolean canApprovalOnBaseDate(String companyId , String employeeID , GeneralDate date);
}
