package nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.requestperiodchange;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.workplace.ExWorkplaceHistItemImported;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.ScheMasterInfo;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.ScheMasterInfoRepository;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpDto;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 職場・勤務種別変更期間を求める
 * 
 * @author tutk
 *
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class RequestPeriodChangeDefault implements RequestPeriodChangeService {

	@Inject
	private ScheMasterInfoRepository scheMasterInfoRepo;
	
	@Override
	public List<DatePeriod> getPeriodChange(String employeeId, DatePeriod period,
			List<ExWorkplaceHistItemImported> workplaceHistory, List<BusinessTypeOfEmpDto> worktypeHis,
			boolean isWorkplace, boolean recreate) {
		// INPUT「異動時に再作成」 = FALSE
		List<DatePeriod> result = new ArrayList<>();
		if (!recreate) {
			result.add(period);
			// INPUT「処理期間」をOUTPUTとして返す
			return result;
		}

		// ドメインモデル「勤務予定マスタ情報」を取得する
		List<ScheMasterInfo> listScheMasterInfo = scheMasterInfoRepo.getScheMasterInfoByPeriod(employeeId, period);

		// ◆INPUT．「変更比較対象」 = 職場 の場合
		if (isWorkplace) {
			Map<String, List<ScheMasterInfo>> mappedByWp = listScheMasterInfo.stream().filter(c->c.getWorkplaceId() != null)
					.collect(Collectors.groupingBy(c -> c.getWorkplaceId()));
			Map<String, List<ExWorkplaceHistItemImported>> mapDateWpl = workplaceHistory.stream().filter(c->c.getWorkplaceId() != null)
					.collect(Collectors.groupingBy(c -> c.getWorkplaceId()));
			Set<GeneralDate> lstDateAll = new HashSet<>();
			for (val itemData : mapDateWpl.entrySet()) {
				String wpl = itemData.getKey();
				List<DatePeriod> lstPeriod = itemData.getValue().stream().map(x -> x.getPeriod())
						.collect(Collectors.toList());
				// for (ExWorkplaceHistItemImport exWorkplaceHistItemImport : workplaceItems) {
				List<DatePeriod> afterMerge = new ArrayList<>();
				for (DatePeriod dateTemp : lstPeriod) {
					DatePeriod dateTarget = intersectPeriod(period, dateTemp);
					if (dateTarget != null)
						afterMerge.add(dateTarget);
				}
				if (afterMerge.isEmpty())
					continue;
				List<GeneralDate> lstDateNeedCheck = afterMerge.stream().flatMap(x -> x.datesBetween().stream())
						.collect(Collectors.toList());

				List<ScheMasterInfo> lstWplDate = mappedByWp.get(wpl);
				if (lstWplDate == null) {
					lstDateAll.addAll(lstDateNeedCheck);
					continue;
				}
				List<GeneralDate> lstDateWpl = lstWplDate.stream().map(x -> x.getGeneralDate()).sorted((x, y) -> x.compareTo(y))
						.collect(Collectors.toList());
				lstDateWpl.removeAll(lstDateNeedCheck);
				lstDateAll.addAll(lstDateWpl);
				// }
			}

			List<GeneralDate> lstDateAllSort = lstDateAll.stream().sorted((x, y) -> x.compareTo(y))
					.collect(Collectors.toList());
			if (lstDateAllSort.isEmpty()) {
				return new ArrayList<>();
			}
			List<DatePeriod> lstResult = new ArrayList<>();
			GeneralDate start = lstDateAllSort.get(0);
			for (int i = 0; i < lstDateAllSort.size(); i++) {
				if (i == lstDateAllSort.size() - 1) {
					lstResult.add(new DatePeriod(start, lstDateAllSort.get(i)));
					break;
				}
				if (!(lstDateAllSort.get(i).addDays(1)).equals(lstDateAllSort.get(i + 1))) {
					lstResult.add(new DatePeriod(start, lstDateAllSort.get(i)));
					start = lstDateAllSort.get(i + 1);
				}
			}
			return lstResult;
		}else {//◆INPUT．「変更比較対象」 =　勤務種別　の場合
			Map<String, List<ScheMasterInfo>> mappedByWkType = listScheMasterInfo.stream().filter(c->c.getBusinessTypeCd() != null)
					.collect(Collectors.groupingBy(c -> c.getBusinessTypeCd()));
			Map<String, List<BusinessTypeOfEmpDto>> mapDateWtype = worktypeHis.stream().filter(c->c.getBusinessTypeCd() != null)
					.collect(Collectors.groupingBy(c -> c.getBusinessTypeCd()));
			Set<GeneralDate> lstDateAll = new HashSet<>();
			for (val itemData : mapDateWtype.entrySet()) {
				String wtype = itemData.getKey();
				List<DatePeriod> lstPeriod = itemData.getValue().stream().map(x -> new DatePeriod(x.getStartDate(),x.getEndDate()))
						.collect(Collectors.toList());
				 List<DatePeriod> afterMerge = new ArrayList<>();
				 for(DatePeriod dateTemp : lstPeriod) {
						DatePeriod dateTarget = intersectPeriod(period, dateTemp);
						if(dateTarget != null) afterMerge.add(dateTarget);
					}
				 if(afterMerge.isEmpty()) continue;
				 List<GeneralDate> lstDateNeedCheck = afterMerge.stream().flatMap(x -> x.datesBetween().stream()).collect(Collectors.toList());
				 List<ScheMasterInfo> lstWpTypeDate = 
						 mappedByWkType.get(wtype);
				 if(lstWpTypeDate == null) {
					 lstDateAll.addAll(lstDateNeedCheck);
					 continue;
				 }
				 List<GeneralDate> lstDateWpl = lstWpTypeDate.stream().map(x -> x.getGeneralDate()).sorted((x, y) -> x.compareTo(y)).collect(Collectors.toList());
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
