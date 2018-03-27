package nts.uk.ctx.at.record.app.find.log;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.find.log.dto.TargetPersonDto;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPersonRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class TargetPersonFinder {

	@Inject
	private TargetPersonRepository  targetPersonRepo;
	
	/**
	 * get all TargetPerson
	 * @return
	 */
	public List<TargetPersonDto> getAllTargetPerson(){
		String employeeID = AppContexts.user().employeeId();
		List<TargetPersonDto> data = targetPersonRepo.getAllTargetPerson(employeeID)
				.stream()
				.map(c -> TargetPersonDto.fromDomain(c))
				.collect(Collectors.toList());
		if(data.isEmpty())
			return Collections.emptyList();
		return data;
	}
	/**
	 * get TargetPerson by code
	 * @param empCalAndSumExecLogId
	 * @return
	 */
	public TargetPersonDto getTargetPersonByID(String empCalAndSumExecLogId){
		String employeeID = AppContexts.user().employeeId();
		Optional<TargetPersonDto> data = targetPersonRepo.getTargetPersonByID(employeeID, empCalAndSumExecLogId)
				.map(c -> TargetPersonDto.fromDomain(c));
		if(data.isPresent())
			return data.get();
		return null;
	}
	
	/**
	 * get list TargetPerson by empCalAndSumExecLogId
	 * @param empCalAndSumExecLogId
	 * @return
	 */
	public List<TargetPersonDto> getListTargetPersonByEmpId(String empCalAndSumExecLogId){
		List<TargetPersonDto> data = targetPersonRepo.getByempCalAndSumExecLogID(empCalAndSumExecLogId).stream()
				.map(c -> TargetPersonDto.fromDomain(c)).collect(Collectors.toList());
		if(data.isEmpty())
			return Collections.emptyList();
		return data;
	}
	
	
}
