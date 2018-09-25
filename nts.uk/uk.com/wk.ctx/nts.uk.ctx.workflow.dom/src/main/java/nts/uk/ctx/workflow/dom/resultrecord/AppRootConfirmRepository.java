package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.Optional;

import nts.arc.time.GeneralDate;

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

	public void deleteByRequestList424(String companyID, String employeeID, GeneralDate date, Integer rootType);
	
}
