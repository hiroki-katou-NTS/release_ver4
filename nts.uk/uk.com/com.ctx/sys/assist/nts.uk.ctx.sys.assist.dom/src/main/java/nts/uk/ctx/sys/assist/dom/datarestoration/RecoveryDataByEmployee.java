package nts.uk.ctx.sys.assist.dom.datarestoration;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.gul.csv.CSVBufferReader;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RecoveryDataByEmployee {
	@Inject
	private PerformDataRecoveryRepository performDataRecoveryRepository; 
	@Inject
	private ProcessRecoverTable processRecoverOneTable;
	public DataRecoveryOperatingCondition recoveryDataByEmployee(String dataRecoveryProcessId, String employeeId,
			List<DataRecoveryTable> listTableOrder,HashMap<String, CSVBufferReader> csvByteReadMaper, String employeeCode,List<TableList> listTbl) throws Exception {

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		
		Optional<PerformDataRecovery> performDataRecovery = performDataRecoveryRepository.getPerformDatRecoverById(dataRecoveryProcessId);
		
		// current target Data [カレント対象データ]
		for (DataRecoveryTable dataRecoveryTable : listTableOrder) {
			Optional<TableList> table = listTbl.stream().filter(tbl -> tbl.getTableEnglishName().equals(dataRecoveryTable.getTableEnglishName())).findFirst();
			if (!table.isPresent())
				return DataRecoveryOperatingCondition.ABNORMAL_TERMINATION;

			// start of transaction
			condition = processRecoverOneTable.recoverTable(table, employeeCode, employeeId, dataRecoveryProcessId,
						dataRecoveryTable, performDataRecovery, csvByteReadMaper);
		}
		
		return condition;
	}

}
