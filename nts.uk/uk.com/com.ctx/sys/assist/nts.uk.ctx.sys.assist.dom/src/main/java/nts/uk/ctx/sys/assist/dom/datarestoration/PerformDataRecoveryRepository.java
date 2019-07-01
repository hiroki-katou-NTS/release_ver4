package nts.uk.ctx.sys.assist.dom.datarestoration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.uk.ctx.sys.assist.dom.category.StorageRangeSaved;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;

/**
 * データ復旧の実行
 */
public interface PerformDataRecoveryRepository {

	Optional<PerformDataRecovery> getPerformDatRecoverById(String dataRecoveryProcessId);

	void add(PerformDataRecovery domain);

	void update(PerformDataRecovery domain);

	void remove(String dataRecoveryProcessId);

	List<TableList> getByStorageRangeSaved(String categoryId,String dataStorageProcessingId, StorageRangeSaved value);

	List<Target> findByDataRecoveryId(String dataRecoveryProcessId);

	Optional<TableList> getByInternal(String internalFileName, String dataRecoveryProcessId);

	Integer countDataExitTableByVKeyUp(Map<String, String> filedWhere, String tableName, String namePhysicalCid,
			String cidCurrent, String dataRecoveryProcessId,String employeeCode);
	
	Integer countDataTransactionExitTableByVKeyUp(Map<String, String> filedWhere, String tableName, String namePhysicalCid,
			String cidCurrent, String dataRecoveryProcessId,String employeeCode);

	void deleteDataExitTableByVkey(List<Map<String, String>> lsiFiledWhere2, String tableName, String namePhysicalCid,
			String cidCurrent,String employeeCode, String dataRecoveryProcessId,TableList tableList);
	
	void deleteTransactionDataExitTableByVkey(List<Map<String, String>> lsiFiledWhere2, String tableName, String namePhysicalCid,
			String cidCurrent,String employeeCode, String dataRecoveryProcessId, TableList tableList);
	
	void insertDataTable( StringBuilder insertToTable , String employeeCode, String dataRecoveryProcessId, TableList tableList);
	
	void insertTransactionDataTable(StringBuilder insertToTable, String employeeCode, String dataRecoveryProcessId, TableList tableList);

	List<TableList> getByRecoveryProcessingId(String dataRecoveryProcessId);

	List<TableList> getAllTableList();

	void deleteEmployeeHis(TableList tableList, String whereCid, String whereSid, String cid, String employeeId,String employeeCode,String dataRecoveryProcessId );
	
	void deleteTransactionEmployeeHis(TableList tableList, String whereCid, String whereSid, String cid, String employeeId,String employeeCode, String dataRecoveryProcessId );

	void addTargetEmployee(Target domain);

	void deleteEmployeeDataRecovery(String dataRecoveryProcessId, List<String> employeeIdList);

	List<TableList> getByDataRecoveryId(String dataRecoveryProcessId);

	void updateCategorySelect(int selectionTarget, String dataRecoveryProcessId, List<String> listCheckCate);

	void updateCategorySelectByDateFromTo(String startOfPeriod, String endOfPeriod, String dataRecoveryProcessId,
			String checkCate);
	
	void deleteTableListByDataStorageProcessingId(String dataRecoveryProcessId);
	
	void addRestorationTarget(RestorationTarget domain);
	
	public void addAllTargetEmployee(List<Target> listTarget);
	
	public List<String> getTypeColumnNotNull(String TABLE_NAME);
	
}
