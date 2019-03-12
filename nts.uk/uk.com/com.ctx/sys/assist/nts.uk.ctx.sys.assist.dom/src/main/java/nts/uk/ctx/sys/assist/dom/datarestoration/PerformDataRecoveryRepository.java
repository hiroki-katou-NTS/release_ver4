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
			String cidCurrent);
	
	Integer countDataTransactionExitTableByVKeyUp(Map<String, String> filedWhere, String tableName, String namePhysicalCid,
			String cidCurrent);

	void deleteDataExitTableByVkey(List<Map<String, String>> lsiFiledWhere2, String tableName, String namePhysicalCid,
			String cidCurrent);
	
	void deleteTransactionDataExitTableByVkey(List<Map<String, String>> lsiFiledWhere2, String tableName, String namePhysicalCid,
			String cidCurrent);
	
	void insertDataTable( StringBuilder insertToTable);
	
	void insertTransactionDataTable(StringBuilder insertToTable);

	List<TableList> getByRecoveryProcessingId(String dataRecoveryProcessId);

	List<TableList> getAllTableList();

	void deleteEmployeeHis(String tableName, String whereCid, String whereSid, String cid, String employeeId);
	
	void deleteTransactionEmployeeHis(String tableName, String whereCid, String whereSid, String cid, String employeeId);

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
