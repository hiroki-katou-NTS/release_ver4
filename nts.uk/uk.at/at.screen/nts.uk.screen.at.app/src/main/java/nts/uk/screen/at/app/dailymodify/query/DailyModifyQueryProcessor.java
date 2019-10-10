package nts.uk.screen.at.app.dailymodify.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordWorkFinder;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;

/** 日別修正QueryProcessor */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DailyModifyQueryProcessor {

	@Inject
	private DailyRecordWorkFinder fullFinder;

	public DailyModifyResult initScreen(DailyModifyQuery query, List<Integer> itemIds) {
		DailyModifyResult result = new DailyModifyResult();
		DailyRecordDto itemDtos = this.fullFinder.find(query.getEmployeeId(), query.getBaseDate());
		result.setItems(AttendanceItemUtil.toItemValues(itemDtos, itemIds));
		result.setEmployeeId(query.getEmployeeId());
		result.setDate(query.getBaseDate());
		return result;
	}

	public List<DailyModifyResult> initScreen(DailyMultiQuery query, List<Integer> itemIds) {
		if(query.getEmployeeIds() == null || query.getEmployeeIds().isEmpty() || query.getPeriod() == null){
			return new ArrayList<>();
		}
		
		return AttendanceItemUtil.toItemValues(this.fullFinder.find(query.getEmployeeIds(), query.getPeriod()), itemIds)
			.entrySet().stream().map(c -> DailyModifyResult.builder().items(c.getValue())
						.workingDate(c.getKey().workingDate()).employeeId(c.getKey().employeeId()).completed())
				.collect(Collectors.toList());
//		return this.fullFinder.find(query.getEmployeeIds(), query.getPeriod()).stream()
//				.map(c -> DailyModifyResult.builder().items(AttendanceItemUtil.toItemValues(c, itemIds))
//						.workingDate(c.workingDate()).employeeId(c.employeeId()).completed())
//				.collect(Collectors.toList());
	}
	
	public Pair<List<DailyModifyResult>, List<DailyRecordDto>>  initScreen(DailyMultiQuery query) {
		if(query.getEmployeeIds() == null || query.getEmployeeIds().isEmpty() || query.getPeriod() == null){
			return Pair.of(Collections.emptyList(), Collections.emptyList());
		}
		List<DailyRecordDto> dtoOlds = this.fullFinder.find(query.getEmployeeIds(), query.getPeriod());
		val resultValues = AttendanceItemUtil.toItemValues(dtoOlds)
		.entrySet().stream().map(c -> DailyModifyResult.builder().items(c.getValue())
					.workingDate(c.getKey().workingDate()).employeeId(c.getKey().employeeId()).completed())
			.collect(Collectors.toList());
//		val resultValues = dtoOlds.stream()
//				.map(c -> DailyModifyResult.builder().items(AttendanceItemUtil.toItemValues(c))
//						.workingDate(c.workingDate()).employeeId(c.employeeId()).completed())
//				.collect(Collectors.toList());
		return Pair.of(resultValues, dtoOlds);
	}
	
	public Pair<List<DailyModifyResult>, List<DailyRecordDto>> getRow(Map<String, List<GeneralDate>> mapDate) {
		if(mapDate.isEmpty()){
			return Pair.of(Collections.emptyList(), Collections.emptyList());
		}
		List<DailyRecordDto> dtoOlds = this.fullFinder.find(mapDate);
		val resultValues = AttendanceItemUtil.toItemValues(dtoOlds)
				.entrySet().stream().map(c -> DailyModifyResult.builder().items(c.getValue())
						.workingDate(c.getKey().workingDate()).employeeId(c.getKey().employeeId()).completed())
				.collect(Collectors.toList());
//		val resultValues = dtoOlds.stream()
//				.map(c -> DailyModifyResult.builder().items(AttendanceItemUtil.toItemValues(c))
//						.workingDate(c.workingDate()).employeeId(c.employeeId()).completed())
//				.collect(Collectors.toList());
		return Pair.of(resultValues, dtoOlds);
	}
	
}
