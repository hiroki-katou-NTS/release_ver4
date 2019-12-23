package test;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.hr.develop.dom.empregulationhistory.algorithm.EmploymentRegulationHistoryInterface;
import nts.uk.ctx.hr.develop.dom.empregulationhistory.algorithm.EmploymentRegulationHistoryRepository;
import nts.uk.ctx.hr.develop.dom.empregulationhistory.dto.RegulationHistoryDto;
import test.dto.EmpRegulationHistListDto;
import test.dto.EmploymentRegulationHistoryDto;
import test.param.EmpRegHisParamDto;

@Stateless
public class FinderTest {

	@Inject
	private EmploymentRegulationHistoryRepository repo;
	
	@Inject
	private EmploymentRegulationHistoryInterface empRegHisInter;
	
	public Optional<EmploymentRegulationHistoryDto> getLatestEmpRegulationHist(String cId) {
		Optional<RegulationHistoryDto> domain = repo.getLatestEmpRegulationHist(cId);
		if(domain.isPresent()) {
			return Optional.of(new EmploymentRegulationHistoryDto(domain.get().getHistoryId(), domain.get().getBaseDate()));
		}
		return Optional.empty();
	}
	
	public EmpRegulationHistListDto getEmpRegulationHistList(String cId){
		return new EmpRegulationHistListDto(repo.getEmpRegulationHistList(cId));
	}
	
	public EmpRegHisParamDto addEmpRegulationHist(String cId, GeneralDate startDate) {
		String id = repo.addEmpRegulationHist(cId, startDate);
		return new EmpRegHisParamDto(null,id,null);
	}
	
	public void updateEmpRegulationHist(String cId, String historyId, GeneralDate startDate) {
		repo.updateEmpRegulationHist(cId, historyId, startDate);
	}
	
	public void removeEmpRegulationHist(String cId, String historyId) {
		repo.removeEmpRegulationHist(cId, historyId);
	}
	
	public EmpRegHisParamDto getHistoryIdByDate(String cId, GeneralDate baseDate){
		Optional<String> id = empRegHisInter.getHistoryIdByDate(cId, baseDate);
		if(id.isPresent()) {
			return new EmpRegHisParamDto(null,id.get(),null);
		}
		return null;
	} 
}
