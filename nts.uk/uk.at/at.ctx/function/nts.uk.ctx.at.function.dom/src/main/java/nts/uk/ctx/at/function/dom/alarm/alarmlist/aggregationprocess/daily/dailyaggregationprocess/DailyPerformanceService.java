package nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.daily.dailyaggregationprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.DailyAttendanceItemAdapter;
import nts.uk.ctx.at.function.dom.adapter.ErAlApplicationAdapter;
import nts.uk.ctx.at.function.dom.adapter.ErAlApplicationAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.ErrorAlarmWorkRecordAdapter;
import nts.uk.ctx.at.function.dom.adapter.ErrorAlarmWorkRecordAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.WorkRecordExtraConAdapter;
import nts.uk.ctx.at.function.dom.adapter.application.ApplicationAdapter;
import nts.uk.ctx.at.function.dom.adapter.dailyattendanceitem.AttendanceItemValueImport;
import nts.uk.ctx.at.function.dom.adapter.dailyattendanceitem.AttendanceResultImport;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.MessageWRExtraConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.worklocation.RecordWorkInfoFunAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.EmployeeDailyPerErrorAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.EmployeeDailyPerErrorImport;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.PeriodByAlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.daily.DailyAlarmCondition;
import nts.uk.ctx.at.function.dom.dailyattendanceitem.DailyAttendanceItem;
import nts.uk.ctx.at.function.dom.dailyattendanceitem.repository.DailyAttendanceItemNameDomainService;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class DailyPerformanceService {
	// 社員の日別実績エラー一覧
	@Inject
	private EmployeeDailyPerErrorAdapter employeeDailyAdapter;

	// 勤務実績のエラーアラーム
	@Inject
	private ErrorAlarmWorkRecordAdapter errorAlarmWorkRecordAdapter;

	// 勤務実績のエラーアラームチェック
	@Inject
	private WorkRecordExtraConAdapter workRecordExtraConAdapter;

	// Get attendance item name 勤怠項目名
	@Inject
	private DailyAttendanceItemNameDomainService dailyAttendanceItemNameService;

	// エラー発生時に呼び出す申請一覧
	@Inject
	private ErAlApplicationAdapter erAlApplicationAdapter;

	// 書いた申請
	@Inject
	private ApplicationAdapter applicationAdapter;
	
	//Get attendance  value
	@Inject
	private DailyAttendanceItemAdapter attendanceItemAdapter; 
	
	// Get work type code
	@Inject
	private RecordWorkInfoFunAdapter recordWorkInfoFunAdapter;
	
	// Get work type name
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	
	

	public List<ValueExtractAlarm> aggregationProcess(DailyAlarmCondition dailyAlarmCondition, PeriodByAlarmCategory period, EmployeeSearchDto employee, String companyID) {
		List<ValueExtractAlarm> valueExtractAlarmList = new ArrayList<>();

		List<String> listErrorAlarmCode = dailyAlarmCondition.getErrorAlarmCode();
		if (listErrorAlarmCode == null || listErrorAlarmCode.isEmpty())
			return new ArrayList<>();

		// 勤務実績のエラーアラーム
		List<ErrorAlarmWorkRecordAdapterDto> errorAlarmWorkRecord = errorAlarmWorkRecordAdapter.getListErAlByListCode(companyID, listErrorAlarmCode);
		Map<String, ErrorAlarmWorkRecordAdapterDto> errorAlarmMap = errorAlarmWorkRecord.stream().collect(Collectors.toMap(ErrorAlarmWorkRecordAdapterDto::getCode, x -> x));

		// 社員の日別実績エラー一覧
		List<EmployeeDailyPerErrorImport> employeeDailyList = employeeDailyAdapter.getByErrorAlarm(employee.getId(), new DatePeriod(period.getStartDate(), period.getEndDate()), listErrorAlarmCode);

		if (dailyAlarmCondition.isAddApplication()) {
			for (EmployeeDailyPerErrorImport eDaily : employeeDailyList) {
				ErrorAlarmWorkRecordAdapterDto errorAlarm = errorAlarmMap.get(eDaily.getErrorAlarmWorkRecordCode());
				if (errorAlarm == null) {
					employeeDailyList.removeIf(e -> e.getErrorAlarmWorkRecordCode().equals(eDaily.getErrorAlarmWorkRecordCode()));
					listErrorAlarmCode.removeIf(x -> x.equals(eDaily.getErrorAlarmWorkRecordCode()));
					continue;
				}
				if (errorAlarm.getUseAtr() > 0) {
					Optional<ErAlApplicationAdapterDto> erAlApplicationOpt = erAlApplicationAdapter.getAllErAlAppByEralCode(companyID, eDaily.getErrorAlarmWorkRecordCode());
					if (!erAlApplicationOpt.isPresent())
						throw new RuntimeException("Domain エラー発生時に呼び出す申請一覧 not found!");

					List<Integer> listAppType = erAlApplicationOpt.get().getAppType();
					List<Integer> listAppTypeWrited = applicationAdapter.getApplicationBySID(Arrays.asList(employee.getId()), eDaily.getDate(), eDaily.getDate()).stream().map(x -> x.getAppType()).collect(Collectors.toList());

					if (intersectTwoListAppType(listAppType, listAppTypeWrited)) {
						employeeDailyList.remove(eDaily);
					}
				}

			}

		}

		// Remove error code from errorAlarmWorkRecord if error code not in
		// employeeDailyList
		List<String> errorCodeList = employeeDailyList.stream().map(x -> x.getErrorAlarmWorkRecordCode()).collect(Collectors.toList());
		errorAlarmWorkRecord.removeIf(x -> !errorCodeList.contains(x.getCode()));

		// 勤務実績のエラーアラームチェック
		List<String> errorAlarmCheckIDs = errorAlarmWorkRecord.stream().map(x -> x.getErrorAlarmCheckID()).collect(Collectors.toList());
		List<MessageWRExtraConAdapterDto> messageList = workRecordExtraConAdapter.getMessageWRExtraConByListID(errorAlarmCheckIDs);

		Map<String, MessageWRExtraConAdapterDto> errAlarmCheckIDToMessage = messageList.stream().collect(Collectors.toMap(MessageWRExtraConAdapterDto::getErrorAlarmCheckID, x -> x));

		for (EmployeeDailyPerErrorImport eDaily : employeeDailyList) {
			
			AlarmContentMessage alarmContentMessage = this.calculateAlarmContentMessage(eDaily, companyID, errorAlarmMap);
						
			ValueExtractAlarm data = new ValueExtractAlarm(employee.getWorkplaceId(), employee.getId(), eDaily.getDate().toString(), TextResource.localize("KAL010_1"),
					alarmContentMessage.getAlarmItem(), alarmContentMessage.getAlarmContent(),
					errAlarmCheckIDToMessage.get(errorAlarmMap.get(eDaily.getErrorAlarmWorkRecordCode()).getErrorAlarmCheckID()).getDisplayMessage());

			valueExtractAlarmList.add(data);
		}

		return valueExtractAlarmList;

	}
	
	

	private boolean intersectTwoListAppType(List<Integer> listAppType1, List<Integer> listAppType2) {
		List<Integer> intersect = listAppType1.stream().filter(listAppType2::contains).collect(Collectors.toList());
		return !intersect.isEmpty();
	}
	
	
	
	private AlarmContentMessage calculateAlarmContentMessage(EmployeeDailyPerErrorImport eDaily, String companyID, Map<String, ErrorAlarmWorkRecordAdapterDto> errorAlarmMap) {
		// Attendance name
		Map<Integer, DailyAttendanceItem> attendanceNameMap = dailyAttendanceItemNameService.getNameOfDailyAttendanceItem(eDaily.getAttendanceItemList()).stream()
				.collect(Collectors.toMap(DailyAttendanceItem::getAttendanceItemId, x -> x));
		// Attendance value
		AttendanceResultImport attendanceResult = attendanceItemAdapter.getValueOf(eDaily.getEmployeeID(), eDaily.getDate(), eDaily.getAttendanceItemList());
		List<AttendanceItemValueImport> attendanceValue = attendanceResult.getAttendanceItems() == null ? new ArrayList<AttendanceItemValueImport>() : attendanceResult.getAttendanceItems();
		Map<Integer, AttendanceItemValueImport> mapAttendance = attendanceValue.stream().collect(Collectors.toMap(AttendanceItemValueImport::getItemId, x -> x));

		// アラーム値メッセージ
		String alarmItem = "";
		String alarmContent = "";

		if (eDaily.getErrorAlarmWorkRecordCode().equals("S001")) {

			alarmItem = TextResource.localize("KAL010_2");

			eDaily.getAttendanceItemList().sort((a, b) -> a.compareTo(b));
			for (Integer id : eDaily.getAttendanceItemList()) {
				alarmContent += "/" + attendanceNameMap.get(id).getAttendanceItemName();
			}
			if (alarmContent.length() > 0)
				alarmContent = alarmContent.substring(1, alarmContent.length());

		} else if (eDaily.getErrorAlarmWorkRecordCode().equals("S004")) {

			alarmItem = TextResource.localize("KAL010_3");
			eDaily.getAttendanceItemList().sort((a, b) -> a.compareTo(b));
			int i = 0;
			for (Integer id : eDaily.getAttendanceItemList()) {
				i += 1;
				if (i % 2 != 0) {
					alarmContent += "/" + attendanceNameMap.get(id).getAttendanceItemName() + ": " + formatValueAttendance(mapAttendance.get(id).getValue());
				} else {
					alarmContent += "、　" + attendanceNameMap.get(id).getAttendanceItemName() + ": " + formatValueAttendance(mapAttendance.get(id).getValue());
				}
			}
			if (alarmContent.length() > 0)
				alarmContent = alarmContent.substring(1, alarmContent.length());

		} else if (eDaily.getErrorAlarmWorkRecordCode().equals("S005")) {

			alarmItem = TextResource.localize("KAL010_4");

			Optional<String> optWorkTypeCode = recordWorkInfoFunAdapter.getWorkTypeCode(eDaily.getEmployeeID(), eDaily.getDate());
			if (!optWorkTypeCode.isPresent())
				throw new RuntimeException("Not found work type code!");
			Optional<WorkType> optWorkType = workTypeRepository.findByPK(companyID, optWorkTypeCode.get());
			if (!optWorkType.isPresent())
				throw new RuntimeException(" WorkType domain not found!");

			alarmContent = TextResource.localize("KAL010_5", optWorkType.get().getName().v(), formatValueAttendance(mapAttendance.get(16).getValue()),
					formatValueAttendance(mapAttendance.get(19).getValue()));

		} else if (eDaily.getErrorAlarmWorkRecordCode().equals("S003")) {

			alarmItem = TextResource.localize("KAL010_12");
			eDaily.getAttendanceItemList().sort((a, b) -> a.compareTo(b));

			int i = 0;
			for (Integer id : eDaily.getAttendanceItemList()) {
				i += 1;
				if (i % 2 != 0) {
					alarmContent += "/" + attendanceNameMap.get(id).getAttendanceItemName();
				} else {
					alarmContent += "、　" + attendanceNameMap.get(id).getAttendanceItemName();
				}
			}
			if (alarmContent.length() > 0)
				alarmContent = TextResource.localize("KAL010_2") + " " + alarmContent.substring(1, alarmContent.length());

		} else if (eDaily.getErrorAlarmWorkRecordCode().equals("S007")) {
			alarmItem = TextResource.localize("KAL010_68");
			Integer id = eDaily.getAttendanceItemList().get(0);
			alarmItem = TextResource.localize("KAL010_69", attendanceNameMap.get(id).getAttendanceItemName(), formatValueAttendance(mapAttendance.get(id).getValue()));

		} else if (eDaily.getErrorAlarmWorkRecordCode().equals("S008")) {
			alarmItem = TextResource.localize("KAL010_70");
			Integer id = eDaily.getAttendanceItemList().get(0);
			alarmItem = TextResource.localize("KAL010_71", attendanceNameMap.get(id).getAttendanceItemName(), formatValueAttendance(mapAttendance.get(id).getValue()));

		} else if (eDaily.getErrorAlarmWorkRecordCode().equals("S006")) {
			alarmItem = TextResource.localize("KAL010_17");
			alarmContent = TextResource.localize("KAL010_18");
		} else {
			alarmItem = errorAlarmMap.get(eDaily.getErrorAlarmWorkRecordCode()).getName();
		}

		return new AlarmContentMessage(alarmItem, alarmContent);
	}
	
	private String formatValueAttendance(String attendanceValue) {
		if(attendanceValue.length()<4) attendanceValue = "0" + attendanceValue;
		return attendanceValue.substring(0,  2) + ":" + attendanceValue.substring(2, 4);		
	}

}
