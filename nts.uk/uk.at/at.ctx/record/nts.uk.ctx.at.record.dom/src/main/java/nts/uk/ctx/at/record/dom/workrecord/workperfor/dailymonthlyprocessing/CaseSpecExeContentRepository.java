package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;
import java.util.Optional;

public interface CaseSpecExeContentRepository {

	/**
	 * get CaseSpecExeContent by caseSpecExeContentID
	 * @param caseSpecExeContentID
	 * @return
	 */
	Optional<CaseSpecExeContent> getCaseSpecExeContentById(String caseSpecExeContentID );
	/**
	 * get all CaseSpecExeContent 
	 * @param caseSpecExeContentID
	 * @return
	 */
	List<CaseSpecExeContent> getAllCaseSpecExeContent();
}
