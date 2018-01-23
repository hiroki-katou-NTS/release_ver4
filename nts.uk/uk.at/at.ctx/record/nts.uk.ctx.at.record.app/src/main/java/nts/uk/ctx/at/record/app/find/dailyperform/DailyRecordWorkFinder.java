package nts.uk.ctx.at.record.app.find.dailyperform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.affiliationInfor.AffiliationInforOfDailyPerforFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.affiliationInfor.dto.AffiliationInforOfDailyPerforDto;
import nts.uk.ctx.at.record.app.find.dailyperform.attendanceleavinggate.AttendanceLeavingGateOfDailyFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.attendanceleavinggate.dto.AttendanceLeavingGateOfDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.calculationattribute.CalcAttrOfDailyPerformanceFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.calculationattribute.dto.CalcAttrOfDailyPerformanceDto;
import nts.uk.ctx.at.record.app.find.dailyperform.dto.AttendanceTimeDailyPerformDto;
import nts.uk.ctx.at.record.app.find.dailyperform.editstate.EditStateOfDailyPerformanceDto;
import nts.uk.ctx.at.record.app.find.dailyperform.editstate.EditStateOfDailyPerformanceFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.erroralarm.EmployeeDailyPerErrorFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.erroralarm.dto.EmployeeDailyPerErrorDto;
import nts.uk.ctx.at.record.app.find.dailyperform.goout.OutingTimeOfDailyPerformanceFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.goout.dto.OutingTimeOfDailyPerformanceDto;
import nts.uk.ctx.at.record.app.find.dailyperform.optionalitem.OptionalItemOfDailyPerformFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.optionalitem.dto.OptionalItemOfDailyPerformDto;
import nts.uk.ctx.at.record.app.find.dailyperform.resttime.BreakTimeDailyFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.resttime.dto.BreakTimeDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.shorttimework.ShortTimeOfDailyFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.shorttimework.dto.ShortTimeOfDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.specificdatetttr.SpecificDateAttrOfDailyPerforFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.specificdatetttr.dto.SpecificDateAttrOfDailyPerforDto;
import nts.uk.ctx.at.record.app.find.dailyperform.temporarytime.TemporaryTimeOfDailyPerformanceFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.temporarytime.dto.TemporaryTimeOfDailyPerformanceDto;
import nts.uk.ctx.at.record.app.find.dailyperform.workinfo.WorkInformationOfDailyFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.workinfo.dto.WorkInformationOfDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.workrecord.AttendanceTimeByWorkOfDailyFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.workrecord.TimeLeavingOfDailyPerformanceFinder;
import nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto.AttendanceTimeByWorkOfDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto.TimeLeavingOfDailyPerformanceDto;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class DailyRecordWorkFinder extends FinderFacade {

	@Inject
	private AttendanceTimeOfDailyPerformFinder attendanceTimeFinder;
	@Inject
	private AffiliationInforOfDailyPerforFinder affiliInfoFinder;
	@Inject
	private AttendanceLeavingGateOfDailyFinder attendanceLeavingGateFinder;
	@Inject
	private OutingTimeOfDailyPerformanceFinder outingTimeFinder;
	@Inject
	private OptionalItemOfDailyPerformFinder optionalItemFinder;
	// @Inject
	// private PCLogOnInforOfDailyPerformFinder pcLogOnInfoFinder;
	@Inject
	private BreakTimeDailyFinder breakItemFinder;
	@Inject
	private SpecificDateAttrOfDailyPerforFinder specificDateAttrFinder;
	@Inject
	private TemporaryTimeOfDailyPerformanceFinder temporaryTimeFinder;
	@Inject
	private WorkInformationOfDailyFinder workInfoFinder;
	@Inject
	private TimeLeavingOfDailyPerformanceFinder timeLeavingFinder;
	@Inject
	private CalcAttrOfDailyPerformanceFinder calcAttrFinder;
	@Inject
	private ShortTimeOfDailyFinder shortWorkFinder;
	@Inject
	private EditStateOfDailyPerformanceFinder editStateFinder;
	@Inject
	private EmployeeDailyPerErrorFinder errorFinder;
	@Inject
	private AttendanceTimeByWorkOfDailyFinder attendanceTimeByWorkFinder;

	@SuppressWarnings("unchecked")
	@Override
	public DailyRecordDto find(String employeeId, GeneralDate baseDate) {
		return DailyRecordDto.builder().employeeId(employeeId).workingDate(baseDate)
					.withWorkInfo(workInfoFinder.find(employeeId, baseDate))
						.withCalcAttr(calcAttrFinder.find(employeeId, baseDate))
							.withAffiliationInfo(affiliInfoFinder.find(employeeId, baseDate))
								.withErrors(errorFinder.find(employeeId, baseDate))
							.outingTime(outingTimeFinder.find(employeeId, baseDate))
						.addBreakTime(breakItemFinder.finds(employeeId, baseDate))
					.attendanceTime(attendanceTimeFinder.find(employeeId, baseDate))
						.attendanceTimeByWork(attendanceTimeByWorkFinder.find(employeeId, baseDate))
							.timeLeaving(timeLeavingFinder.find(employeeId, baseDate))
								.shortWorkTime(shortWorkFinder.find(employeeId, baseDate))
							.specificDateAttr(specificDateAttrFinder.find(employeeId, baseDate))
						.attendanceLeavingGate(attendanceLeavingGateFinder.find(employeeId, baseDate))
					.optionalItems(optionalItemFinder.find(employeeId, baseDate))
						.addEditStates(editStateFinder.finds(employeeId, baseDate))
							.temporaryTime(temporaryTimeFinder.find(employeeId, baseDate))
								.complete();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		Map<String, Map<GeneralDate, WorkInformationOfDailyDto>> workInfos = toMap(workInfoFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, CalcAttrOfDailyPerformanceDto>> calcAttrs = toMap(calcAttrFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, AffiliationInforOfDailyPerforDto>> affiliInfo = toMap(affiliInfoFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, EmployeeDailyPerErrorDto>> errors = toMap(errorFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, OutingTimeOfDailyPerformanceDto>> outings = toMap(outingTimeFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, List<BreakTimeDailyDto>>> breaks = toMapList(breakItemFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, AttendanceTimeDailyPerformDto>> attendTime = toMap(attendanceTimeFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, AttendanceTimeByWorkOfDailyDto>> attendTimeByWork = toMap(attendanceTimeByWorkFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, TimeLeavingOfDailyPerformanceDto>> leaving = toMap(timeLeavingFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, ShortTimeOfDailyDto>> shortWork = toMap(shortWorkFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, SpecificDateAttrOfDailyPerforDto>> specificDateAttr = toMap(specificDateAttrFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, AttendanceLeavingGateOfDailyDto>> attendLeavingGate = toMap(attendanceLeavingGateFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, OptionalItemOfDailyPerformDto>> optionalItems = toMap(optionalItemFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, List<EditStateOfDailyPerformanceDto>>> editStates = toMapList(editStateFinder.find(employeeId, baseDate));
		Map<String, Map<GeneralDate, TemporaryTimeOfDailyPerformanceDto>> temporaryTime = toMap(temporaryTimeFinder.find(employeeId, baseDate));
		
		return (List<T>) employeeId.stream().map(em -> {
			List<DailyRecordDto> dtoByDates = new ArrayList<>();
			GeneralDate start = baseDate.start();
			while(start.beforeOrEquals(baseDate.end())){
				DailyRecordDto current = DailyRecordDto.builder().employeeId(em).workingDate(start)
				.withWorkInfo(getValue(workInfos.get(em), start))
					.withCalcAttr(getValue(calcAttrs.get(em), start))
						.withAffiliationInfo(getValue(affiliInfo.get(em), start))
							.withErrors(getValue(errors.get(em), start))
						.outingTime(getValue(outings.get(em), start))
					.addBreakTime(getListValue(breaks.get(em), start))
				.attendanceTime(getValue(attendTime.get(em), start))
					.attendanceTimeByWork(getValue(attendTimeByWork.get(em), start))
						.timeLeaving(getValue(leaving.get(em), start))
							.shortWorkTime(getValue(shortWork.get(em), start))
						.specificDateAttr(getValue(specificDateAttr.get(em), start))
					.attendanceLeavingGate(getValue(attendLeavingGate.get(em), start))
				.optionalItems(getValue(optionalItems.get(em), start))
					.addEditStates(getListValue(editStates.get(em), start))
						.temporaryTime(getValue(temporaryTime.get(em), start))
							.complete();
				dtoByDates.add(current);
				start = start.addDays(1);
			}
			return dtoByDates;
		}).flatMap(List::stream).collect(Collectors.toList());
	}
	
	private <T extends ConvertibleAttendanceItem> T getValue(Map<GeneralDate, T> data, GeneralDate date){
		return data == null ? null : data.get(date);
	}
	
	private <T extends ConvertibleAttendanceItem> List<T> getListValue(Map<GeneralDate, List<T>> data, GeneralDate date){
		return data == null ? null : data.get(date);
	}
	
	private <T extends ConvertibleAttendanceItem> Map<String, Map<GeneralDate, T>> toMap(List<T> dtos){
		return dtos.stream().collect(Collectors.groupingBy(c -> c.employeeId(), Collectors.toMap(x -> x.workingDate(), x -> x)));
	}
	
	private <T extends ConvertibleAttendanceItem> Map<String, Map<GeneralDate, List<T>>> toMapList(List<T> dtos){
		return dtos.stream().collect(Collectors.groupingBy(c -> c.employeeId(), Collectors.groupingBy(x -> x.workingDate())));
	}
}
