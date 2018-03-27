package nts.uk.ctx.at.record.app.find.log;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.find.log.dto.ErrMessageInfoDto;
import nts.uk.ctx.at.record.app.find.log.dto.InputErrMessageInfoByID;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;

@Stateless
public class ErrMessageInfoFinder {
	@Inject
	private ErrMessageInfoRepository errMessageInfoRepo;
	
	public List<ErrMessageInfoDto> getAllErrMessageInfoByID(InputErrMessageInfoByID inputErrMessageInfoByID){
		List<ErrMessageInfoDto> data = errMessageInfoRepo
				.getAllErrMessageInfoByID(inputErrMessageInfoByID.getEmpCalAndSumExecLogID(),
						inputErrMessageInfoByID.getExecutionContent())
				.stream()
				.map(c -> ErrMessageInfoDto.fromDomain(c))
				.collect(Collectors.toList());
		if(data.isEmpty())
			return Collections.emptyList();
		return data;
		
	}
	
	public List<ErrMessageInfoDto> getAllErrMessageInfoByEmpID(String empCalAndSumExecLogID){
		List<ErrMessageInfoDto> data = errMessageInfoRepo
				.getAllErrMessageInfoByEmpID(empCalAndSumExecLogID)
				.stream()
				.map(c -> ErrMessageInfoDto.fromDomain(c))
				.collect(Collectors.toList());
		if(data.isEmpty())
			return Collections.emptyList();
		return data;
		
	}
}
