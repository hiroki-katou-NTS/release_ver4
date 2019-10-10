package nts.uk.ctx.at.record.app.find.dailyperform.affiliationInfor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.affiliationInfor.dto.AffiliationInforOfDailyPerforDto;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/** 日別実績の所属情報 Finder */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AffiliationInforOfDailyPerforFinder extends FinderFacade {

	@Inject
	private AffiliationInforOfDailyPerforRepository affiliationInfoRepo;

	@SuppressWarnings("unchecked")
	@Override
	public AffiliationInforOfDailyPerforDto find(String employeeId, GeneralDate baseDate) {
		return AffiliationInforOfDailyPerforDto
				.getDto(this.affiliationInfoRepo.findByKey(employeeId, baseDate).orElse(null));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.affiliationInfoRepo.finds(employeeId, baseDate).stream()
				.map(c -> AffiliationInforOfDailyPerforDto.getDto(c)).collect(Collectors.toList());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		return (List<T>) this.affiliationInfoRepo.finds(param).stream()
				.map(c -> AffiliationInforOfDailyPerforDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	public Object getDomain(String employeeId, GeneralDate baseDate) {
		return affiliationInfoRepo.findByKey(employeeId, baseDate);
	}
}
