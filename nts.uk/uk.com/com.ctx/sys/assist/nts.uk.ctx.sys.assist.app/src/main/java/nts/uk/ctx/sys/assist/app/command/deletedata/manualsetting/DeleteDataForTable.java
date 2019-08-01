package nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionCsvRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.EmployeeDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.TableDeletionDataCsv;

@Stateless
public class DeleteDataForTable {
	
	@Inject
	private DataDeletionCsvRepository repoCsv;
	
	/**
	 * アルゴリズム「サーバデータ削除実行カテゴリ」を実行する
	 */
	public void deleteDataForTable(TableDeletionDataCsv tableDataDel,
			List<EmployeeDeletion> employeeDeletions) {
		try {
			repoCsv.deleteData(tableDataDel, employeeDeletions);
		} catch (Exception e) {
			throw e;		
		}
	}

}
