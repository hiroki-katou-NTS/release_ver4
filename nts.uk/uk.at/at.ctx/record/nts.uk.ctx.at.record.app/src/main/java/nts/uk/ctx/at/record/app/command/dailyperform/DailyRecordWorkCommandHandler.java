package nts.uk.ctx.at.record.app.command.dailyperform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.command.dailyperform.affiliationInfor.AffiliationInforOfDailyPerformCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.affiliationInfor.AffiliationInforOfDailyPerformCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.affiliationInfor.BusinessTypeOfDailyPerformCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.affiliationInfor.BusinessTypeOfDailyPerformCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.attendanceleavinggate.AttendanceLeavingGateOfDailyCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.attendanceleavinggate.AttendanceLeavingGateOfDailyCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.attendanceleavinggate.PCLogInfoOfDailyCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.attendanceleavinggate.PCLogInfoOfDailyCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.attendancetime.AttendanceTimeOfDailyPerformCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.attendancetime.AttendanceTimeOfDailyPerformCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.breaktime.BreakTimeOfDailyPerformanceCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.breaktime.BreakTimeOfDailyPerformanceCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.calculationattribute.CalcAttrOfDailyPerformanceCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.calculationattribute.CalcAttrOfDailyPerformanceCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.editstate.EditStateOfDailyPerformCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.editstate.EditStateOfDailyPerformCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.goout.OutingTimeOfDailyPerformanceCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.goout.OutingTimeOfDailyPerformanceCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.optionalitem.OptionalItemOfDailyPerformCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.optionalitem.OptionalItemOfDailyPerformCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.remark.RemarkOfDailyCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.remark.RemarkOfDailyCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.shorttimework.ShortTimeOfDailyCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.shorttimework.ShortTimeOfDailyCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.specificdatetttr.SpecificDateAttrOfDailyCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.specificdatetttr.SpecificDateAttrOfDailyCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.temporarytime.TemporaryTimeOfDailyPerformanceCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.temporarytime.TemporaryTimeOfDailyPerformanceCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.workinfo.WorkInformationOfDailyPerformCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.workinfo.WorkInformationOfDailyPerformCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.workrecord.AttendanceTimeByWorkOfDailyCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.workrecord.AttendanceTimeByWorkOfDailyCommandUpdateHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.workrecord.TimeLeavingOfDailyPerformanceCommandAddHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.workrecord.TimeLeavingOfDailyPerformanceCommandUpdateHandler;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordWorkFinder;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculateDailyRecordServiceCenter;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.app.util.attendanceitem.DailyWorkCommonCommand;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.RecordHandler;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;

@Stateless
public class DailyRecordWorkCommandHandler extends RecordHandler {

	/** 勤務情報： 日別実績の勤務情報 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_WORK_INFO_CODE, 
			jpPropertyName = DAILY_WORK_INFO_NAME, index = 1)
	private WorkInformationOfDailyPerformCommandAddHandler workInfoAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_WORK_INFO_CODE, 
			jpPropertyName = DAILY_WORK_INFO_NAME, index = 1)
	private WorkInformationOfDailyPerformCommandUpdateHandler workInfoUpdateHandler;

	/** 計算区分： 日別実績の計算区分 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_CALCULATION_ATTR_CODE, 
			jpPropertyName = DAILY_CALCULATION_ATTR_NAME, index = 2)
	private CalcAttrOfDailyPerformanceCommandAddHandler calcAttrAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_CALCULATION_ATTR_CODE,
			jpPropertyName = DAILY_CALCULATION_ATTR_NAME, index = 2)
	private CalcAttrOfDailyPerformanceCommandUpdateHandler calcAttrUpdateHandler;

	/** 所属情報： 日別実績の所属情報 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_AFFILIATION_INFO_CODE, 
			jpPropertyName = DAILY_AFFILIATION_INFO_NAME, index = 3)
	private AffiliationInforOfDailyPerformCommandAddHandler affiliationInfoAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_AFFILIATION_INFO_CODE, 
			jpPropertyName = DAILY_AFFILIATION_INFO_NAME, index = 3)
	private AffiliationInforOfDailyPerformCommandUpdateHandler affiliationInfoUpdateHandler;

	/** エラー一覧： 社員の日別実績エラー一覧 */
//	@Inject
//	@AttendanceItemLayout(layout = "D", jpPropertyName = "", index = 4)
//	private EmployeeDailyPerErrorCommandAddHandler errorAddHandler;
//	@Inject
//	@AttendanceItemLayout(layout = "D", jpPropertyName = "", index = 4)
//	private EmployeeDailyPerErrorCommandUpdateHandler errorUpdateHandler;
	
