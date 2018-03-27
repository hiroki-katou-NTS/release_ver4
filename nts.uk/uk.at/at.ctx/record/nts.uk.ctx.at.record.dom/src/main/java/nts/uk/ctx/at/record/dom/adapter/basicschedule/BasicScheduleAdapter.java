package nts.uk.ctx.at.record.dom.adapter.basicschedule;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface BasicScheduleAdapter {
	
	Optional<BasicScheduleSidDto> findAllBasicSchedule(String employeeId, GeneralDate baseDate);
	
	List<WorkScheduleSidImport> findAllWorkSchedule();
	
	List<WorkBreakTimeImport> findWorkBreakTime(String employeeId, GeneralDate baseDate);

}
