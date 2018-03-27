package nts.uk.screen.at.app.dailyperformance.correction.checkdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.service.workrecord.erroralarm.recordcheck.ErAlWorkRecordCheckService;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ValidatorDataDaily {
	
	@Inject
	private ErAlWorkRecordCheckService erAlWorkRecordCheckService;

	private static final Integer[] CHILD_CARE = { 759, 760, 761, 762 };
	private static final Integer[] CARE = { 763, 764, 765, 766 };

	// 育児と介護の時刻が両方入力されていないかチェックする
	public List<DPItemValue> checkCareItemDuplicate(List<DPItemValue> items) {
		List<DPItemValue> childCares = hasChildCare(items);
		List<DPItemValue> cares = hasCare(items);
		if (!childCares.isEmpty() && !cares.isEmpty()) {
			// throw new BusinessException("Msg_996");
			childCares.addAll(cares);
			return childCares;
		} else
			return Collections.emptyList();

	}

	private List<DPItemValue> hasChildCare(List<DPItemValue> items) {
		List<DPItemValue> itemChild = items.stream()
				.filter(x -> x.getValue() != null && (x.getItemId() == CHILD_CARE[0] || x.getItemId() == CHILD_CARE[1]
						|| x.getItemId() == CHILD_CARE[2] || x.getItemId() == CHILD_CARE[3]))
				.collect(Collectors.toList());
		return itemChild.isEmpty() ? Collections.emptyList() : itemChild;
	}

	private List<DPItemValue> hasCare(List<DPItemValue> items) {
		List<DPItemValue> itemCare = items.stream().filter(x -> x.getValue() != null && (x.getItemId() == CARE[0]
				|| x.getItemId() == CARE[1] || x.getItemId() == CARE[2] || x.getItemId() == CARE[3]))
				.collect(Collectors.toList());
		return itemCare.isEmpty() ? Collections.emptyList() : itemCare;
	}

	public List<DPItemValue> checkCareInputData(List<DPItemValue> items) {
		List<DPItemValue> childCares = hasChildCare(items);
		List<DPItemValue> cares = hasCare(items);
		List<DPItemValue> result = new ArrayList<>();
		if (!childCares.isEmpty()) {
			Map<Integer, DPItemValue> childMap= childCares.stream().collect(Collectors.toMap(DPItemValue::getItemId, x -> x));
		    boolean childCare759 = childMap.containsKey(CHILD_CARE[0]);
		    boolean childCare760 = childMap.containsKey(CHILD_CARE[1]);
		    boolean childCare761 = childMap.containsKey(CHILD_CARE[2]);
		    boolean childCare762 = childMap.containsKey(CHILD_CARE[3]);
		    if(!(childCare759 && childCare760)){
		    	if(childCare759){
		    		result.add(childMap.get(CHILD_CARE[0]));
		    	}else if(childCare760){
		    		result.add(childMap.get(CHILD_CARE[1]));
		    	}
		    }
			if (!(childCare761 && childCare762)) {
				if(childCare761){
		    		result.add(childMap.get(CHILD_CARE[2]));
		    	}else if(childCare762){
		    		result.add(childMap.get(CHILD_CARE[3]));
		    	}
			}
			return result;
		} else if (!cares.isEmpty()) {
			Map<Integer, DPItemValue> caresMap= cares.stream().collect(Collectors.toMap(DPItemValue::getItemId, x -> x));
		    boolean care763 = caresMap.containsKey(CARE[0]);
		    boolean care764 = caresMap.containsKey(CARE[1]);
		    boolean care765 = caresMap.containsKey(CARE[2]);
		    boolean care766 = caresMap.containsKey(CARE[3]);
		    if(!(care763 && care764)){
		    	if(care763){
		    		result.add(caresMap.get(CARE[0]));
		    	}else if(care764){
		    		result.add(caresMap.get(CARE[1]));
		    	}
		    }
			if (!(care765 && care766)) {
				if(care765){
		    		result.add(caresMap.get(CARE[2]));
		    	}else if(care766){
		    		result.add(caresMap.get(CARE[3]));
		    	}
			}
			return result;
		}
		return Collections.emptyList();
	}
	
	public List<DPItemValue> checkContinuousHolidays(String employeeId, DateRange date) {
		Map<GeneralDate, Integer> result = erAlWorkRecordCheckService.checkContinuousHolidays(employeeId,
				new DatePeriod(date.getStartDate(), date.getEndDate()));
		if (!result.isEmpty()) {
			return result.entrySet().stream()
					.map(x -> new DPItemValue("勤務種類", employeeId, x.getKey(), 0, String.valueOf(x.getValue())))
					.collect(Collectors.toList());
		} else {
			List<DPItemValue> list = new ArrayList<>();
			list.add( new DPItemValue("勤務種類", employeeId, date.getStartDate(), 0, "5"));
			return Collections.emptyList();
		}
	}
	
}
