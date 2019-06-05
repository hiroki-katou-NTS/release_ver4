package nts.uk.ctx.at.record.dom.affiliationinformation.wkplaceinfochangeperiod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExWorkPlaceHistoryImport;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExWorkplaceHistItemImport;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 職場情報変更期間を求める
 * 
 * @author tutk
 *
 */
@Stateless
public class WkplaceInfoChangePeriod {
	@Inject
	private AffiliationInforOfDailyPerforRepository affInforOfDailyPerforRepo;

	public List<DatePeriod> getWkplaceInfoChangePeriod(String employeeId,DatePeriod datePeriod, List<ExWorkplaceHistItemImport> workplaceItems,boolean useWorkplace){
		//INPUT「異動時に再作成」をチェックする
		if(!useWorkplace) {
			return Arrays.asList(datePeriod);
		}
		//ドメインモデル「日別実績の所属情報」を取得する
		List<AffiliationInforOfDailyPerfor> listAffiliationInforOfDailyPerfor = affInforOfDailyPerforRepo.finds(Arrays.asList(employeeId) , datePeriod);
		Map<String, List<AffiliationInforOfDailyPerfor>> mappedByWp = listAffiliationInforOfDailyPerfor.stream()
				.collect(Collectors.groupingBy(c -> c.getWplID()));
		
		List<GeneralDate> lstDateAll = new ArrayList<>();
		for(ExWorkplaceHistItemImport exWorkplaceHistItemImport : workplaceItems) {
			 List<AffiliationInforOfDailyPerfor> lstWplDate = 
					 mappedByWp.get(exWorkplaceHistItemImport.getWorkplaceId());
			 if(lstWplDate == null) continue;
			 List<GeneralDate> lstDateWpl = lstWplDate.stream().map(x -> x.getYmd()).sorted((x, y) -> x.compareTo(y)).collect(Collectors.toList());
			 DatePeriod dateTarget =  intersectPeriod(datePeriod, exWorkplaceHistItemImport.getPeriod());
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
		for(int i = 0; i <lstDateAllSort.size();i++) {
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
