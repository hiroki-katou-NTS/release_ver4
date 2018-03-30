package nts.uk.ctx.at.record.app.find.remainingnumber.nursingcareleave.data;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.remainingnumber.nursingcareleavemanagement.data.NursCareLevRemainDataRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.nursingcareleavemanagement.data.NursingCareLeaveRemainingData;

@Stateless
public class CareLeaveDataFinder {

	@Inject
	private NursCareLevRemainDataRepository repo;
	
	public CareLeaveDataDto getCareLeaveRemaining(String empId){
		Optional<NursingCareLeaveRemainingData> domain = repo.getCareByEmpId(empId);
		if(!domain.isPresent()) return null;
		return new CareLeaveDataDto(domain.get().getSId(), domain.get().getNumOfUsedDay());
	}
	
	public CareLeaveDataDto getChildCareLeaveRemaining(String empId){
		Optional<NursingCareLeaveRemainingData> domain = repo.getChildCareByEmpId(empId);
		if(!domain.isPresent()) return null;
		return new CareLeaveDataDto(domain.get().getSId(), domain.get().getNumOfUsedDay());
	}
}
