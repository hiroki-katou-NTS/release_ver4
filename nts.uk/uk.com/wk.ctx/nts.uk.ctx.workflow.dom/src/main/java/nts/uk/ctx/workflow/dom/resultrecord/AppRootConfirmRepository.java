package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.closure.ClosureMonth;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface AppRootConfirmRepository {

	public void insert(AppRootConfirm appRootConfirm);

	public void update(AppRootConfirm appRootConfirm);

	public void delete(AppRootConfirm appRootConfirm);
	

	/**
	 * 承認状態を作成する
	 * 
	 * @param appRootInstance
	 */
	public void createNewStatus(String companyID, String employeeID, GeneralDate date, RecordRootType rootType);

	/**
	 * 日別実績の承認状況を取得する（単一社員・単一日）
	 * 
	 * @param companyID
	 * @param employeeID
	 * @param date
	 * @return
	 */
	public Optional<AppRootConfirm> findAppRootConfirmDaily(String employeeID, GeneralDate date);

	/**
	 * 日別実績の承認状況を取得する（単一社員・期間）
	 * 
	 * @param companyID
	 * @param employeeID
	 * @param period
	 * @return
	 */
	public List<AppRootConfirm> findAppRootConfirmDaily(String employeeID, DatePeriod period);

	/**
	 * 日別実績の承認状況を取得する（複数社員・単一日）
	 * 
	 * @param companyID
	 * @param employeeIDLst
	 * @param date
	 * @return
	 */
	public List<AppRootConfirm> findAppRootConfirmDaily(List<String> employeeIDLst, GeneralDate date);

	/**
	 * 日別実績の承認状況を取得する（複数社員・期間）
	 * 
	 * @param companyID
	 * @param employeeIDLst
	 * @param period
	 * @return
	 */
	public List<AppRootConfirm> findAppRootConfirmDaily(List<String> employeeIDLst, DatePeriod period);

	/**
	 * 月別実績の承認状況を取得する（単一社員・単一集計期間）
	 * 
	 * @param companyID
	 * @param employeeID
	 * @param closureMonth
	 * @return
	 */
	public Optional<AppRootConfirm> findAppRootConfirmMonthly(String employeeID, ClosureMonth closureMonth);

	/**
	 * 月別実績の承認状況を取得する（単一社員・複数集計期間）
	 * 
	 * @param companyID
	 * @param employeeID
	 * @param closureMonthLst
	 * @return
	 */
	public List<AppRootConfirm> findAppRootConfirmMonthly(String employeeID, List<ClosureMonth> closureMonthLst);

	/**
	 * 月別実績の承認状況を取得する（複数社員・単一集計期間）
	 * 
	 * @param companyID
	 * @param employeeIDLst
	 * @param closureMonth
	 * @return
	 */
	public List<AppRootConfirm> findAppRootConfirmMonthly(List<String> employeeIDLst, ClosureMonth closureMonth);

	/**
	 * 月別実績の承認状況を取得する（複数社員・複数集計期間）
	 * 
	 * @param companyID
	 * @param employeeIDLst
	 * @param closureMonthLst
	 * @return
	 */
	public List<AppRootConfirm> findAppRootConfirmMonthly(List<String> employeeIDLst, List<ClosureMonth> closureMonthLst);

	/**
	 * 月別実績の承認状況を取得する（単一社員・年月）
	 * 
	 * @param companyID
	 * @param employeeID
	 * @param closureMonthLst
	 * @return
	 */
	public List<AppRootConfirm> findAppRootConfirmMonthly(String employeeID, YearMonth yearMonth);

	/**
	 * 月別実績の承認状況を取得する（複数社員・年月）
	 * 
	 * @param companyID
	 * @param employeeIDLst
	 * @param closureMonth
	 * @return
	 */
	public List<AppRootConfirm> findAppRootConfirmMonthly(List<String> employeeIDLst, YearMonth yearMonth);


	
	//↓修正が必要　加藤メモ（日次と月次は同時に消す必要がある）

	/**
	 * 日別実績の承認状況を削除する
	 * @param employeeID
	 * @param date
	 */
	public void deleteAppRootConfirmDaily(String employeeID, GeneralDate date);
	
	/**
	 * 月別実績の承認状況を削除する
	 * @param employeeID
	 * @param closureMonth
	 */
	public void deleteAppRootConfirmMonthly(String employeeID, ClosureMonth closureMonth);
	
}
