package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface AppRootConfirmRepository {
	
	public Optional<AppRootConfirm> findByID(String rootID);
	
	public void insert(AppRootConfirm appRootInstance);
	
	public void update(AppRootConfirm appRootInstance);
	
	public void delete(AppRootConfirm appRootInstance);
	
	/**
	 * 承認状態をクリアする
	 * @param appRootInstance
	 */
	public void clearStatus(String companyID, String employeeID, GeneralDate date, RecordRootType rootType);
	
	/**
	 * 承認状態を作成する
	 * @param appRootInstance
	 */
	public void createNewStatus(String companyID, String employeeID, GeneralDate date, RecordRootType rootType);
	
	public Optional<AppRootConfirm> findByEmpDate(String companyID, String employeeID, GeneralDate date, RecordRootType rootType);
	
	public List<AppRootConfirm> findByEmpDate(String companyID, List<String> employeeID, DatePeriod date, RecordRootType rootType);

	public void deleteByRequestList424(String companyID, String employeeID, GeneralDate date, Integer rootType);
	
	public Optional<AppRootConfirm> findByEmpMonth(String companyID, String employeeID, YearMonth yearMonth,
			Integer closureID, ClosureDate closureDate, RecordRootType rootType);
	
	public Optional<AppRootConfirm> findByEmpPeriodMonth(String companyID, String employeeID, DatePeriod period);
	
	public List<AppRootConfirm> findByEmpYearMonth(String companyID, String employeeID, YearMonth yearMonth);
	
	public void clearStatusFromDate(String companyID, String employeeID, GeneralDate date, RecordRootType rootType);
	
	public List<AppRootConfirm> findByEmpLstMonth(String companyID, List<String> employeeIDLst, YearMonth yearMonth,
			Integer closureID, ClosureDate closureDate, RecordRootType rootType);
	
}
