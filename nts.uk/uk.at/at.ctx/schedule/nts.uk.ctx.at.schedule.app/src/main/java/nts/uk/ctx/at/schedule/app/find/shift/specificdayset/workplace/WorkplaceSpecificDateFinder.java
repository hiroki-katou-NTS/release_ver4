package nts.uk.ctx.at.schedule.app.find.shift.specificdayset.workplace;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.workplace.WorkplaceSpecificDateItem;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.workplace.WorkplaceSpecificDateRepository;

@Stateless
public class WorkplaceSpecificDateFinder {
	@Inject
	WorkplaceSpecificDateRepository workplaceSpecDateRepo;
	
	final String DATE_FORMAT = "yyyy/MM/dd";
	
	// WITH name
	public List<WokplaceSpecificDateDto> getWpSpecByDateWithName(String workplaceId, String wpSpecDate) {
		List<WokplaceSpecificDateDto> wokplaceSpecificDateDtos = new ArrayList<WokplaceSpecificDateDto>();
		GeneralDate startDate = GeneralDate.fromString(wpSpecDate, DATE_FORMAT);
		GeneralDate endDate = startDate.addMonths(1).addDays(-1);
		List<WorkplaceSpecificDateItem> resultList = workplaceSpecDateRepo.getWpSpecByDateWithName(workplaceId, startDate, endDate);
		for(GeneralDate loopDate=startDate; loopDate.beforeOrEquals(endDate); loopDate.addDays(1)){
			List<WorkplaceSpecificDateItem> listByDate = resultList.stream().filter(x -> x.getSpecificDate().equals(loopDate)).collect(Collectors.toList());
			if(!listByDate.isEmpty()){
				List<Integer> specificDateItemNo = new ArrayList<Integer>();
				for(WorkplaceSpecificDateItem dateRecord: listByDate){
					specificDateItemNo.add(dateRecord.getSpecificDateItemNo().v());
				}
				wokplaceSpecificDateDtos.add(new WokplaceSpecificDateDto(workplaceId, listByDate.get(0).getSpecificDate(), specificDateItemNo));
			}
		}
		return wokplaceSpecificDateDtos;
	}

}
