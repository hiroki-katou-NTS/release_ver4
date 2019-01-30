package nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemConvertFactory;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CheckExcessAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManagePerCompanySet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManagePerPersonDailySet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.divergencetime.service.DivTimeSysFixedCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.service.ErAlCheckService;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.SystemFixedErrorAlarm;
import nts.uk.shr.com.context.AppContexts;

/**
 * 日別計算用のエラーチェック
 * @author keisuke_hoshina
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CalculationErrorCheckServiceImpl implements CalculationErrorCheckService{
	@Inject
	/*システム固定エラーアラームに対する処理*/
	private DivTimeSysFixedCheckService divTimeSysFixedCheckService;
	
	@Inject
	private ErAlCheckService erAlCheckService;
	
	@Inject
	private AttendanceItemConvertFactory converterFactory;
	
	@Inject
	/*日別作成側に実装されていたエラーアラーム処理*/
	private DailyRecordCreateErrorAlermService dailyRecordCreateErrorAlermService;
	
	@Override
	public IntegrationOfDaily errorCheck(IntegrationOfDaily integrationOfDaily, ManagePerPersonDailySet personCommonSetting, ManagePerCompanySet master) {
		String companyID = AppContexts.user().companyId();
		List<EmployeeDailyPerError> addItemList = integrationOfDaily.getEmployeeError() == null? integrationOfDaily.getEmployeeError() :new ArrayList<>();
		List<ErrorAlarmWorkRecord> divergenceError = new ArrayList<>();
		DailyRecordToAttendanceItemConverter attendanceItemConverter = this.converterFactory.createDailyConverter().setData(integrationOfDaily);
		//勤務実績のエラーアラーム数分ループ
		for(ErrorAlarmWorkRecord errorItem : master.getErrorAlarm()) {
			//使用しない
			if(!errorItem.getUseAtr()) continue;
			//乖離系のシステムエラーかどうかチェック
			if(includeDivergence(errorItem)) { 
				divergenceError.add(errorItem);
				continue;
			}
			//システム固定
			List<EmployeeDailyPerError>  addItems = new ArrayList<>();
			if(errorItem.getFixedAtr()) {
				addItems = systemErrorCheck(integrationOfDaily,errorItem,attendanceItemConverter, master);
			}
			
			//ユーザ設定
			else {
				addItems = erAlCheckService.checkErrorFor(companyID, integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
						integrationOfDaily.getAffiliationInfor().getYmd(), errorItem, integrationOfDaily);
				//addItemList.addAll(addItems);
			}
			addItemList.addAll(addItems);
		}
		
		//乖離系のエラーはここでまとめてチェック(レスポンス対応のため)
		addItemList.addAll(divergenceErrorCheck(integrationOfDaily, master, divergenceError));
		addItemList = addItemList.stream().filter(tc -> tc != null).collect(Collectors.toList());
		integrationOfDaily.setEmployeeError(addItemList);
		return integrationOfDaily;
	}
	

	/**
	 * 乖離系のエラーアラームチェック
	 * @return　エラーアラーム一覧
	 */
	private List<EmployeeDailyPerError> divergenceErrorCheck(IntegrationOfDaily integrationOfDaily,ManagePerCompanySet master,List<ErrorAlarmWorkRecord> errorList) {
		if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {
			return divTimeSysFixedCheckService.divergenceTimeCheckBySystemFixed(AppContexts.user().companyId(), 
																	 	 		integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
																	 	 		integrationOfDaily.getAffiliationInfor().getYmd(),
																	 	 		integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getDivTime().getDivergenceTime(),
																	 	 		integrationOfDaily.getAttendanceLeave(),
																	 	 		errorList,
																	 	 		master.getDivergenceTime(),
																	 	 		master.getShareContainer());
		}
		return Collections.emptyList();
	}

	/**
	 * 該当のエラーが乖離系であるか判定する
	 * @param errorItem エラーアラーム
	 * @return　乖離系である
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
	 * システム固定エラーチェック
	 * @param attendanceItemConverter 
	 * @return 社員の日別実績エラー一覧
	 */
	public List<EmployeeDailyPerError> systemErrorCheck(IntegrationOfDaily integrationOfDaily,ErrorAlarmWorkRecord errorItem, 
			DailyRecordToAttendanceItemConverter attendanceItemConverter, ManagePerCompanySet master) {
		Optional<SystemFixedErrorAlarm> fixedErrorAlarmCode = SystemFixedErrorAlarm.getEnumFromErrorCode(errorItem.getCode().toString());
		//if(!integrationOfDaily.getAttendanceLeave().isPresent() || !fixedErrorAlarmCode.isPresent())
		if(!fixedErrorAlarmCode.isPresent())
			return Collections.emptyList();

		switch(fixedErrorAlarmCode.get()) {
			//事前残業申請超過
			case PRE_OVERTIME_APP_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
												integrationOfDaily.getAffiliationInfor().getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.PRE_OVERTIME_APP_EXCESS);
			//事前休出申請超過
			case PRE_HOLIDAYWORK_APP_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
												integrationOfDaily.getAffiliationInfor().getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.PRE_HOLIDAYWORK_APP_EXCESS);
			//事前フレックス申請超過
			case PRE_FLEX_APP_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
												integrationOfDaily.getAffiliationInfor().getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.PRE_FLEX_APP_EXCESS);
			//事前深夜申請超過
			case PRE_MIDNIGHT_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
												integrationOfDaily.getAffiliationInfor().getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.PRE_MIDNIGHT_EXCESS);
			//残業時間実績超過
			case OVER_TIME_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
												integrationOfDaily.getAffiliationInfor().getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.OVER_TIME_EXCESS);
			//休出時間実績超過
			case REST_TIME_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
												integrationOfDaily.getAffiliationInfor().getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.REST_TIME_EXCESS);
			//フレックス時間実績超過
			case FLEX_OVER_TIME:
				return integrationOfDaily.getErrorList(integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
												integrationOfDaily.getAffiliationInfor().getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.FLEX_OVER_TIME);
			//深夜時間実績超過
			case MIDNIGHT_EXCESS:
				return integrationOfDaily.getErrorList(integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
												integrationOfDaily.getAffiliationInfor().getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.MIDNIGHT_EXCESS);
			//遅刻
			case LATE:
				return integrationOfDaily.getErrorList(integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
												integrationOfDaily.getAffiliationInfor().getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.LATE);
			//早退
			case LEAVE_EARLY:
				return integrationOfDaily.getErrorList(integrationOfDaily.getAffiliationInfor().getEmployeeId(), 
												integrationOfDaily.getAffiliationInfor().getYmd(),
												fixedErrorAlarmCode.get(),
												CheckExcessAtr.LEAVE_EARLY);
			//出勤打刻漏れ
			case TIME_LEAVING_STAMP_LEAKAGE:
				return dailyRecordCreateErrorAlermService.lackOfTimeLeavingStamping(integrationOfDaily);
			//入退門漏
			case ENTRANCE_STAMP_LACKING:
				return dailyRecordCreateErrorAlermService.lackOfAttendanceGateStamping(integrationOfDaily);
			//PCログ打刻漏れ
			case PCLOG_STAMP_LEAKAGE:
				return dailyRecordCreateErrorAlermService.lackOfAttendancePCLogOnStamping(integrationOfDaily);
			//打刻順序不正
			case INCORRECT_STAMP: 
				return dailyRecordCreateErrorAlermService.stampIncorrectOrderAlgorithm(integrationOfDaily);
			//休日打刻
			case HOLIDAY_STAMP:
				val result = dailyRecordCreateErrorAlermService.checkHolidayStamp(integrationOfDaily);
				if(!result.isPresent()) {
					return Collections.emptyList();
				}
				else {
					return Arrays.asList(result.get());
				}
				
			//二重打刻
			case DOUBLE_STAMP:
				return dailyRecordCreateErrorAlermService.doubleStampAlgorithm(integrationOfDaily);
			//それ以外ルート
			default:
				return Collections.emptyList();
		}
	}
}
