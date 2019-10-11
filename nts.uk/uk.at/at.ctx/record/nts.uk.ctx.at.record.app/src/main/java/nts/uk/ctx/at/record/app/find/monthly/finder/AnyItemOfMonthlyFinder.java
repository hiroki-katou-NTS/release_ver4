package nts.uk.ctx.at.record.app.find.monthly.finder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.AnyItemOfMonthlyDto;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.app.util.attendanceitem.MonthlyFinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AnyItemOfMonthlyFinder extends MonthlyFinderFacade {
	
	@Inject
	private AnyItemOfMonthlyRepository repo;
	
	@Inject
	private OptionalItemRepository optionalMasterRepo;

	@Override
	@SuppressWarnings("unchecked")
	public AnyItemOfMonthlyDto find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		List<AnyItemOfMonthly> domains = this.repo.findByMonthlyAndClosure(employeeId, yearMonth, closureId, closureDate);
		List<Integer> itemIds = domains.stream().map(i -> i.getAnyItemId()).distinct().collect(Collectors.toList());
		if(!itemIds.isEmpty()){
			Map<Integer, OptionalItem> optionalMaster = optionalMasterRepo
					.findByListNos(AppContexts.user().companyId(), itemIds).stream()
					.collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c));
			return AnyItemOfMonthlyDto.from(domains, optionalMaster);
		}
		return AnyItemOfMonthlyDto.from(domains);
	}

	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(Collection<String> employeeId, DatePeriod range) {
		return find(employeeId, ConvertHelper.yearMonthsBetween(range));
	}

	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(Collection<String> employeeId, YearMonth yearMonth) {
		return find(employeeId, Arrays.asList(yearMonth));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Collection<String> employeeId, Collection<YearMonth> yearMonth) {
		Map<Integer, OptionalItemAtr> optionalMaster = optionalMasterRepo.findAll(AppContexts.user().companyId())
				.stream().collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c.getOptionalItemAtr()));
		
		return (List<T>) repo.findBySidsAndMonthsV2(new ArrayList<>(employeeId), new ArrayList<>(yearMonth))
				.stream().collect(Collectors.groupingBy(c -> StringUtils.join(c.getEmployeeId(), c.getYearMonth()), 
						Collectors.collectingAndThen(Collectors.toList(), 
								list -> AnyItemOfMonthlyDto.fromWith(list, optionalMaster))))
				.entrySet().stream().map(dto -> dto.getValue()).collect(Collectors.toList());
	}
}
