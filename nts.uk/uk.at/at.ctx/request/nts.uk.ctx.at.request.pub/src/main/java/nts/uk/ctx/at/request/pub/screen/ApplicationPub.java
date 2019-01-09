package nts.uk.ctx.at.request.pub.screen;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface ApplicationPub {
	
	/**
	 * requestList #26
	 * getApplicationBySID 
	 * @param employeeID
	 * @param startDate
	 * @param endDate
	 * @return list<ApplicationExport>
	 */
	public List<ApplicationExport> getApplicationBySID(List<String> employeeID, GeneralDate startDate, GeneralDate endDate);
	
	/**
	 * requestList #239
	 * 申請締切設定を取得する
	 * @param companyID
	 * @param closureID
	 * @return
	 */
	public ApplicationDeadlineExport getApplicationDeadline(String companyID, Integer closureID);
	
	/**
	 * 社員、期間に一致する申請をグループ化して取得する
	 * RequestList #542
	 * @param employeeID
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<AppGroupExport> getApplicationGroupBySID(List<String> employeeID, GeneralDate startDate, GeneralDate endDate);
}
