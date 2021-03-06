package nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.divergence.time.service.DivTimeSysFixedCheckService;
import nts.uk.ctx.at.record.dom.require.RecordDomRequireService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.service.ErAlCheckService;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.attendance.MasterShareBus;
import nts.uk.ctx.at.shared.dom.attendance.MasterShareBus.MasterShareContainer;
import nts.uk.ctx.at.shared.dom.common.TimeOfDay;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.service.AttendanceItemConvertFactory;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.CommonCompanySettingForCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.erroralarm.SystemFixedErrorAlarm;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.ManagePerCompanySet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.ManagePerPersonDailySet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.deviationtime.deviationtimeframe.CheckExcessAtr;
import nts.uk.ctx.at.shared.dom.scherec.dailyprocess.calc.FactoryManagePerPersonDailySet;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.algorithm.DailyStatutoryLaborTime;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.DailyUnit;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * ???????????????????????????????????????
 * @author keisuke_hoshina
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CalculationErrorCheckServiceImpl implements CalculationErrorCheckService{
	@Inject
	/*?????????????????????????????????????????????????????????*/
	private DivTimeSysFixedCheckService divTimeSysFixedCheckService;
	
	@Inject
	private ErAlCheckService erAlCheckService;
	
	@Inject
	private AttendanceItemConvertFactory converterFactory;
	//???????????????????????????
	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository;
	
	@Inject
	/*??????????????????????????????????????????????????????????????????*/
	private DailyRecordCreateErrorAlermService dailyRecordCreateErrorAlermService;
	
	@Inject
	private CommonCompanySettingForCalc commonCompanySettingForCalc;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject 
	private RecordDomRequireService requireService;
	
	@Inject
	private FactoryManagePerPersonDailySet factoryManagePerPersonDailySet;

	/** ??????????????????????????????????????? */
	@Inject
	private DeclareErrorCheckService declareErrorCheckService;
	
	@Override
	public IntegrationOfDaily errorCheck(IntegrationOfDaily integrationOfDaily, ManagePerPersonDailySet personCommonSetting, ManagePerCompanySet master) {
		String companyID = AppContexts.user().companyId();
		List<EmployeeDailyPerError> addItemList = integrationOfDaily.getEmployeeError() == null? integrationOfDaily.getEmployeeError() :new ArrayList<>();
		List<ErrorAlarmWorkRecord> divergenceError = new ArrayList<>();
		DailyRecordToAttendanceItemConverter attendanceItemConverter = this.converterFactory.createDailyConverter().setData(integrationOfDaily);
		//???????????????????????????????????????????????????
		for(ErrorAlarmWorkRecord errorItem : errorAlarmWorkRecordRepository.getAllErAlCompanyAndUseAtr(companyID, true)) {
			//???????????????
			if(!errorItem.getUseAtr()) continue;
			//?????????????????????????????????????????????????????????
			if(includeDivergence(errorItem)) { 
				divergenceError.add(errorItem);
				continue;
			}
			//??????????????????
			List<EmployeeDailyPerError>  addItems = new ArrayList<>();
			if(errorItem.getFixedAtr()) {
				addItems = systemErrorCheck(integrationOfDaily,errorItem,attendanceItemConverter, master);
			}
			
			//???????????????
			else {
				addItems = erAlCheckService.checkErrorFor(companyID, integrationOfDaily.getEmployeeId(), 
						integrationOfDaily.getYmd(), errorItem, integrationOfDaily);
				//addItemList.addAll(addItems);
			}
			addItemList.addAll(addItems);
		}
		
		//?????????????????????????????????????????????????????????(??????????????????????????????)
		addItemList.addAll(divergenceErrorCheck(integrationOfDaily, master, divergenceError));
		addItemList = addItemList.stream().filter(tc -> tc != null).collect(Collectors.toList());
		integrationOfDaily.setEmployeeError(addItemList);
		return integrationOfDaily;
	}
	

	/**
	 * ?????????????????????????????????????????????
	 * @return??????????????????????????????
	 */
	private List<EmployeeDailyPerError> divergenceErrorCheck(IntegrationOfDaily integrationOfDaily,ManagePerCompanySet master,List<ErrorAlarmWorkRecord> errorList) {
		if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {
			TimeLeavingOfDailyPerformance dailyPerformance = new TimeLeavingOfDailyPerformance(integrationOfDaily.getEmployeeId(), 
					integrationOfDaily.getYmd(), 
					integrationOfDaily.getAttendanceLeave().isPresent() ? integrationOfDaily.getAttendanceLeave().get() : null);
			return divTimeSysFixedCheckService.divergenceTimeCheckBySystemFixed(AppContexts.user().companyId(), 
																	 	 		integrationOfDaily.getEmployeeId(), 
																	 	 		integrationOfDaily.getYmd(),
																	 	 		integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getDivTime().getDivergenceTime(),
																	 	 		Optional.ofNullable(dailyPerformance),
																	 	 		errorList,
																	 	 		master.getDivergenceTime(),
																	 	 		master.getShareContainer());
		}
		return Collections.emptyList();
	}

	/**
	 * ??????????????????????????????????????????????????????
	 * @param errorItem ?????????????????????
	 * @return?????????????????????
	 */
	private boolean includeDivergence(ErrorAlarmWorkRecord errorItem) {
		Optional<SystemFixedErrorAlarm> fixedErrorAlarmCode = SystemFixedErrorAlarm.getEnumFromErrorCode(errorItem.getCode().toString());
		if(!fixedErrorAlarmCode.isPresent()) 
			return false; 
		switch (fixedErrorAlarmCode.get()) {
		case ERROR_OF_DIVERGENCE_TIME:
		case ALARM_OF_DIVERGENCE_TIME:
		case DIVERGENCE_ERROR_1:
		case DIVERGENCE_ALARM_1:
		case DIVERGENCE_ERROR_2:
		case DIVERGENCE_ALARM_2:
		case DIVERGENCE_ERROR_3:
		case DIVERGENCE_ALARM_3:
		case DIVERGENCE_ERROR_4:
		case DIVERGENCE_ALARM_4:
		case DIVERGENCE_ERROR_5:
		case DIVERGENCE_ALARM_5:
		case DIVERGENCE_ERROR_6:
		case DIVERGENCE_ALARM_6:
		case DIVERGENCE_ERROR_7:
		case DIVERGENCE_ALARM_7:
		case DIVERGENCE_ERROR_8:
		case DIVERGENCE_ALARM_8:
		case DIVERGENCE_ERROR_9:
		case DIVERGENCE_ALARM_9:	
		case DIVERGENCE_ERROR_10:
		case DIVERGENCE_ALARM_10:
			return true;
		default:
			return false;
		}
	}

	/**
	 * ???????????????????????????????????????
	 * @param attendanceItemConverter 
	 * @return ????????????????????????????????????
	 */
	public List<EmployeeDailyPerError> systemErrorCheck(IntegrationOfDaily integrationOfDaily,ErrorAlarmWorkRecord errorItem, 
			DailyRecordToAttendanceItemConverter attendanceItemConverter, ManagePerCompanySet master) {
		Optional<SystemFixedErrorAlarm> fixedErrorAlarmCode = SystemFixedErrorAlarm.getEnumFromErrorCode(errorItem.getCode().toString());
		//if(!integrationOfDaily.getAttendanceLeave().isPresent() || !fixedErrorAlarmCode.isPresent())
		if(!fixedErrorAlarmCode.isPresent())
			return Collections.emptyList();

		switch(fixedErrorAlarmCode.get()) {
			//????????????????????????
			case PRE_OVERTIME_APP_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getEmployeeId(), 
												integrationOfDaily.getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.PRE_OVERTIME_APP_EXCESS);
			//????????????????????????
			case PRE_HOLIDAYWORK_APP_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getEmployeeId(), 
												integrationOfDaily.getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.PRE_HOLIDAYWORK_APP_EXCESS);
			//?????????????????????????????????
			case PRE_FLEX_APP_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getEmployeeId(), 
												integrationOfDaily.getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.PRE_FLEX_APP_EXCESS);
			//????????????????????????
			case PRE_MIDNIGHT_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getEmployeeId(), 
												integrationOfDaily.getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.PRE_MIDNIGHT_EXCESS);
			//????????????????????????
			case OVER_TIME_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getEmployeeId(), 
												integrationOfDaily.getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.OVER_TIME_EXCESS);
			//????????????????????????
			case REST_TIME_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getEmployeeId(), 
												integrationOfDaily.getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.REST_TIME_EXCESS);
			//?????????????????????????????????
			case FLEX_OVER_TIME:
				return integrationOfDaily.getErrorList(integrationOfDaily.getEmployeeId(), 
												integrationOfDaily.getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.FLEX_OVER_TIME);
			//????????????????????????
			case MIDNIGHT_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getEmployeeId(), 
												integrationOfDaily.getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.MIDNIGHT_EXCESS);
			//??????
			case LATE:
				return integrationOfDaily.getErrorList(integrationOfDaily.getEmployeeId(), 
												integrationOfDaily.getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.LATE);
			//??????
			case LEAVE_EARLY:
				return integrationOfDaily.getErrorList(integrationOfDaily.getEmployeeId(), 
												integrationOfDaily.getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.LEAVE_EARLY);
			//?????????????????? - ?????????????????????
			case TIME_LEAVING_STAMP_LEAKAGE:
				return dailyRecordCreateErrorAlermService.lackOfTimeLeavingStamping(integrationOfDaily);
			//???????????? - ?????????????????????
			case ENTRANCE_STAMP_LACKING:
				return dailyRecordCreateErrorAlermService.lackOfAttendanceGateStamping(integrationOfDaily);
			//PC?????????????????? - PC????????????????????????
			case PCLOG_STAMP_LEAKAGE:
				return dailyRecordCreateErrorAlermService.lackOfAttendancePCLogOnStamping(integrationOfDaily);
			//??????????????????
			case INCORRECT_STAMP: 
				return dailyRecordCreateErrorAlermService.stampIncorrectOrderAlgorithm(integrationOfDaily);
			//????????????
			case HOLIDAY_STAMP:
				val result = dailyRecordCreateErrorAlermService.checkHolidayStamp(integrationOfDaily);
				if(!result.isPresent()) {
					return Collections.emptyList();
				}
				else {
					return Arrays.asList(result.get());
				}
				
			//????????????
			case DOUBLE_STAMP:
				return dailyRecordCreateErrorAlermService.doubleStampAlgorithm(integrationOfDaily);
			//??????
			case DECLARE:
				return this.declareErrorCheckService.errorCheck(integrationOfDaily, fixedErrorAlarmCode.get());
			//?????????????????????
			default:
				return Collections.emptyList();
		}
	}


	@Override
	public IntegrationOfDaily errorCheck(String companyId, String employeeID, GeneralDate ymd,
			IntegrationOfDaily integrationOfDaily, boolean sysfixecategory) {
		// ????????????????????????
		val companyCommonSetting = commonCompanySettingForCalc.getCompanySetting();

		// ????????????????????????
		val integraListByRecordAndEmpId = getIntegrationOfDailyByEmpId(Arrays.asList(integrationOfDaily));

		// ????????????????????????
		val masterData = workingConditionItemRepository
				.getBySidAndPeriodOrderByStrDWithDatePeriod(integraListByRecordAndEmpId, ymd, ymd);

		// nowIntegration??????????????????
		Optional<Entry<DateHistoryItem, WorkingConditionItem>> nowWorkingItem = masterData
				.getItemAtDateAndEmpId(integrationOfDaily.getYmd(), integrationOfDaily.getEmployeeId());
		if (nowWorkingItem.isPresent()) {
			DailyUnit dailyUnit = DailyStatutoryLaborTime.getDailyUnit(
					requireService.createRequire(),
					new CacheCarrier(), companyId,
					integrationOfDaily.getAffiliationInfor().getEmploymentCode().toString(),
					integrationOfDaily.getEmployeeId(), integrationOfDaily.getYmd(),
					nowWorkingItem.get().getValue().getLaborSystem());
			if (dailyUnit == null || dailyUnit.getDailyTime() == null)
				dailyUnit = new DailyUnit(new TimeOfDay(0));
			else {
				if (companyCommonSetting.getShareContainer() == null) {
					MasterShareContainer<String> shareContainer = MasterShareBus.open();
					companyCommonSetting.setShareContainer(shareContainer);
				}
				Optional<ManagePerPersonDailySet> optManagePerPersonDailySet = factoryManagePerPersonDailySet.create(companyId, companyCommonSetting, 
						integrationOfDaily, nowWorkingItem.get().getValue());
				if(optManagePerPersonDailySet.isPresent()) {
					integrationOfDaily = errorCheckNew(integrationOfDaily,
						optManagePerPersonDailySet.get(),
						companyCommonSetting, sysfixecategory);
				}
			}
		}
		return integrationOfDaily;
	}

	public IntegrationOfDaily errorCheckNew(IntegrationOfDaily integrationOfDaily,
			ManagePerPersonDailySet personCommonSetting, ManagePerCompanySet master, boolean sysfixecategory) {
		String companyID = AppContexts.user().companyId();
		List<EmployeeDailyPerError> addItemList = integrationOfDaily.getEmployeeError() == null
				? integrationOfDaily.getEmployeeError()
				: new ArrayList<>();
		List<ErrorAlarmWorkRecord> divergenceError = new ArrayList<>();
		DailyRecordToAttendanceItemConverter attendanceItemConverter = this.converterFactory.createDailyConverter()
				.setData(integrationOfDaily);
		// ???????????????????????????????????????????????????
		for (ErrorAlarmWorkRecord errorItem : errorAlarmWorkRecordRepository.getAllErAlCompanyAndUseAtr(companyID, true)) {
			// ???????????????
			if (!errorItem.getUseAtr())
				continue;
			// ?????????????????????????????????????????????????????????
			if (includeDivergence(errorItem)) {
				divergenceError.add(errorItem);
				continue;
			}
			// ??????????????????
			List<EmployeeDailyPerError> addItems = new ArrayList<>();
			if (sysfixecategory) {
				if(errorItem.getFixedAtr()) {
					addItems = systemErrorCheck(integrationOfDaily, errorItem, attendanceItemConverter, master);
				}
			}else {
				if(!errorItem.getFixedAtr()) {
					// ???????????????
					addItems = erAlCheckService.checkErrorFor(companyID, integrationOfDaily.getEmployeeId(),
							integrationOfDaily.getYmd(), errorItem, integrationOfDaily);
				}
			}
			addItemList.addAll(addItems);
		}

		// ?????????????????????????????????????????????????????????(??????????????????????????????)
		addItemList.addAll(divergenceErrorCheck(integrationOfDaily, master, divergenceError));
		addItemList = addItemList.stream().filter(tc -> tc != null).collect(Collectors.toList());
		integrationOfDaily.setEmployeeError(addItemList);
		return integrationOfDaily;
	}

	/**
	 * ????????????(WORK)List??????????????????????????????????????????????????????
	 * 
	 * @param integrationOfDaily
	 * @return <??????????????????????????????
	 */
	private Map<String, DatePeriod> getIntegrationOfDailyByEmpId(List<IntegrationOfDaily> integrationOfDaily) {
		Map<String, DatePeriod> returnMap = new HashMap<>();
		// ????????????ID ????????????
		List<String> idList = getAllEmpId(integrationOfDaily);
		idList.forEach(id -> {
			// ???????????????ID?????????????????????integration?????????
			val integrationOfDailys = integrationOfDaily.stream().filter(tc -> tc.getEmployeeId().equals(id))
					.collect(Collectors.toList());
			// ?????????????????????????????????????????????
			val createdDatePriod = getDateSpan(integrationOfDailys);
			// Map<???????????????ID??????????????????>???????????????
			returnMap.put(id, createdDatePriod);
		});
		return returnMap;
	}

	/*
	 * ?????????????????????
	 */
	private List<String> getAllEmpId(List<IntegrationOfDaily> integrationOfDailys) {
		return integrationOfDailys.stream().distinct().map(integrationOfDaily -> integrationOfDaily.getEmployeeId())
				.collect(Collectors.toList());
	}

	/**
	 * ????????????????????????
	 * @param integrationOfDaily ????????????(WORK)LIST
	 * @return ???????????????
	 */
	private DatePeriod getDateSpan(List<IntegrationOfDaily> integrationOfDailys) {
		val sortedIntegration = integrationOfDailys.stream().sorted((first,second) -> first.getYmd().compareTo(second.getYmd())).collect(Collectors.toList());
		return new DatePeriod(sortedIntegration.get(0).getYmd(), sortedIntegration.get(sortedIntegration.size() - 1).getYmd());
	}
}
