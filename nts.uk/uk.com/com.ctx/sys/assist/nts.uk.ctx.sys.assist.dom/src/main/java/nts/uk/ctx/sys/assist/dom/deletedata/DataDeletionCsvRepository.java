/**
 * 
 */
package nts.uk.ctx.sys.assist.dom.deletedata;

import java.util.List;

import nts.arc.layer.infra.file.export.FileGeneratorContext;

/**
 * @author nam.lh
 *
 */
public interface DataDeletionCsvRepository {
	List<TableDeletionDataCsv> getTableDelDataCsvById(String delId);
	List<String> getColumnName(String nameTable);
	void backupCsvFile(TableDeletionDataCsv tableDelData, List<EmployeeDeletion> employeeDeletions, List<String> header, FileGeneratorContext generatorContext);
	void deleteData(TableDeletionDataCsv tableDelData, List<EmployeeDeletion> employeeDeletions);
}
