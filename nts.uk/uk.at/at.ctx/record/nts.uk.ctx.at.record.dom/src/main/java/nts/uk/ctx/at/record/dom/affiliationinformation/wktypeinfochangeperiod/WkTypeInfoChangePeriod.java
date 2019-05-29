package nts.uk.ctx.at.record.dom.affiliationinformation.wktypeinfochangeperiod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExWorkplaceHistItemImport;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpDto;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 勤務種別情報変更期間を求める
 * @author tutk
 *
 */
@Stateless
public class WkTypeInfoChangePeriod {
	@Inject
	private WorkTypeOfDailyPerforRepository workTypeOfDailyPerforRepo;
	
	public List<DatePeriod> getWkTypeInfoChangePeriod(String employeeId,DatePeriod datePeriod, List<BusinessTypeOfEmpDto> listBusinessTypeOfEmp,boolean useWorkType){
		//INPUT「勤務種別変更時に再作成」をチェックする
		if(!useWorkType) {
			return Arrays.asList(datePeriod);
		}
		//ドメインモデル「日別実績の勤務種別」を取得する
		List<WorkTypeOfDailyPerformance> listWorkTypeOfDailyPerformance =  workTypeOfDailyPerforRepo.finds(Arrays.asList(employeeId),datePeriod);
		
		Map<String, List<WorkTypeOfDailyPerformance>> mappedByWkType = listWorkTypeOfDailyPerformance.stream()
				.collect(Collectors.groupingBy(c -> c.getWorkTypeCode().v()));
		
		List<GeneralDate> lstDateAll = new ArrayList<>();
		
		for(BusinessTypeOfEmpDto businessTypeOfEmpDto : listBusinessTypeOfEmp) {
			 List<WorkTypeOfDailyPerformance> lstWpTypeDate = 
					 mappedByWkType.get(businessTypeOfEmpDto.getBusinessTypeCd());
			 if(lstWpTypeDate == null) continue;
			 List<GeneralDate> lstDateWpl = lstWpTypeDate.stream().map(x -> x.getDate()).sorted((x, y) -> x.compareTo(y)).collect(Collectors.toList());
			 DatePeriod dateTarget =  intersectPeriod(datePeriod, new DatePeriod(businessTypeOfEmpDto.getStartDate(),businessTypeOfEmpDto.getEndDate()));
			 if(dateTarget == null) continue;
			 List<GeneralDate> lstDateNeedCheck = dateTarget.datesBetween();
			 lstDateWpl.removeAll(lstDateNeedCheck);
			 lstDateAll.addAll(lstDateWpl);
		}
		
		List<GeneralDate> lstDateAllSort = lstDateAll.stream().sorted((x, y) -> x.compareTo(y)).collect(Collectors.toList());
		if(lstDateAllSort.isEmpty()) {
			return Collections.emptyList();
		}
		List<DatePeriod> lstResult = new ArrayList<>();
		GeneralDate start = lstDateAllSort.get(0);
		for(int i = 0; i < lstDateAllSort.size();i++) {
			if(i == lstDateAllSort.size()-1) {
				lstResult.add(new DatePeriod(start, lstDateAllSort.get(i))); 
				break;
			}
			if(!(lstDateAllSort.get(i).addDays(1)).equals(lstDateAllSort.get(i+1))) {
				lstResult.add(new DatePeriod(start, lstDateAllSort.get(i)));
				start = lstDateAllSort.get(i+1);
			}
		}
		
		return lstResult;
	}
	
	private DatePeriod intersectPeriod(DatePeriod dateTarget, DatePeriod dateTranfer) {
		if (dateTarget.start().beforeOrEquals(dateTranfer.end())
				&& dateTarget.end().afterOrEquals(dateTranfer.start())) {
			GeneralDate start = dateTarget.start().beforeOrEquals(dateTranfer.start()) ? dateTranfer.start()
					: dateTarget.start();
			GeneralDate end = dateTarget.end().beforeOrEquals(dateTranfer.end()) ? dateTarget.end() : dateTranfer.end();
			return new DatePeriod(start, end);
		}
		return null;
	}
}
