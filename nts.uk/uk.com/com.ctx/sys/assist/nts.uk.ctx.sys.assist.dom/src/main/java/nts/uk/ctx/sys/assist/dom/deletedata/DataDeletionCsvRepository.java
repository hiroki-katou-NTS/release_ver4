/**
 * 
 */
package nts.uk.ctx.sys.assist.dom.deletedata;

import java.util.List;

/**
 * @author nam.lh
 *
 */
public interface DataDeletionCsvRepository {
	List<TableDeletionDataCsv> getTableDelDataCsvById(String delId);
	List<String> getColumnName(String nameTable);
	List<List<String>> getDataForEachCaegory(TableDeletionDataCsv tableDelData, List<EmployeeDeletion> employeeDeletions);
	void deleteData(TableDeletionDataCsv tableDelData, List<EmployeeDeletion> employeeDeletions);
}
