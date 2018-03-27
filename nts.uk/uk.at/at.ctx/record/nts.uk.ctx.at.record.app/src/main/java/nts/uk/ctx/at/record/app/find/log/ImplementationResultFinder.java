package nts.uk.ctx.at.record.app.find.log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.find.log.dto.PersonInfoErrMessageLogDto;
import nts.uk.ctx.at.record.app.find.log.dto.ScreenImplementationResultDto;
import nts.uk.ctx.at.record.dom.adapter.person.EmpBasicInfoImport;
import nts.uk.ctx.at.record.dom.adapter.person.PersonInfoAdapter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;

/**
 * 
 * @author hieult
 *
 */
@Stateless
public class ImplementationResultFinder {
	
	@Inject
	private PersonInfoAdapter personInfoAdapter;
	
	@Inject
	private ErrMessageInfoRepository errMessageInfoRepository;
	
	public List<PersonInfoErrMessageLogDto> getScreenImplementationResult(ScreenImplementationResultDto screenImplementationResultDto) {
		
		// Get List ErrMessageInfo
		List<ErrMessageInfo> listErrMessageInfo = errMessageInfoRepository.getErrMessageInfoByExecutionContent(screenImplementationResultDto.getEmpCalAndSumExecLogID(), screenImplementationResultDto.getExecutionContent());
		
		// Get distinct list EmployeeID
		List<String> listEmployeeID = listErrMessageInfo.stream().map(c -> { return c.getEmployeeID(); }).distinct().collect(Collectors.toList());
		
		// Get PersonInfo from list EmployeeID
		List<EmpBasicInfoImport> listPersonInfo = personInfoAdapter.getListPersonInfo(listEmployeeID);
		
		// Build result
		List<PersonInfoErrMessageLogDto> result = new ArrayList<PersonInfoErrMessageLogDto>();
		for (ErrMessageInfo errMessageInfo: listErrMessageInfo) {
			EmpBasicInfoImport employeeLog = listPersonInfo.stream().filter(c -> c.getEmployeeId().equals(errMessageInfo.getEmployeeID())).findAny().get();
			PersonInfoErrMessageLogDto personInfoErrMessageLogDto = new PersonInfoErrMessageLogDto(
					employeeLog.getEmployeeCode(), employeeLog.getNamePerson(), errMessageInfo.getDisposalDay(), errMessageInfo.getMessageError().v());
			result.add(personInfoErrMessageLogDto);
		}
		
		result = result.stream().sorted(Comparator.comparing(PersonInfoErrMessageLogDto::getPersonCode)).limit(100).collect(Collectors.toList());

		return result;
		
	}
}
