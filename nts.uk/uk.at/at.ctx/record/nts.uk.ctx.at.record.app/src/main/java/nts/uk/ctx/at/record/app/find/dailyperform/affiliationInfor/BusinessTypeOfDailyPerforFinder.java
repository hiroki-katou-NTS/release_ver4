package nts.uk.ctx.at.record.app.find.dailyperform.affiliationInfor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.affiliationInfor.dto.BusinessTypeOfDailyPerforDto;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/** 日別実績の所属情報 Finder */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class BusinessTypeOfDailyPerforFinder extends FinderFacade {

	@Inject
	private WorkTypeOfDailyPerforRepository repo;

	@SuppressWarnings("unchecked")
	@Override
	public BusinessTypeOfDailyPerforDto find(String employeeId, GeneralDate baseDate) {
		return BusinessTypeOfDailyPerforDto
				.getDto(this.repo.findByKey(employeeId, baseDate).orElse(null));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> BusinessTypeOfDailyPerforDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		return (List<T>) this.repo.finds(param).stream()
			.map(c -> BusinessTypeOfDailyPerforDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	public Object getDomain(String employeeId, GeneralDate baseDate) {
		return repo.findByKey(employeeId, baseDate);
	}
}
