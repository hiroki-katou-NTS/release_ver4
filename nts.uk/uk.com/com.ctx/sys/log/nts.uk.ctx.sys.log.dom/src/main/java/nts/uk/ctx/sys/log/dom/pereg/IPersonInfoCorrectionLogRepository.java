package nts.uk.ctx.sys.log.dom.pereg;

import java.util.List;

import nts.uk.shr.com.security.audittrail.correction.content.pereg.PersonInfoCorrectionLog;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author vuongnv
 * 
 */
public interface IPersonInfoCorrectionLogRepository {
	List<PersonInfoCorrectionLog> findByTargetAndDate(String operationId, List<String> listEmployeeId,
			DatePeriod period);
	
	List<PersonInfoCorrectionLog> findByTargetAndDate(List<String> operationId, List<String> listEmployeeId,
			DatePeriod period);

	
	List<PersonInfoCorrectionLog> findByTargetAndDate(List<String> operationId, List<String> listEmployeeId);
	
	List<PersonInfoCorrectionLog> findByTargetAndDateScreenF(List<String> operationId, List<String> listEmployeeId);
	
	void save(List<PersonInfoCorrectionLog> correctionLogs);
}
