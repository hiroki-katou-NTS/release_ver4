package nts.uk.ctx.at.function.ac.worklocation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.worklocation.RecordWorkInfoFunAdapter;
import nts.uk.ctx.at.function.dom.adapter.worklocation.RecordWorkInfoFunAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.worklocation.WorkInfoOfDailyPerFnImport;
//import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.pub.workinformation.InfoCheckNotRegisterPubExport;
import nts.uk.ctx.at.record.pub.workinformation.RecordWorkInfoPub;
import nts.uk.ctx.at.record.pub.workinformation.WorkInfoOfDailyPerExport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class WorkLocationFunAcFinder implements RecordWorkInfoFunAdapter {

	@Inject
	private RecordWorkInfoPub recordWorkInfoPub;

	@Override
	public Optional<RecordWorkInfoFunAdapterDto> getInfoCheckNotRegister(String employeeId, GeneralDate ymd) {
		Optional<InfoCheckNotRegisterPubExport> data = recordWorkInfoPub.getInfoCheckNotRegister(employeeId, ymd);
		if(data.isPresent())
			return Optional.of(convertToExport(data.get()));
		return Optional.empty();
	}
	
	private RecordWorkInfoFunAdapterDto convertToExport(InfoCheckNotRegisterPubExport export) {
		return new  RecordWorkInfoFunAdapterDto(
					export.getEmployeeId(),
					export.getWorkTimeCode(),
					export.getWorkTypeCode(),
					export.getWorkingDate());
	}

	@Override
	public Optional<String> getWorkTypeCode(String employeeId, GeneralDate ymd) {		
		return recordWorkInfoPub.getWorkTypeCode(employeeId, ymd);
	}

	@Override
	public List<RecordWorkInfoFunAdapterDto> findByPeriodOrderByYmdAndEmps(List<String> employeeIds, DatePeriod datePeriod) {
		List<InfoCheckNotRegisterPubExport> data = recordWorkInfoPub.findByPeriodOrderByYmdAndEmps(employeeIds, datePeriod);
		if(data.isEmpty())
			return Collections.emptyList();
		
		return data.stream().map(c->convertToExport(c)).collect(Collectors.toList());
	}

	
	@Override
	public List<WorkInfoOfDailyPerFnImport> findByPeriodOrderByYmd(String employeeId) {
		List<WorkInfoOfDailyPerExport> workInfo = this.recordWorkInfoPub.findByEmpId(employeeId);
		if(workInfo.isEmpty())
			return Collections.emptyList();
		return workInfo.stream().map(c->convertToWorkInfoOfDailyPerformance(c)).collect(Collectors.toList());
	}
	
	private WorkInfoOfDailyPerFnImport convertToWorkInfoOfDailyPerformance(WorkInfoOfDailyPerExport domain) {
		return new WorkInfoOfDailyPerFnImport(
				domain.getEmployeeId(),
				domain.getYmd());
	}



}
