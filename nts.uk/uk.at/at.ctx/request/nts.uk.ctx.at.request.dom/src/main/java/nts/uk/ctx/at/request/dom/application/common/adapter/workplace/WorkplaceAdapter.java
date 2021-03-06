package nts.uk.ctx.at.request.dom.application.common.adapter.workplace;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.WorkplaceInforExport;
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
	
	public List<WkpHistImport> findWkpBySid(List<String> sID, GeneralDate date);

	List<WkpHistImport> findWkpBySidAndBaseDate(List<String> sID, GeneralDate date);

	Optional<EmploymentHistoryImported> getEmpHistBySid(String companyId, String employeeId, GeneralDate baseDate);
	
	List<EmployeeBasicInfoImport> findBySIds(List<String> sIds);
	
	WorkPlaceHistBySIDImport findWpkBySIDandPeriod(String sID, DatePeriod datePeriod);
	/**
	 * 上位階層の職場の設定を取得する
	 * RequestList #83
	 */
	public List<String> findListWpkIDParent(String companyId, String workplaceId, GeneralDate date);
	/**
	 * 上位階層の職場の設定を取得する
	 * RequestList #83-1
	 */
	public List<String> findListWpkIDParentDesc(String companyId, String workplaceId, GeneralDate date);
	
	public List<String> getUpperWorkplaceRQ569(String companyId, String workplaceId, GeneralDate date);
	
	public List<WorkplaceInforExport> getWorkplaceInforByWkpIds(String companyId, List<String> listWorkplaceId,
			GeneralDate baseDate);
	
	Optional<WorkPlaceAuthorityImport> getWorkPlaceAuthorityById(String companyId, String roleId, int functionNo);
}
