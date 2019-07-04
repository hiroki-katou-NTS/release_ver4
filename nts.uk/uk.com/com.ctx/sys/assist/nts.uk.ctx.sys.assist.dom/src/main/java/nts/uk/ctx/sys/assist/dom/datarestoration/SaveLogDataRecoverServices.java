/**
 * 
 */
package nts.uk.ctx.sys.assist.dom.datarestoration;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;



@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SaveLogDataRecoverServices {
	
	@Inject
	private DataRecoveryLogRepository repoDataRecoveryLog;
	
	public void saveErrorLogDataRecover(String recoveryProcessId, String target, String errorContent,
			GeneralDate targetDate, String processingContent, String contentSql) {
		GeneralDate logTime = GeneralDate.today();
		int logSequenceNumber = repoDataRecoveryLog.getMaxSeqId(recoveryProcessId) + 1;
		String errorContent_ = "";
		if (errorContent.length() > 1999) {
			errorContent_ = errorContent.substring(0,1990);
		}else{
			errorContent_ = errorContent;
		}
		
		String processingContent_ = "";
		if (processingContent.length() > 100) {
			processingContent_ = processingContent.substring(0, 95);
		}else{
			processingContent_ = processingContent;
		}
		
		String contentSql_ = "";
		if (contentSql.length() > 1999) {
			contentSql_ = contentSql.substring(0, 1990);
		}else{
			contentSql_ = contentSql;
		}
		 
		
		DataRecoveryLog dataRecoveryLog = new DataRecoveryLog(recoveryProcessId, target, errorContent_, logTime,
				logSequenceNumber, processingContent_, contentSql_);
		repoDataRecoveryLog.add(dataRecoveryLog);
		System.out.println("testtt");
	}
}