	/** エラー一覧： 社員の日別実績エラー一覧 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_BUSINESS_TYPE_CODE,
			jpPropertyName = DAILY_BUSINESS_TYPE_NAME, index = 4)
	private BusinessTypeOfDailyPerformCommandAddHandler businessTypeAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_BUSINESS_TYPE_CODE, 
			jpPropertyName = DAILY_BUSINESS_TYPE_NAME, index = 4)
	private BusinessTypeOfDailyPerformCommandUpdateHandler businessTypeUpdateHandler;

	/** 外出時間帯: 日別実績の外出時間帯 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_OUTING_TIME_CODE, 
			jpPropertyName = DAILY_OUTING_TIME_NAME, index = 5)
	private OutingTimeOfDailyPerformanceCommandAddHandler outingTimeAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_OUTING_TIME_CODE, 
			jpPropertyName = DAILY_OUTING_TIME_NAME, index = 5)
	private OutingTimeOfDailyPerformanceCommandUpdateHandler outingTimeUpdateHandler;

	/** 休憩時間帯: 日別実績の休憩時間帯 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_BREAK_TIME_CODE, 
			jpPropertyName = DAILY_BREAK_TIME_NAME, index = 6)
	private BreakTimeOfDailyPerformanceCommandAddHandler breakTimeAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_BREAK_TIME_CODE, 
			jpPropertyName = DAILY_BREAK_TIME_NAME, index = 6)
	private BreakTimeOfDailyPerformanceCommandUpdateHandler breakTimeUpdateHandler;

	/** 勤怠時間: 日別実績の勤怠時間 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_ATTENDANCE_TIME_CODE, 
			jpPropertyName = DAILY_ATTENDANCE_TIME_NAME, index = 7)
	private AttendanceTimeOfDailyPerformCommandAddHandler attendanceTimeAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_ATTENDANCE_TIME_CODE, 
			jpPropertyName = DAILY_ATTENDANCE_TIME_NAME, index = 7)
	private AttendanceTimeOfDailyPerformCommandUpdateHandler attendanceTimeUpdateHandler;

	/** 作業別勤怠時間: 日別実績の作業別勤怠時間 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_ATTENDANCE_TIME_BY_WORK_CODE, 
			jpPropertyName = DAILY_ATTENDANCE_TIME_BY_WORK_NAME, index = 8)
	private AttendanceTimeByWorkOfDailyCommandAddHandler attendanceTimeByWorkAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_ATTENDANCE_TIME_BY_WORK_CODE, 
			jpPropertyName = DAILY_ATTENDANCE_TIME_BY_WORK_NAME, index = 8)
	private AttendanceTimeByWorkOfDailyCommandUpdateHandler attendanceTimeByWorkUpdateHandler;

	/** 出退勤: 日別実績の出退勤 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_ATTENDACE_LEAVE_CODE, 
			jpPropertyName = DAILY_ATTENDACE_LEAVE_NAME, index = 9)
	private TimeLeavingOfDailyPerformanceCommandAddHandler timeLeavingAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_ATTENDACE_LEAVE_CODE, 
			jpPropertyName = DAILY_ATTENDACE_LEAVE_NAME, index = 9)
	private TimeLeavingOfDailyPerformanceCommandUpdateHandler timeLeavingUpdatedHandler;

	/** 短時間勤務時間帯: 日別実績の短時間勤務時間帯 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_SHORT_TIME_CODE,  
			jpPropertyName = DAILY_SHORT_TIME_NAME, index = 10)
	private ShortTimeOfDailyCommandAddHandler shortWorkTimeAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_SHORT_TIME_CODE,  
			jpPropertyName = DAILY_SHORT_TIME_NAME, index = 10)
	private ShortTimeOfDailyCommandUpdateHandler shortWorkTimeUpdateHandler;

	/** 特定日区分: 日別実績の特定日区分 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_SPECIFIC_DATE_ATTR_CODE,  
			jpPropertyName = DAILY_SPECIFIC_DATE_ATTR_NAME,  index = 11)
	private SpecificDateAttrOfDailyCommandAddHandler specificDateAttrAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_SPECIFIC_DATE_ATTR_CODE,  
			jpPropertyName = DAILY_SPECIFIC_DATE_ATTR_NAME,  index = 11)
	private SpecificDateAttrOfDailyCommandUpdateHandler specificDateAttrUpdateHandler;

	/** 入退門: 日別実績の入退門 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_ATTENDANCE_LEAVE_GATE_CODE,  
			jpPropertyName = DAILY_ATTENDANCE_LEAVE_GATE_NAME,  index = 12)
	private AttendanceLeavingGateOfDailyCommandAddHandler attendanceLeavingGateAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_ATTENDANCE_LEAVE_GATE_CODE,  
			jpPropertyName = DAILY_ATTENDANCE_LEAVE_GATE_NAME,  index = 12)
	private AttendanceLeavingGateOfDailyCommandUpdateHandler attendanceLeavingGateUpdateHandler;

	/** 任意項目: 日別実績の任意項目 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_OPTIONAL_ITEM_CODE,  
			jpPropertyName = DAILY_ATTENDACE_LEAVE_NAME,  index = 13)
	private OptionalItemOfDailyPerformCommandAddHandler optionalItemAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_OPTIONAL_ITEM_CODE,  
			jpPropertyName = DAILY_OPTIONAL_ITEM_NAME,  index = 13)
	private OptionalItemOfDailyPerformCommandUpdateHandler optionalItemUpdateHandler;

	/** 編集状態: 日別実績の編集状態 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_EDIT_STATE_CODE,  
			jpPropertyName = DAILY_EDIT_STATE_NAME,  index = 14)
	private EditStateOfDailyPerformCommandAddHandler editStateAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_EDIT_STATE_CODE,  
			jpPropertyName = DAILY_EDIT_STATE_NAME,  index = 14)
	private EditStateOfDailyPerformCommandUpdateHandler editStateUpdateHandler;

	/** 臨時出退勤: 日別実績の臨時出退勤 */
	@Inject
	@AttendanceItemLayout(layout = DAILY_TEMPORARY_TIME_CODE,  
			jpPropertyName = DAILY_TEMPORARY_TIME_NAME,  index = 15)
	private TemporaryTimeOfDailyPerformanceCommandAddHandler temporaryTimeAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_TEMPORARY_TIME_CODE,  
			jpPropertyName = DAILY_TEMPORARY_TIME_NAME,  index = 15)
	private TemporaryTimeOfDailyPerformanceCommandUpdateHandler temporaryTimeUpdateHandler;
	
	@Inject
	@AttendanceItemLayout(layout = DAILY_PC_LOG_INFO_CODE,  
			jpPropertyName = DAILY_PC_LOG_INFO_NAME,  index = 16)
	private PCLogInfoOfDailyCommandAddHandler pcLogInfoAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_PC_LOG_INFO_CODE,  
			jpPropertyName = DAILY_PC_LOG_INFO_NAME,  index = 16)
	private PCLogInfoOfDailyCommandUpdateHandler pcLogInfoUpdateHandler;

	@Inject
	@AttendanceItemLayout(layout = DAILY_REMARKS_CODE,  
			jpPropertyName = DAILY_REMARKS_NAME,  index = 17)
	private RemarkOfDailyCommandAddHandler remarksAddHandler;
	@Inject
	@AttendanceItemLayout(layout = DAILY_REMARKS_CODE,  
			jpPropertyName = DAILY_REMARKS_NAME,  index = 17)
	private RemarkOfDailyCommandUpdateHandler remarksUpdateHandler;
	
	@Inject
	private CalculateDailyRecordServiceCenter calcService;
	
