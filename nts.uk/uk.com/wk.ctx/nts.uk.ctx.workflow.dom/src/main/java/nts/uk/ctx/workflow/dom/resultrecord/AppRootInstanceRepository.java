package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface AppRootInstanceRepository {
	
	public Optional<AppRootInstance> findByID(String rootID);
	
	public void insert(AppRootInstance appRootInstance);
	
	public void update(AppRootInstance appRootInstance);
	
	public void delete(AppRootInstance appRootInstance);
	
	public Optional<AppRootInstance> findByEmpDate(String companyID, String employeeID, GeneralDate recordDate, RecordRootType rootType);
	
	public Optional<AppRootInstance> findByEmpDateNewest(String companyID, String employeeID, RecordRootType rootType);
	
	public List<AppRootInstance> findByEmpLstPeriod(List<String> employeeIDLst, DatePeriod period, RecordRootType rootType);
	
	public List<AppRootInstance> findByEmpLstPeriod(String compID, List<String> employeeIDLst, DatePeriod period, RecordRootType rootType);
	
	public List<AppRootInstance> findByApproverPeriod(String approverID, DatePeriod period, RecordRootType rootType);
	
	public List<AppRootInstance> findByApproverEmployeePeriod(String companyId, String approverID, List<String> employeeIDs, DatePeriod period, RecordRootType rootType);
	
	public List<AppRootInstance> findByEmpFromDate(String companyID, String employeeID, GeneralDate recordDate, RecordRootType rootType);
	
	public Optional<AppRootInstance> findByEmpDateNewestBelow(String companyID, String employeeID, GeneralDate recordDate, RecordRootType rootType);
	
	public Optional<AppRootInstance> findByContainDate(String companyID, String employeeID, GeneralDate recordDate, RecordRootType rootType);
	
	public List<AppRootInstance> findByApproverDateCID(String companyID, String approverID, GeneralDate date, RecordRootType rootType);
	
	public List<String> findEmpLstByRq610(String approverID, DatePeriod period, RecordRootType rootType);
	
}
