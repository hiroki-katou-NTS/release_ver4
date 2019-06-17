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
		DataRecoveryLog dataRecoveryLog = new DataRecoveryLog(recoveryProcessId, target, errorContent, logTime,
				logSequenceNumber, processingContent, contentSql);
		repoDataRecoveryLog.add(dataRecoveryLog);
	}
	
	public void saveStartDataRecoverLog(String dataRecoveryProcessId) {
		GeneralDate logTime = GeneralDate.today();
		DataRecoveryLog resultLogDomain = DataRecoveryLog.createFromJavatype(dataRecoveryProcessId, null, null, logTime,
				0, null, null);
		repoDataRecoveryLog.add(resultLogDomain);
	}

}
