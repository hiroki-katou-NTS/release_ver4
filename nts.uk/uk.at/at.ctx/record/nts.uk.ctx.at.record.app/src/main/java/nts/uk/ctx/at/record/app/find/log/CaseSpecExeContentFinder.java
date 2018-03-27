package nts.uk.ctx.at.record.app.find.log;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.find.log.dto.CaseSpecExeContentDto;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CaseSpecExeContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CaseSpecExeContentRepository;

@Stateless
public class CaseSpecExeContentFinder {

	@Inject
	private CaseSpecExeContentRepository caseSpecExeContentRepo;
	
	/**
	 * get CaseSpecExeContent By Id 
	 * @param caseSpecExeContentID
	 * @return
	 */
	public CaseSpecExeContentDto getCaseSpecExeContentById(String caseSpecExeContentID ) {
		
		Optional<CaseSpecExeContentDto> data = caseSpecExeContentRepo
				.getCaseSpecExeContentById(caseSpecExeContentID).map(c->CaseSpecExeContentDto.fromDomain(c));
		if(data.isPresent()) {
			return data.get();
		}
		return null;
	}
	/**
	 * get all CaseSpecExeContent
	 * @return
	 */
	public List<CaseSpecExeContentDto> getAllCaseSpecExeContent() {
		List<CaseSpecExeContent> temp = caseSpecExeContentRepo
				.getAllCaseSpecExeContent();
		List<CaseSpecExeContentDto> data = temp.stream().map(c->CaseSpecExeContentDto.fromDomain(c)).collect(Collectors.toList());
		if(data.isEmpty()) {
			return Collections.emptyList();
		}
		return data ;
	}
}
