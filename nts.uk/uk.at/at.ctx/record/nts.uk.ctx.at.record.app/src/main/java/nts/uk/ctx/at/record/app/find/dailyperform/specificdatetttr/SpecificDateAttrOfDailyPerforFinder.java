package nts.uk.ctx.at.record.app.find.dailyperform.specificdatetttr;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.specificdatetttr.dto.SpecificDateAttrOfDailyPerforDto;
import nts.uk.ctx.at.record.dom.raisesalarytime.repo.SpecificDateAttrOfDailyPerforRepo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/** 日別実績の特定日区分 Finder */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SpecificDateAttrOfDailyPerforFinder extends FinderFacade {

	@Inject
	private SpecificDateAttrOfDailyPerforRepo repo;

	@SuppressWarnings("unchecked")
	@Override
	public SpecificDateAttrOfDailyPerforDto find(String employeeId, GeneralDate baseDate) {
		return SpecificDateAttrOfDailyPerforDto.getDto(this.repo.find(employeeId, baseDate).orElse(null));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> SpecificDateAttrOfDailyPerforDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		return (List<T>) this.repo.finds(param).stream()
			.map(c -> SpecificDateAttrOfDailyPerforDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	public Object getDomain(String employeeId, GeneralDate baseDate) {
		return repo.find(employeeId, baseDate);
	}

}
