package nts.uk.ctx.at.record.dom.monthlyclosureupdatelog;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author HungTT
 *
 */

public interface MonthlyClosureUpdateLogRepository {

	public List<MonthlyClosureUpdateLog> getAll(String companyId);
	
	public List<MonthlyClosureUpdateLog> getAllSortedByExeDate(String companyId);
	
	public List<MonthlyClosureUpdateLog> getAllByClosureId(String companyId, int closureId);
	
	public Optional<MonthlyClosureUpdateLog> getLogById(String id);
	
	public Optional<MonthlyClosureUpdateLog> getLogRunningOrNotConfirmByEmpId(String companyId, int closureId, String employeeId);
	
	public void add(MonthlyClosureUpdateLog domain);
	
	public void updateStatus(MonthlyClosureUpdateLog domain);
	
	/**
	 * set status of all employee in company = 2 
	 * @param cid
	 */
	public void updateStatusForCompany(String cid);
	
}
