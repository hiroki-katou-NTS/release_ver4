package nts.uk.ctx.at.record.app.find.dailyperform.optionalitem;

import java.util.List;
import java.util.Map;
//import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.optionalitem.dto.OptionalItemOfDailyPerformDto;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
//import nts.uk.ctx.at.record.dom.optitem.PerformanceAtr;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/** 日別実績の任意項目 Finder */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class OptionalItemOfDailyPerformFinder extends FinderFacade {

	@Inject
	private OptionalItemRepository optionalMasterRepo;

	@Inject
	private AnyItemValueOfDailyRepo repo;

	@SuppressWarnings("unchecked")
	@Override
	public OptionalItemOfDailyPerformDto find(String employeeId, GeneralDate baseDate) {
		AnyItemValueOfDaily domain = this.repo.find(employeeId, baseDate).orElse(null);
		if (domain != null) {
			List<Integer> itemIds = domain.getItems().stream().map(i -> i.getItemNo().v()).distinct().collect(Collectors.toList());
			if(!itemIds.isEmpty()){
				Map<Integer, OptionalItem> optionalMaster = optionalMasterRepo
						.findByListNos(AppContexts.user().companyId(), itemIds).stream()
						.collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c));
				return OptionalItemOfDailyPerformDto.getDto(domain, optionalMaster);
			}
		}
		return OptionalItemOfDailyPerformDto.getDto(domain);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		List<AnyItemValueOfDaily> domains = this.repo.finds(employeeId, baseDate);
		List<Integer> itemIds = domains.stream()
										.map(d -> d.getItems().stream().map(i -> i.getItemNo().v()).collect(Collectors.toList()))
										.flatMap(List::stream).distinct()
										.collect(Collectors.toList());
		if (!itemIds.isEmpty()) {
			Map<Integer, OptionalItem> masters = optionalMasterRepo.findByListNos(AppContexts.user().companyId(), itemIds).stream()
					.collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c));
			return (List<T>) domains.stream().map(c -> OptionalItemOfDailyPerformDto.getDto(c, masters))
					.collect(Collectors.toList());
		}
		return (List<T>) domains.stream().map(c -> OptionalItemOfDailyPerformDto.getDto(c))
				.collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		Map<Integer, OptionalItemAtr> optionalMaster = optionalMasterRepo
				.findAll(AppContexts.user().companyId()).stream()
				.collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c.getOptionalItemAtr()));
		return (List<T>) this.repo.finds(param).stream()
			.map(c -> OptionalItemOfDailyPerformDto.getDtoWith(c, optionalMaster)).collect(Collectors.toList());
	}

	@Override
	public Object getDomain(String employeeId, GeneralDate baseDate) {
		return repo.find(employeeId, baseDate);
	}

}