//	@Inject 
//	private ErAlCheckService determineErrorAlarmWorkRecordService;
	
	@Inject
	private EmployeeDailyPerErrorRepository employeeErrorRepo;
	
	@Inject
	private DailyRecordWorkFinder finder;
	
	private final List<String> DOMAIN_CHANGED_BY_CALCULATE = Arrays.asList(DAILY_ATTENDANCE_TIME_CODE, DAILY_OPTIONAL_ITEM_CODE);
	
	private final Map<String, String[]> DOMAIN_CHANGED_BY_EVENT = new HashMap<>();
	{
		DOMAIN_CHANGED_BY_EVENT.put(DAILY_WORK_INFO_CODE, 
									getArray(DAILY_ATTENDACE_LEAVE_CODE, 
											DAILY_BREAK_TIME_CODE));
		DOMAIN_CHANGED_BY_EVENT.put(DAILY_ATTENDACE_LEAVE_CODE, 
									getArray(DAILY_BREAK_TIME_CODE));
	}
	
	private String[] getArray(String... arrays){
		return arrays;
	}

	public void handleAdd(DailyRecordWorkCommand command) {
		handler(command, false);
	}
	
	public void handleUpdate(DailyRecordWorkCommand command) {
		handler(command, true);
	}
	
	public void handleAdd(List<DailyRecordWorkCommand> command) {
		handler(command, false);
	}
	
	public void handleUpdate(List<DailyRecordWorkCommand> command) {
		handler(command, true);
	}

	private <T extends DailyWorkCommonCommand> void handler(DailyRecordWorkCommand command, boolean isUpdate) {
		handler(Arrays.asList(command), isUpdate);
	}
	
	private <T extends DailyWorkCommonCommand> void handler(List<DailyRecordWorkCommand> commands, boolean isUpdate) {
		registerNotCalcDomain(commands, isUpdate);
		
		List<IntegrationOfDaily> calced = calcIfNeed(commands);
		
		updateDomainAfterCalc(commands, isUpdate, calced);
		
		registerErrorWhenCalc(toMapParam(commands), 
				calced.stream().map(d -> d.getEmployeeError()).flatMap(List::stream).collect(Collectors.toList()));
	}

	@SuppressWarnings({ "unchecked" })
	private <T extends DailyWorkCommonCommand> void updateDomainAfterCalc(List<DailyRecordWorkCommand> commands,
			boolean isUpdate, List<IntegrationOfDaily> calced) {
		commands.stream().forEach(c -> {
			calced.stream().filter(d -> d.getAffiliationInfor().getEmployeeId().equals(c.getEmployeeId()) 
					&& d.getAffiliationInfor().getYmd().equals(c.getWorkDate()))
			.findFirst().ifPresent(d -> {
				DOMAIN_CHANGED_BY_CALCULATE.stream().forEach(layout -> {
					T command = (T) c.getCommand(layout);
					Object updatedD = getDomain(layout, d);
					if(updatedD != null){
						updateCommandData(command, updatedD);
						CommandFacade<T> handler = (CommandFacade<T>) getHandler(layout, isUpdate);
						if(handler != null){
							handler.handle(command);
						}
					}
				});
				
			});
		});
	}
	
	@SuppressWarnings({ "unchecked" })
	private <T extends DailyWorkCommonCommand> void registerAllCommand(List<DailyRecordWorkCommand> commands, boolean isUpdate) {
		commands.stream().forEach(c -> {
			c.getAvailableLayout().stream().forEach(layout -> {
				T command = (T) c.getCommand(layout);
				CommandFacade<T> handler = (CommandFacade<T>) getHandler(layout, isUpdate);
				if(handler != null){
					handler.handle(command);
				}
			});
		});
	}

	private void registerErrorWhenCalc(Map<String, List<GeneralDate>> param, List<EmployeeDailyPerError> errors) {
		//remove data error
		employeeErrorRepo.removeParam(param);
		//insert error;
		employeeErrorRepo.insert(errors.stream().filter(e -> e!= null && e.getAttendanceItemList().get(0) != null)
				.collect(Collectors.toList()));
//		determineErrorAlarmWorkRecordService.createEmployeeDailyPerError(errors);
	}

	@SuppressWarnings({ "unchecked" })
	private <T extends DailyWorkCommonCommand> void registerNotCalcDomain(List<DailyRecordWorkCommand> commands, boolean isUpdate) {
		commands.stream().forEach(command -> {
			handleEditStates(isUpdate, command);
			
			List<String> mapped = command.itemValues().stream().map(c -> getGroup(c)).distinct().collect(Collectors.toList());
			
			mapped.stream().filter(c -> !DOMAIN_CHANGED_BY_CALCULATE.contains(c)).forEach(c -> {
				CommandFacade<T> handler = (CommandFacade<T>) getHandler(c, isUpdate);
				if(handler != null){
					handler.handle((T) command.getCommand(c));
				}
			});
			
			DOMAIN_CHANGED_BY_EVENT.values().stream().flatMap(x -> Arrays.stream(x)).distinct().forEach(layout -> {
				if(mapped.contains(layout)){
					FinderFacade cFinder = finder.getFinder(layout);
					if(cFinder != null){
						Object updatedD = cFinder.getDomain(command.getEmployeeId(), command.getWorkDate());
						updateCommandData(command.getCommand(layout), updatedD);
					}
				}
			});
		});
	}

	@SuppressWarnings({ "unchecked" })
	private <T extends DailyWorkCommonCommand> void handleEditStates(boolean isUpdate, DailyRecordWorkCommand command) {
		CommandFacade<T> handler = (CommandFacade<T>) getHandler(DAILY_EDIT_STATE_CODE, isUpdate);
		if(handler != null){
			handler.handle((T) command.getCommand(DAILY_EDIT_STATE_CODE));
		}
	}
	
	private List<IntegrationOfDaily> calcIfNeed(List<DailyRecordWorkCommand> commands){
		return calcService.calculate(commands.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	private <T extends DailyWorkCommonCommand> void updateCommandData(T command, Object updatedD) {
		if(ConvertHelper.isCollection(updatedD)){
			command.updateDatas((List<?>) updatedD);
		} else if (ConvertHelper.isOptional(updatedD)) {
			command.updateDataO((Optional<?>) updatedD);
		} else {
			command.updateData(updatedD);
		}
	}
	
	private CommandFacade<?> getHandler(String layout, boolean isUpdate) {
		switch (layout) {
		case DAILY_WORK_INFO_CODE:
			return isUpdate ? this.workInfoUpdateHandler : this.workInfoAddHandler;
		case DAILY_CALCULATION_ATTR_CODE:
			return isUpdate ? this.calcAttrUpdateHandler : this.calcAttrAddHandler;
		case DAILY_AFFILIATION_INFO_CODE:
			return isUpdate ? this.affiliationInfoUpdateHandler : this.affiliationInfoAddHandler;
		case DAILY_BUSINESS_TYPE_CODE:
			return isUpdate ? this.businessTypeUpdateHandler : this.businessTypeAddHandler;
		case DAILY_OUTING_TIME_CODE:
			return isUpdate ? this.outingTimeUpdateHandler : this.outingTimeAddHandler;
		case DAILY_BREAK_TIME_CODE:
			return isUpdate ? this.breakTimeUpdateHandler : this.breakTimeAddHandler;
		case DAILY_ATTENDANCE_TIME_CODE:
			return isUpdate ? this.attendanceTimeUpdateHandler : this.attendanceTimeAddHandler;
		case DAILY_ATTENDANCE_TIME_BY_WORK_CODE:
			return isUpdate ? this.attendanceTimeByWorkUpdateHandler : this.attendanceTimeByWorkAddHandler;
		case DAILY_ATTENDACE_LEAVE_CODE:
			return isUpdate ? this.timeLeavingUpdatedHandler : this.timeLeavingAddHandler;
		case DAILY_SHORT_TIME_CODE:
			return isUpdate ? this.shortWorkTimeUpdateHandler : this.shortWorkTimeAddHandler;
		case DAILY_SPECIFIC_DATE_ATTR_CODE:
			return isUpdate ? this.specificDateAttrUpdateHandler : this.specificDateAttrAddHandler;
		case DAILY_ATTENDANCE_LEAVE_GATE_CODE:
			return isUpdate ? this.attendanceLeavingGateUpdateHandler : this.attendanceLeavingGateAddHandler;
		case DAILY_OPTIONAL_ITEM_CODE:
			return isUpdate ? this.optionalItemUpdateHandler : this.optionalItemAddHandler;
		case DAILY_EDIT_STATE_CODE:
			return isUpdate ? this.editStateUpdateHandler : this.editStateAddHandler;
		case DAILY_TEMPORARY_TIME_CODE:
			return isUpdate ? this.temporaryTimeUpdateHandler : this.temporaryTimeAddHandler;
		case DAILY_PC_LOG_INFO_CODE:
			return isUpdate ? this.pcLogInfoUpdateHandler : this.pcLogInfoAddHandler;
		case DAILY_REMARKS_CODE:
			return isUpdate ? this.remarksUpdateHandler : this.remarksAddHandler;
		default:
			return null;
		}
	}

	private Object getDomain(String layout, IntegrationOfDaily d) {
		switch (layout) {
		case DAILY_WORK_INFO_CODE:
			return d.getWorkInformation();
		case DAILY_CALCULATION_ATTR_CODE:
			return d.getCalAttr();
		case DAILY_AFFILIATION_INFO_CODE:
			return d.getAffiliationInfor();
		case DAILY_BUSINESS_TYPE_CODE:
			return d.getBusinessType();
		case DAILY_OUTING_TIME_CODE:
			return d.getOutingTime();
		case DAILY_BREAK_TIME_CODE:
			return d.getBreakTime();
		case DAILY_ATTENDANCE_TIME_CODE:
			return d.getAttendanceTimeOfDailyPerformance();
		case DAILY_ATTENDANCE_TIME_BY_WORK_CODE:
			return d.getAttendancetimeByWork();
		case DAILY_ATTENDACE_LEAVE_CODE:
			return d.getAttendanceLeave();
		case DAILY_SHORT_TIME_CODE:
			return d.getShortTime();
		case DAILY_SPECIFIC_DATE_ATTR_CODE:
			return d.getSpecDateAttr();
		case DAILY_ATTENDANCE_LEAVE_GATE_CODE:
			return d.getAttendanceLeavingGate();
		case DAILY_OPTIONAL_ITEM_CODE:
			return d.getAnyItemValue();
		case DAILY_EDIT_STATE_CODE:
			return d.getEditState();
		case DAILY_TEMPORARY_TIME_CODE:
			return d.getTempTime();
		case DAILY_PC_LOG_INFO_CODE:
			return d.getPcLogOnInfo();
		case DAILY_REMARKS_CODE:
			return null;
		default:
			return null;
		}
	}

	private String getGroup(ItemValue c) {
		return String.valueOf(c.layoutCode().charAt(0));
	}

	private Map<String, List<GeneralDate>> toMapParam(List<DailyRecordWorkCommand> commands) {
		return commands.stream().collect(Collectors.groupingBy(c -> c.getEmployeeId(), 
														Collectors.collectingAndThen(Collectors.toList(), 
																c -> c.stream().map(q -> q.getWorkDate())
																				.collect(Collectors.toList()))));
	}

}
