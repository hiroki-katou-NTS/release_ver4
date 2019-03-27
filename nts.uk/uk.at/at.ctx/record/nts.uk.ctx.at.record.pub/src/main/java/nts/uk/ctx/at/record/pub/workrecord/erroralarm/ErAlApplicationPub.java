package nts.uk.ctx.at.record.pub.workrecord.erroralarm;

import java.util.List;
import java.util.Optional;

public interface ErAlApplicationPub {
	Optional<ErAlApplicationPubExport> getAllErAlAppByEralCode(String companyID, String errorAlarmCode);

	List<ErAlApplicationPubExport> getAllErAlAppByEralCode(String companyID, List<String> errorAlarmCode);
}
