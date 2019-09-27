package nts.uk.shr.com.security.audittrail.start;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface StartPageLogRepository {

	Optional<StartPageLog> find(String operationId);

	List<StartPageLog> find(List<String> operationIds);
	
	List<StartPageLog> finds(String companyId);
	
	List<StartPageLog> finds(GeneralDate targetDate);
	
	List<StartPageLog> finds(DatePeriod targetDate);
	
	List<StartPageLog> findBySid(String sId);
	
	List<StartPageLog> findBySid(List<String> sIds);
	
	List<StartPageLog> findBy(String companyId, List<String> listEmployeeId,
			GeneralDateTime start, GeneralDateTime end, int offset, int limit);
	
	List<StartPageLog> findByScreenF(String companyId, List<String> listEmployeeId,
			GeneralDateTime start, GeneralDateTime end);
	
	List<StartPageLog> findBy(String companyId, GeneralDateTime start, GeneralDateTime end, int offSet, int limit);
	
	List<StartPageLog> findByScreenF(String companyId, GeneralDateTime start, GeneralDateTime end);
}
