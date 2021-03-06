package nts.uk.ctx.at.record.dom.affiliationinformation.wktypeinfochangeperiod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.shared.dom.employeeworkway.businesstype.employee.BusinessTypeOfEmployeeHis;
/**
 * 勤務種別情報変更期間を求める
 * @author tutk
 *
 */
@Stateless
public class WkTypeInfoChangePeriod {
	@Inject
	private AffiliationInforOfDailyPerforRepository affDailyPerforRepo;
	
	public List<DatePeriod> getWkTypeInfoChangePeriod(String employeeId,DatePeriod datePeriod, 
			List<BusinessTypeOfEmployeeHis> listBusinessTypeOfEmp, boolean useWorkType) {
		//INPUT「勤務種別変更時に再作成」をチェックする
		List<DatePeriod> result = new ArrayList<>();
		if(!useWorkType) {
			result.add(datePeriod);
			return result;
		}
		//ドメインモデル「日別実績の勤務種別」を取得する
		List<AffiliationInforOfDailyPerfor> affDailies =  affDailyPerforRepo.finds(Arrays.asList(employeeId),datePeriod);
		
		Map<String, List<AffiliationInforOfDailyPerfor>> mappedByWkType = affDailies.stream()
				.filter(c -> c.getAffiliationInfor().getBusinessTypeCode().isPresent())
				.collect(Collectors.groupingBy(c -> c.getAffiliationInfor().getBusinessTypeCode().get().v()));
		Map<String, List<BusinessTypeOfEmployeeHis>> mapDateWtype = listBusinessTypeOfEmp.stream()
				.collect(Collectors.groupingBy(c -> c.getEmployee().getBusinessTypeCode().v()));
		
		Set<GeneralDate> lstDateAll = new HashSet<>();
		for (val itemData : mapDateWtype.entrySet()) {
			String wtype = itemData.getKey();
			List<DatePeriod> lstPeriod = itemData.getValue().stream().map(x -> x.getHistory().span())
					.collect(Collectors.toList());
			 List<DatePeriod> afterMerge = new ArrayList<>();
			 for(DatePeriod dateTemp : lstPeriod) {
					DatePeriod dateTarget = intersectPeriod(datePeriod, dateTemp);
					if(dateTarget != null) afterMerge.add(dateTarget);
				}
			 if(afterMerge.isEmpty()) continue;
			 List<GeneralDate> lstDateNeedCheck = afterMerge.stream().flatMap(x -> x.datesBetween().stream()).collect(Collectors.toList());
			 List<AffiliationInforOfDailyPerfor> lstWpTypeDate =  mappedByWkType.get(wtype);
			 if(lstWpTypeDate == null) {
				 lstDateAll.addAll(lstDateNeedCheck);
				 continue;
			 }
			 List<GeneralDate> lstDateWpl = lstWpTypeDate.stream().map(x -> x.getYmd()).sorted((x, y) -> x.compareTo(y)).collect(Collectors.toList());
			 lstDateWpl.removeAll(lstDateNeedCheck);
			 lstDateAll.addAll(lstDateWpl);
			 
		}
		List<GeneralDate> lstDateAllSort = lstDateAll.stream().sorted((x, y) -> x.compareTo(y)).collect(Collectors.toList());
		if(lstDateAllSort.isEmpty()) {
			return new ArrayList<>();
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
