package nts.uk.ctx.at.record.dom.workrecord.erroralarm;

import java.util.List;
import java.util.Optional;

public interface ErAlApplicationRepository {
	
	Optional<ErAlApplication> getAllErAlAppByEralCode(String companyID,String errorAlarmCode);
	
	List<ErAlApplication> getAllErAlAppByEralCode(String companyID, List<String> errorAlarmCode);
}
