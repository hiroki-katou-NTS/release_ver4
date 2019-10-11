package nts.uk.ctx.at.record.app.find.dailyperform.workinfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.workinfo.dto.WorkInformationOfDailyDto;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WorkInformationOfDailyFinder extends FinderFacade {

	@Inject
	private WorkInformationRepository workInfoRepo;

	@Override
	@SuppressWarnings("unchecked")
	public WorkInformationOfDailyDto find(String employeeId, GeneralDate baseDate) {
		return WorkInformationOfDailyDto.getDto(this.workInfoRepo.find(employeeId, baseDate).orElse(null));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.workInfoRepo.findByPeriodOrderByYmdAndEmps(employeeId, baseDate).stream()
				.map(c -> WorkInformationOfDailyDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		return (List<T>) this.workInfoRepo.finds(param).stream()
			.map(c -> WorkInformationOfDailyDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	public Object getDomain(String employeeId, GeneralDate baseDate) {
		return this.workInfoRepo.find(employeeId, baseDate);
	}
}
