package nts.uk.ctx.at.record.app.find.resultsperiod.optionalaggregationperiod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.record.dom.executionstatusmanage.optionalperiodprocess.AggrPeriodInfor;
import nts.uk.ctx.at.record.dom.executionstatusmanage.optionalperiodprocess.AggrPeriodInforRepository;
import nts.uk.ctx.at.record.dom.resultsperiod.optionalaggregationperiod.OptionalAggrPeriod;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class AggrPeriodErrorInfoFinder {

	@Inject
	private AggrPeriodInforRepository errorInfoRepo;

	@Inject
	private EmployeeRecordAdapter empAdapter;

	public List<AggrPeriodErrorInfoDto> findAll(String aggrPeriodId) {
		List<AggrPeriodErrorInfoDto> result = new ArrayList<>();
		List<AggrPeriodInfor> listErr = errorInfoRepo.findAll(aggrPeriodId);
		for (AggrPeriodInfor err : listErr) {
			EmployeeRecordImport empInfo = empAdapter.getPersonInfor(err.getMemberId());
			AggrPeriodErrorInfoDto dto = new AggrPeriodErrorInfoDto(err.getMemberId(), empInfo.getEmployeeCode(),
					empInfo.getPname(), err.getProcessDay(), err.getErrorMess().v());
			result.add(dto);
		}
		result.sort((AggrPeriodErrorInfoDto c1, AggrPeriodErrorInfoDto c2) -> c1.getEmployeeCode()
				.compareTo(c2.getEmployeeCode()));
		return result;
	}
	
	public List<PeriodInforDto> findAllInfo(String periodArrgLogId){
		return errorInfoRepo.findAll(periodArrgLogId).stream().map(e -> {
			return convertToDbType(e);
		}).collect(Collectors.toList());
	}
	
	private PeriodInforDto convertToDbType(AggrPeriodInfor infor) {
		PeriodInforDto periodInforDto = new PeriodInforDto();
			periodInforDto.setMemberId(infor.getMemberId());
			periodInforDto.setPeriodArrgLogId(infor.getPeriodArrgLogId());
			periodInforDto.setResourceId(infor.getResourceId());
			periodInforDto.setProcessDay(infor.getProcessDay());
			periodInforDto.setErrorMess(infor.getErrorMess().v());
		return periodInforDto;
	}

}
