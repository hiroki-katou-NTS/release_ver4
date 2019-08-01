package nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.sys.assist.dom.deletedata.EmployeeDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.ManualSetDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.TableDeletionDataCsv;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class DeleteDataForCategory {
	
	@Inject
	private ManagementDeletionRepository repoManagementDel;
	@Inject 
	private SaveErrorLogDeleteResult saveErrLogDel;
	@Inject
	private DeleteDataForTable deleteDataForTable;
	public void deleteProcess(List<TableDeletionDataCsv> childTables, List<TableDeletionDataCsv> parentTables,
			List<EmployeeDeletion> employeeDeletions, ManagementDeletion managementDel, ManualSetDeletion domain ){
		
		//delete child
		for (TableDeletionDataCsv tableDataDel : childTables) {
			// アルゴリズム「サーバデータ削除実行カテゴリ」を実行する
			try {
				deleteDataForTable.deleteDataForTable(tableDataDel, employeeDeletions);
			} catch (Exception e) {
				ManagementDeletion managementDeletion = managementDel;
				int errorCount = managementDeletion.getErrorCount();
				managementDeletion.setErrorCount(errorCount + 1);
				repoManagementDel.update(managementDeletion);
				// ドメインモデル「データ削除の結果ログ」を追加する
				saveErrLogDel.saveErrorWhenDelData(domain, e.getMessage());
				throw e;
			}
		}
		
		//delete parent
		for (TableDeletionDataCsv tableDataDel : parentTables) {
			// アルゴリズム「サーバデータ削除実行カテゴリ」を実行する
			try {
				deleteDataForTable.deleteDataForTable(tableDataDel, employeeDeletions);
			} catch (Exception e) {
				ManagementDeletion managementDeletion = managementDel;
				int errorCount = managementDeletion.getErrorCount();
				managementDeletion.setErrorCount(errorCount + 1);
				repoManagementDel.update(managementDeletion);
				// ドメインモデル「データ削除の結果ログ」を追加する
				saveErrLogDel.saveErrorWhenDelData(domain, e.getMessage());
				throw e;
			}
		}
		
	}
}
