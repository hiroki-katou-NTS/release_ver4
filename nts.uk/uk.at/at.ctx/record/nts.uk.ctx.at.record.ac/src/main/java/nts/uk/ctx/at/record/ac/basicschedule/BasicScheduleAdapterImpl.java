package nts.uk.ctx.at.record.ac.basicschedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleAdapter;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleSidDto;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.WorkBreakTimeImport;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.WorkScheduleSidImport;
import nts.uk.ctx.at.schedule.pub.schedule.basicschedule.ScBasicScheduleExport;
import nts.uk.ctx.at.schedule.pub.schedule.basicschedule.ScBasicSchedulePub;
import nts.uk.ctx.at.schedule.pub.schedule.basicschedule.ScWorkBreakTimeExport;

@Stateless
public class BasicScheduleAdapterImpl implements BasicScheduleAdapter {

	@Inject
	private ScBasicSchedulePub scBasicSchedulePub;

	@Override
	public Optional<BasicScheduleSidDto> findAllBasicSchedule(String employeeId, GeneralDate baseDate) {
		Optional<ScBasicScheduleExport> basicScheduleExport = this.scBasicSchedulePub.findById(employeeId, baseDate);
		if(!basicScheduleExport.isPresent()) {
			return Optional.empty();
		}
		List<WorkScheduleSidImport> workScheduleSidImports = new ArrayList<>();
		basicScheduleExport.get().getWorkScheduleTimeZones().forEach(item -> {
			WorkScheduleSidImport workScheduleSidImport = new WorkScheduleSidImport(item.getScheduleCnt(), item.getScheduleStartClock(), item.getScheduleEndClock(), item.getBounceAtr());
			workScheduleSidImports.add(workScheduleSidImport);
		});	
		
		BasicScheduleSidDto basicScheduleSidDto = new BasicScheduleSidDto(basicScheduleExport.get().getEmployeeId(),
				basicScheduleExport.get().getDate(),
				basicScheduleExport.get().getWorkTypeCode(),
				basicScheduleExport.get().getWorkTimeCode(),
				workScheduleSidImports);
		return Optional.of(basicScheduleSidDto);
	}

	@Override
	public List<WorkScheduleSidImport> findAllWorkSchedule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkBreakTimeImport> findWorkBreakTime(String employeeId, GeneralDate baseDate) {
		List<ScWorkBreakTimeExport> scWorkBreakTimeExports = this.scBasicSchedulePub.findWorkBreakTime(employeeId, baseDate);
		
		if (scWorkBreakTimeExports.isEmpty() || scWorkBreakTimeExports == null) {
			return null;
		}
		List<WorkBreakTimeImport> workBreakTimeImportList = new ArrayList<>();
		
		scWorkBreakTimeExports.stream().forEach(f -> {
			WorkBreakTimeImport workBreakTimeImport = new WorkBreakTimeImport(f.getScheduleBreakCnt(), f.getScheduledStartClock(), f.getScheduledEndClock());
			workBreakTimeImportList.add(workBreakTimeImport);
		});
				
		return workBreakTimeImportList;
	}

}
