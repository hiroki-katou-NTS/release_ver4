package nts.uk.ctx.at.record.dom.divergencetime.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.i18n.I18NResources;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemConvertFactory;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployee;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeEmpOfHistoryRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeOfEmployeeRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistory;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistoryRepository;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeRepository;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeUsageUnitRepository;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeValue;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryRepository;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeRepository;
import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessageRepository;
import nts.uk.ctx.at.record.dom.divergence.time.message.ErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalsituationmanagement.export.clearapprovalconfirm.ClearConfirmApprovalService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.SystemFixedErrorAlarm;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.attendance.MasterShareBus;
import nts.uk.ctx.at.shared.dom.attendance.MasterShareBus.MasterShareContainer;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.program.ProgramIdConsts;

@Stateless
public class DivTimeSysFixedCheckService {

	@Inject
	private CompanyDivergenceReferenceTimeRepository comDivRefTime;
	
	@Inject
	private AttendanceTimeRepository attendanceTimeRepo;
	
	@Inject
	private DivergenceTimeRepository diverTimeRepo;
	
	@Inject
	private ErrorAlarmWorkRecordRepository erAlConditionRepo;
	
	@Inject
	private DivergenceReferenceTimeUsageUnitRepository divRefUnitRepo;
	
	@Inject
	private WorkTypeDivergenceReferenceTimeHistoryRepository wtDivHisRepo;
	
	@Inject
	private WorkTypeDivergenceReferenceTimeRepository wtDivRefTime;
	
	@Inject
	private CompanyDivergenceReferenceTimeHistoryRepository comDivHisRepo;
	
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeaveRepo;
	
	@Inject
	private AttendanceItemConvertFactory convertHelper;
	
	@Inject
	private DivergenceTimeErrorAlarmMessageRepository divMesRepo;

	@Inject
	private WorkTypeDivergenceTimeErrorAlarmMessageRepository wtDivMesRepo;
	
	@Inject
	private IdentityProcessUseSetRepository iPSURepo;
	
//	@Inject
//	private IdentificationRepository identityRepo;
	
//	@Inject
//	private EmployeeDailyPerErrorRepository errorRepo;
//	
//	@Inject 
//	private ErrorAlarmWorkRecordRepository eaRecordRepo;
	
	@Inject
	private ApprovalProcessingUseSettingRepository approvalSettingRepo;
	
//	@Inject
//	private ApprovalStatusOfDailyPerforRepository approvalStateRepo;
	
//	@Inject
//	private AppRootStateConfirmAdapter appRootStateAdapter;
	
	@Inject
	private I18NResources resources;
	
	@Inject
	private BusinessTypeEmpOfHistoryRepository bteHisRepo;
	
	@Inject
	private BusinessTypeOfEmployeeRepository bteRepo;
	
	@Inject
	private ClearConfirmApprovalService approvalService;
	
	public final static List<String> SYSTEM_FIXED_DIVERGENCE_CHECK_CODE = Arrays.asList("D001", "D002", "D003", "D004", "D005", 
			"D006", "D007", "D008", "D009", "D010", "D011", "D012", "D013", "D014", "D015", "D016", "D017", "D018", "D019", "D020");
	
	private final static String WORKTYPE_HISTORY_ITEM = "W_HIS";
	
	private final static String COMPANY_HISTORY_ITEM = "C_HIS";
	
	private final static String WORKTYPE_CODE = "WTC";

	private static final String TIME_LEAVE_RECORD_KEY = "TIME_LEAVE_RECORD";

	private static final String CONVERTER_KEY = "CONVERTER";
	
	/** ログオフ時刻をシステム時刻として計算するかチェックする */
	private final static int LOGOFF_DIV_NO = 7;
	
	/** ・勤怠項目ID　34（退勤時刻1） */
	private final static int TIME_LEAVE_ITEM = 34;
	
	private final static String EMPTY_STRING = "";

	private final static String PATTERN_1 = "[0-9]+$";

	private final static String SEPERATOR = "|";

	private final static String RUNTIME_ERROR_1 = "勤務実績のエラーアラームのコードのフォーマットが正しくない：　";

	private final static String RUNTIME_ERROR_2 = "CompanyDivergenceReferenceTimeHistory not found!! For Company: ";

	private final static String RUNTIME_ERROR_3 = "本人確認処理の利用設定 not found!!!";

	private final static String WORK_TYPE_SETTING = "WorkTypeSetting";

	private final static String ERROR_ALARM_CHECK = "ErrorAlarmCheck";

	private final static String DIVERGENCE_MESSAGE_KEY = "DivergenceMessage";

	private final static String KDW003_108_KEY = "KDW003_108";
	
	private final static String MSG_1298_KEY = "Msg_1298";

	private final static String BUSINESS_TYPE_CODE_D = "BusinessTypeCode";

	private final static String COM_DIV_REF_TIME_KEY = "ComDivRefTime";

	private final static String WT_DIV_REF_TIME_KEY = "WTDivRefTime";

	private final static String BUSINESS_TYPE_HISTORY_KEY = "BusinessTypeOfEmployeeHistory";

	private final static String COM_DIV_REF_TIME_HIS_KEY = "CompanyDivergenceReferenceTimeHistory";

	private final static String TIME_NOW_KEY = "TimeNow";

	private final static String TODAY_KEY = "Today";

	private final static String APPROVAL_SETTING_KEY = "ApprovalSetting";

	private final static String IDENTITY_PUS_KEY = "IdentityPUS";

	private final static String DIVERGENCE_TIME_KEY = "DivergenceTime";
	
	/** 乖離時間（確認解除） */
	public List<EmployeeDailyPerError> divergenceTimeCheckBySystemFixed(String comId, String empId, GeneralDate tarD){
		List<EmployeeDailyPerError> checkR = new ArrayList<>(); 
		attendanceTimeRepo.find(empId, tarD).ifPresent(at -> {
			checkR.addAll(divergenceTimeCheckBySystemFixed(comId, empId, tarD, 
					at.getActualWorkingTimeOfDaily().getDivTime().getDivergenceTime()));
		});
		return checkR;
	}
	
	/** 乖離時間（確認解除） */
	public List<EmployeeDailyPerError> divergenceTimeCheckBySystemFixed(String comId, String empId, 
			GeneralDate tarD, List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divTime){
		return divergenceTimeCheckBySystemFixed(comId, empId, tarD, divTime, null, Optional.empty());
	}
	
	/** 乖離時間（確認解除） */
	public List<EmployeeDailyPerError> divergenceTimeCheckBySystemFixed(String comId, String empId, 
			GeneralDate tarD, List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divTime, 
			Optional<TimeLeavingOfDailyPerformance> tl){
		return divergenceTimeCheckBySystemFixed(comId, empId, tarD, divTime, null, tl);
	}
	
	/** 乖離時間（確認解除） */
	public List<EmployeeDailyPerError> divergenceTimeCheckBySystemFixed(String comId, String empId, 
			GeneralDate tarD, List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divTime, 
			Optional<TimeLeavingOfDailyPerformance> tl, List<ErrorAlarmWorkRecord> erAls){
		return divergenceTimeCheckBySystemFixed(comId, empId, tarD, divTime, null, tl, erAls);
	}
	
	/** 乖離時間（確認解除） */
	public List<EmployeeDailyPerError> divergenceTimeCheckBySystemFixed(String comId, String empId, 
			GeneralDate tarD, List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divTime, 
			Optional<TimeLeavingOfDailyPerformance> tl, List<ErrorAlarmWorkRecord> erAls,
			List<DivergenceTime> divTimeErAlMs){
		return divergenceTimeCheckBySystemFixed(comId, empId, tarD, divTime, tl, erAls, divTimeErAlMs, null);
	}
	
	/** 乖離時間（確認解除） */
	public List<EmployeeDailyPerError> divergenceTimeCheckBySystemFixed(String comId, String empId, 
			GeneralDate tarD, List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divTime, 
			Optional<TimeLeavingOfDailyPerformance> tl, List<ErrorAlarmWorkRecord> erAls,
			List<DivergenceTime> divTimeErAlMs, MasterShareContainer<String> shareContainer){
		
		List<EmployeeDailyPerError> result = divergenceTimeCheckBySystemFixed(comId, empId, tarD, divTime, null, 
				tl, erAls, divTimeErAlMs, shareContainer);
		
		return result;
	}
	
	/** 乖離時間（確認解除） */
	public List<EmployeeDailyPerError> divergenceTimeCheckBySystemFixed(String comId, String empId, 
			GeneralDate tarD, List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divTime,
			IdentityProcessUseSet iPUS, Optional<TimeLeavingOfDailyPerformance> tl){
		return divergenceTimeCheckBySystemFixed(comId, empId, tarD, divTime, iPUS, tl, null);
	}
	
	/** 乖離時間（確認解除） */
	public List<EmployeeDailyPerError> divergenceTimeCheckBySystemFixed(String comId, String empId,
			GeneralDate tarD, List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divTime, 
			IdentityProcessUseSet iPUS, Optional<TimeLeavingOfDailyPerformance> tl, 
			List<ErrorAlarmWorkRecord> erAls){
		return divergenceTimeCheckBySystemFixed(comId, empId, tarD, divTime, iPUS, tl, erAls, null);
	}
	
	/** 乖離時間（確認解除） */
	public List<EmployeeDailyPerError> divergenceTimeCheckBySystemFixed(String comId, String empId,
			GeneralDate tarD, List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divTime, 
			IdentityProcessUseSet iPUS, Optional<TimeLeavingOfDailyPerformance> tl, 
			List<ErrorAlarmWorkRecord> erAls, List<DivergenceTime> divTimeErAlMs){
		MasterShareContainer<String> shareContainer = MasterShareBus.open();
		List<EmployeeDailyPerError> result = divergenceTimeCheckBySystemFixed(comId, empId, tarD, divTime, 
				iPUS, tl, erAls, divTimeErAlMs, shareContainer);
		shareContainer.clearAll();
		return result;
	}
	
	/** 乖離時間（確認解除） */
	public List<EmployeeDailyPerError> divergenceTimeCheckBySystemFixed(String comId, String empId,
			GeneralDate tarD, List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divTime, 
			IdentityProcessUseSet iPUS, Optional<TimeLeavingOfDailyPerformance> tl, 
			List<ErrorAlarmWorkRecord> erAls, List<DivergenceTime> divTimeErAlMs,
			MasterShareContainer<String> shareContainer){
		boolean isNotShare = shareContainer == null;
		if(isNotShare){
			shareContainer = MasterShareBus.open();
		}
		List<EmployeeDailyPerError> errors = check(comId, empId, tarD, divTime, tl, erAls, divTimeErAlMs, shareContainer);
		if(errors.isEmpty()) {
			return errors;
		}
		if(iPUS == null) {
			iPUS = shareContainer.getShared(join(IDENTITY_PUS_KEY, SEPERATOR, comId), () -> iPSURepo.findByKey(comId)
														.orElseThrow(() -> new RuntimeException(RUNTIME_ERROR_3)));
		}
		/**List<EmployeeDailyPerError> result = removeconfirm(comId, empId, tarD, errors, iPUS, shareContainer);*/
		if(isNotShare){
			shareContainer.clearAll();;
		}
		return errors;
	}
	
	/** 確認解除 */
	/**
	private List<EmployeeDailyPerError> removeconfirm(String comId, String empId, GeneralDate tarD, 
			List<EmployeeDailyPerError> errors, IdentityProcessUseSet iPUS, MasterShareContainer<String> shareContainer) {
		List<EmployeeDailyPerError> divEr67 = errors.stream().filter(c -> c.getErrorAlarmWorkRecordCode() != null
				&& (c.getErrorAlarmWorkRecordCode().v().equals(SystemFixedErrorAlarm.DIVERGENCE_ERROR_6.value)
				|| c.getErrorAlarmWorkRecordCode().v().equals(SystemFixedErrorAlarm.DIVERGENCE_ERROR_7.value)
				|| c.getErrorAlarmWorkRecordCode().v().equals(SystemFixedErrorAlarm.DIVERGENCE_ERROR_1.value)
				|| c.getErrorAlarmWorkRecordCode().v().equals(SystemFixedErrorAlarm.DIVERGENCE_ERROR_2.value)
				|| c.getErrorAlarmWorkRecordCode().v().equals(SystemFixedErrorAlarm.DIVERGENCE_ERROR_3.value)
				|| c.getErrorAlarmWorkRecordCode().v().equals(SystemFixedErrorAlarm.DIVERGENCE_ERROR_4.value)
				|| c.getErrorAlarmWorkRecordCode().v().equals(SystemFixedErrorAlarm.DIVERGENCE_ERROR_5.value)
				|| c.getErrorAlarmWorkRecordCode().v().equals(SystemFixedErrorAlarm.DIVERGENCE_ERROR_9.value)
				|| c.getErrorAlarmWorkRecordCode().v().equals(SystemFixedErrorAlarm.DIVERGENCE_ERROR_10.value)
				|| c.getErrorAlarmWorkRecordCode().v().equals(SystemFixedErrorAlarm.DIVERGENCE_ERROR_8.value))
				&& c.getDate().equals(tarD) && c.getEmployeeID().equals(empId)).collect(Collectors.toList());
		
		if (!divEr67.isEmpty()) {
//			if (iPUS.isUseConfirmByYourself()) {
//				identityRepo.findByCode(empId, tarD).ifPresent(id -> {
//					removeSelfIdentity(comId, iPUS, divEr67, empId);
//				});
//			}
			Optional<ApprovalProcessingUseSetting>  approvalSet = shareContainer.getShared(join(APPROVAL_SETTING_KEY, SEPERATOR, comId),
					() -> approvalSettingRepo.findByCompanyId(comId));
			
			approvalService.clearConfirmApproval(empId, divEr67.stream().map(c -> c.getDate()).collect(Collectors.toList()), 
					approvalSet, Optional.of(iPUS));
//				if (as.getUseDayApproverConfirm() != null && as.getUseDayApproverConfirm()
//						&& as.getSupervisorConfirmErrorAtr() != null
//						&& !as.getSupervisorConfirmErrorAtr().equals(ConfirmationOfManagerOrYouself.CAN_CHECK)) {
//					approvalStateRepo.find(empId, tarD).ifPresent(asd -> {
//						 承認状態をすべてクリアする 
//						appRootStateAdapter.clearAppRootstate(asd.getRootInstanceID());
//					});
//				}
		}
		return errors;
	}*/
	
	/** 日の本人確認を解除する */
//	private void removeSelfIdentity(String comId, IdentityProcessUseSet iPUS, List<EmployeeDailyPerError> divEr67, String empId) {
//		iPUS.getYourSelfConfirmError().ifPresent(sConEr -> {
//			if (sConEr != SelfConfirmError.CAN_CONFIRM_WHEN_ERROR) {
//				// fix remove ドメインモデル「日の本人確認」を削除する Thanh
//				divEr67.stream().forEach(c -> {
//					identityRepo.remove(comId, empId, c.getDate());
//				});
//			}
//		});
//	}
	
	/** システム固定エラー：　乖離時間をチェックする */
	private List<EmployeeDailyPerError> check(String comId, String empId, GeneralDate tarD,
			List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divTime, 
			Optional<TimeLeavingOfDailyPerformance> tl, List<ErrorAlarmWorkRecord> erAls,
			List<DivergenceTime> divTimeErAlMs, MasterShareContainer<String> shareContainer) {
		boolean checkByWT = shareContainer.getShared(join(WORK_TYPE_SETTING, SEPERATOR, comId),
								() -> isCheckWithWorkType(comId));
		boolean isToday = shareContainer.getShared(TODAY_KEY, () -> GeneralDate.today()).equals(tarD);
		
		if(erAls == null || erAls.isEmpty()){
			erAls = shareContainer.getShared(join(ERROR_ALARM_CHECK, SEPERATOR, comId), 
					() -> erAlConditionRepo.getListErAlByListCode(comId, SYSTEM_FIXED_DIVERGENCE_CHECK_CODE));
		}
		if(erAls.isEmpty()) {
			return new ArrayList<>();
		}
		List<Integer> divCheckNos = erAls.stream().map(c -> getDivNo(c.getCode()))
													.distinct().collect(Collectors.toList());

		List<DivergenceTime> divTimeErAls = getDivergenceTimeErAl(comId, divCheckNos, divTimeErAlMs, shareContainer);
		if(divTimeErAls.isEmpty()) {
			return new ArrayList<>();
		}
		val historyR = getHistory(checkByWT, empId, tarD, comId, shareContainer); 
		boolean isWHis = historyR.get(WORKTYPE_HISTORY_ITEM) != null;
		DateHistoryItem  hisItem = getHisItem(historyR, isWHis);
		BusinessTypeCode bsCode = isWHis ? (BusinessTypeCode) historyR.get(WORKTYPE_CODE) : null; 
		if(hisItem == null){
			return new ArrayList<>();
		}        
		boolean isCheckToDay = AppContexts.user().employeeId().equals(empId) && isToday 
			&& ProgramIdConsts.KDW003A.equals(AppContexts.programId());

		List<DivergenceCheckResult> checkR = new ArrayList<>(); 
		shareDivRefTime(isWHis, hisItem.identifier(), divCheckNos, bsCode, shareContainer);
		shareDivMesTime(isWHis, comId, divCheckNos, bsCode, shareContainer);
		erAls.stream().sorted((e1, e2) -> e1.getCode().compareTo(e2.getCode())).forEach(erAl -> {
			int numberIn = getNumber(erAl.getCode().v()),
				divNo = getNo(numberIn);
			boolean isAlarm = numberIn % 2 == 0;
//            if(!checkR.stream().filter(cr -> getDivNo(cr.getErrorAlarmWorkRecordCode()) == divNo).findFirst().isPresent()){
            	divTimeErAls.stream().filter(d -> d.getDivergenceTimeNo() == divNo).findFirst().ifPresent(de -> {
    				divTime.stream().filter(dt -> dt.getDivTimeId() == de.getDivergenceTimeNo()).findFirst().ifPresent(dt -> {
    					// add DivResonCode to check Thanh
    					boolean isPcLogOffDiv = dt.getDivTimeId() == LOGOFF_DIV_NO && isCheckToDay;
    					
    					InternalCheckStatus status = evaluateDivTime(divNo, isAlarm, isWHis, hisItem.identifier(), bsCode, de, shareContainer, dt,
					 			getDivTimeValue(empId, tarD, tl, dt, isPcLogOffDiv, shareContainer));
    					
    					if(status != InternalCheckStatus.NO_ERROR){
    						
    						
    						checkR.add(new DivergenceCheckResult(erAl.getCode(), erAl.getErrorDisplayItem(), 
												erAl.getCancelableAtr(), status, isAlarm, divNo));
//    						checkR.add(newError(comId, empId, tarD, erAl, message));
    					}
						
    					
    				});
    			});
//            }
		});
		//TODO: comment
//		shareContainer.share("LAST_TIME_CHECK_NO", divCheckNos);
//		return checkR
		return checkR.stream().collect(Collectors.groupingBy(c -> c.no, Collectors.toList())).entrySet().stream().map(c -> {
			if(c.getValue().size() >= 2){
				DivergenceCheckResult alarm = c.getValue().stream().filter(x -> x.isAlarm).findFirst().get();
				DivergenceCheckResult error = c.getValue().stream().filter(x -> !x.isAlarm).findFirst().get();
				if(error.status == InternalCheckStatus.ERROR){
					return error;
				} else if(alarm.status == InternalCheckStatus.ERROR){
					return alarm;
				} else if(error.status == InternalCheckStatus.NO_ERROR_WITH_REASON && alarm.status != InternalCheckStatus.ERROR_CLEARED){
					return error;
				} else if(alarm.status == InternalCheckStatus.NO_ERROR_WITH_REASON && error.status != InternalCheckStatus.ERROR_CLEARED){
					return alarm;
				}
				return null;
			} 
			if(c.getValue().size() == 1){
				if(c.getValue().get(0).status == InternalCheckStatus.ERROR || c.getValue().get(0).status == InternalCheckStatus.NO_ERROR_WITH_REASON){
					return c.getValue().get(0);
				}
			}
			return null;
		}).filter(c -> c != null).map(c -> {
			boolean isPcLogOffDiv = c.no == LOGOFF_DIV_NO && isToday;
			String message = getMessage(comId, shareContainer, isWHis, bsCode, c.no, c.isAlarm,
					isPcLogOffDiv, c.status);
			return newError(comId, empId, tarD, c, message);
		}).collect(Collectors.toList());
	}

	private String getMessage(String comId, MasterShareContainer<String> shareContainer, boolean isWHis,
			BusinessTypeCode bsCode, int divNo, boolean isAlarm, boolean isPcLogOffDiv, InternalCheckStatus status) {
		if(status == InternalCheckStatus.NO_ERROR_WITH_REASON){
			return getWarning(shareContainer);
		}
		
		if(status == InternalCheckStatus.ERROR){
			return getMessage(isWHis, isPcLogOffDiv, comId, divNo, isAlarm, bsCode, shareContainer);
		}
		
		return EMPTY_STRING;
	}
	
	private String getWarning(MasterShareContainer<String> shareContainer){
		return shareContainer.getShared(MSG_1298_KEY, () -> resources.localize(MSG_1298_KEY).orElse(EMPTY_STRING));
	} 

	private int getDivNo(ErrorAlarmWorkRecordCode c) {
		return getNo(getNumber(c.v()));
	}

	private int getDivTimeValue(String empId, GeneralDate tarD, Optional<TimeLeavingOfDailyPerformance> tl,
			nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime dt, boolean isPcDivergence,
			MasterShareContainer<String> shareContainer) {
		if(isPcDivergence) {
			return calcCurrentDivergenceTime(empId, tarD, tl, shareContainer);
		} 
		return dt.getDivTimeAfterDeduction() == null ? 0 : dt.getDivTimeAfterDeduction().valueAsMinutes();
	}

	private DateHistoryItem getHisItem(Map<String, Object> historyR, boolean isWHis) {
		return (DateHistoryItem) (isWHis ? historyR.get(WORKTYPE_HISTORY_ITEM) : historyR.get(COMPANY_HISTORY_ITEM));
	}

	/** 上記の計算で求めた時間を発生した乖離時間として処理を進める */
	private int calcCurrentDivergenceTime(String employeeId, GeneralDate workingDate, 
			Optional<TimeLeavingOfDailyPerformance> timeLeave, MasterShareContainer<String> shareContainer) {
		if(!timeLeave.isPresent()){
			timeLeave = shareContainer.getShared(join(TIME_LEAVE_RECORD_KEY, SEPERATOR, employeeId, SEPERATOR, workingDate.toString()), 
					() -> timeLeaveRepo.findByKey(employeeId, workingDate));
		}
		/** 勤怠項目ID　34（退勤時刻1） */
		if(timeLeave.isPresent()) {
			val valued = shareContainer.getShared(CONVERTER_KEY, () -> convertHelper.createDailyConverter())
					.withTimeLeaving(timeLeave.get()).convert(TIME_LEAVE_ITEM);
			if(valued.isPresent() && valued.get().value() != null) {
				GeneralDateTime now = shareContainer.getShared(TIME_NOW_KEY, () -> GeneralDateTime.now());
				int currentTime = now.hours() * 60 + now.minutes();
				return currentTime - (int) valued.get().value();
			}
		}
		return 0;
	}
	
	/** 履歴項目を取得する */
	private Map<String, Object> getHistory(boolean isCheckByWT, String empId, GeneralDate tarD, String comId,
			MasterShareContainer<String> shareContainer){
		if (!isCheckByWT) {
			return getComHistory(tarD, comId, shareContainer);
		}
		
		BusinessTypeCode workTypeCode = getWorkInfo(comId, isCheckByWT, empId, tarD, shareContainer);
		if(workTypeCode == null){
			return getComHistory(tarD, comId, shareContainer);
		}
		DateHistoryItem history = shareContainer.getShared(
					join(comId, SEPERATOR, workTypeCode.toString()), () -> wtDivHisRepo.findAll(comId, workTypeCode))
										.items().stream().filter(c -> c.contains(tarD)).findFirst().orElse(null);
		if(history == null){
			return getComHistory(tarD, comId, shareContainer);
		}
		Map<String, Object> res = new HashMap<>();
		res.put(WORKTYPE_HISTORY_ITEM, history);
		res.put(WORKTYPE_CODE, workTypeCode);
		return res;
	}

	private Map<String, Object> getComHistory(GeneralDate tarD, String comId, MasterShareContainer<String> shrContainer) {
		Map<String, Object> res = new HashMap<>();
		CompanyDivergenceReferenceTimeHistory hisM = shrContainer.getShared(join(COM_DIV_REF_TIME_HIS_KEY, SEPERATOR, comId), 
																() -> comDivHisRepo.findAll(comId));
		if(hisM == null) {
			throw new RuntimeException(join(RUNTIME_ERROR_2, comId));
		}
		DateHistoryItem history = hisM.items().stream().filter(c -> c.contains(tarD)).findFirst().orElse(null);
		res.put(COMPANY_HISTORY_ITEM, history);
		return res;
	}
	
	private BusinessTypeCode getWorkInfo(String comId, boolean isGet, String empId, GeneralDate tarD,
			MasterShareContainer<String> shareContainer){
		BusinessTypeOfEmployeeHistory bteHis = !isGet ? null : shareContainer.getShared(
											join(BUSINESS_TYPE_HISTORY_KEY, SEPERATOR, comId, SEPERATOR, empId), 
													() -> bteHisRepo.findByEmployee(comId, empId).orElse(null));
		if(bteHis == null){
			return null;
		}
		return shareContainer.getShared(join(BUSINESS_TYPE_CODE_D, SEPERATOR, tarD.toString(), SEPERATOR, empId), 
							() -> getBusinessType(tarD, bteHis, shareContainer));
	}

	private BusinessTypeCode getBusinessType(GeneralDate tarD, BusinessTypeOfEmployeeHistory bteHis,
			MasterShareContainer<String> shareContainer) {
		DateHistoryItem hisItem = bteHis.getHistory().stream().filter(c -> c.contains(tarD)).findFirst().orElse(null);
		if(hisItem == null){
			return null;
		}
		BusinessTypeOfEmployee bte = shareContainer.getShared(join(BUSINESS_TYPE_CODE_D, SEPERATOR, hisItem.identifier()),
				() -> bteRepo.findByHistoryId(hisItem.identifier()).orElse(null));
		return bte == null ? null : bte.getBusinessTypeCode();
	}

	/** 「乖離時間」を取得する */
	private List<DivergenceTime> getDivergenceTimeErAl(String comId, List<Integer> divCheckNos, 
			List<DivergenceTime> divTimeErAlMs, MasterShareContainer<String> shareContainer) {
		if(divTimeErAlMs != null && !divTimeErAlMs.isEmpty()){
			return divTimeErAlMs;
		}
		return shareContainer.getShared(join(DIVERGENCE_TIME_KEY, SEPERATOR, comId), 
				() -> diverTimeRepo.getUsedDivTimeListByNoV2(comId, divCheckNos));
	}

	/** ドメインモデル「乖離基準時間利用単位」を取得する */
	private boolean isCheckWithWorkType(String companyId) {
		val divRefUnit = divRefUnitRepo.findByCompanyId(companyId).orElse(null);
		return divRefUnit == null ? false : divRefUnit.isWorkTypeUseSet();
	}
	
	/** 乖離時間のチェック */
	private InternalCheckStatus evaluateDivTime(int divNo, boolean isAlarm, boolean isCheckByWorkType, String history, 
			BusinessTypeCode bsCode, DivergenceTime divTimeEr, MasterShareContainer<String> shareContainer, 
			nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime divTime, int divergenceTime){
		if(divTime == null){
			return InternalCheckStatus.NO_ERROR;
		}
		if(history == null){
			return evaluateReasonOnNoError(divTime);
		}
		
		if(isCheckByWorkType){
			return evaluateByWorkType(divNo, history, divergenceTime, isAlarm, bsCode, divTimeEr, divTime, shareContainer);
		} else {
			return evaluateByCompany(divNo, divergenceTime, isAlarm, history, divTimeEr, divTime, shareContainer);
		}
	}
	
	private void shareDivRefTime(boolean isBussiness, String hisId, List<Integer> divNos, BusinessTypeCode bsCode, 
			MasterShareContainer<String> shareContainer){
//		List<Integer> lastNos = shareContainer.getShared("LAST_TIME_CHECK_NO", () -> new ArrayList<>());
		if(isBussiness){
			String key = join(WT_DIV_REF_TIME_KEY, SEPERATOR, hisId);
			if(!shareContainer.isShared(key)){
				shareContainer.share(key, wtDivRefTime.findByHistoryIdAndDivergenceTimeNos(bsCode, hisId, divNos));
			}
		} else {
			String key = join(COM_DIV_REF_TIME_KEY, SEPERATOR, hisId);
			if(!shareContainer.isShared(key)){
				shareContainer.share(key, comDivRefTime.findByHistoryIdAndDivergenceTimeNos(hisId, divNos));
			}
		}
	}
	
	private void shareDivMesTime(boolean isBussiness, String comId, List<Integer> divNos, BusinessTypeCode bsCode, 
			MasterShareContainer<String> shareContainer){
		if(!isBussiness){
			String key = join(DIVERGENCE_MESSAGE_KEY, SEPERATOR, comId);
			if(!shareContainer.isShared(key)){
				shareContainer.share(key, this.divMesRepo.findByDivergenceTimeNoList(new CompanyId(comId), divNos));
			}
		} else {
			String key = join(DIVERGENCE_MESSAGE_KEY, SEPERATOR, comId, SEPERATOR, bsCode.toString());
			if(!shareContainer.isShared(key)){
				shareContainer.share(key, this.wtDivMesRepo.getByDivergenceTimeNoList(divNos, new CompanyId(comId), bsCode));
			}
		}
	}

	/** 勤務種別ごとの乖離基準時間でチェックする */
	private InternalCheckStatus evaluateByWorkType(int divNo, String history, int divergenceTime, 
			boolean isAlarm, BusinessTypeCode bsCode, DivergenceTime divTimeEr, 
			nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime divTime, 
			MasterShareContainer<String> shareContainer){
		
		WorkTypeDivergenceReferenceTime divTimeBaseByWT = getWTDivRefTime(divNo, history, bsCode, shareContainer);
		
		if(divTimeBaseByWT == null || !divTimeBaseByWT.getDivergenceReferenceTimeValue().isPresent()){
			return evaluateReasonOnNoError(divTime);
		}
		
		return evaluate(divergenceTime, isAlarm, divTimeBaseByWT.getNotUseAtr() == NotUseAtr.USE, 
						divTimeBaseByWT.getDivergenceReferenceTimeValue().get(), divTimeEr, divTime);
	}

	/** 会社の履歴項目でチェックする */
	private InternalCheckStatus evaluateByCompany(int divNo,  int divergenceTime, boolean isAlarm, String history,
			DivergenceTime divTimeEr, nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime divTime, 
			MasterShareContainer<String> shareContainer){
		
		CompanyDivergenceReferenceTime divTimeBaseByCom = getComDivRefTime(divNo, history, shareContainer);
		if(divTimeBaseByCom == null || !divTimeBaseByCom.getDivergenceReferenceTimeValue().isPresent()){
			return evaluateReasonOnNoError(divTime);
		}
		
		return evaluate(divergenceTime, isAlarm, divTimeBaseByCom.getNotUseAtr() == NotUseAtr.USE,
						divTimeBaseByCom.getDivergenceReferenceTimeValue().get(), divTimeEr, divTime);
	}
	
	private WorkTypeDivergenceReferenceTime getWTDivRefTime(int divNo, String hisId, BusinessTypeCode bsCode, 
			MasterShareContainer<String> shareContainer) {
		List<WorkTypeDivergenceReferenceTime> lst = shareContainer.getShared(join(WT_DIV_REF_TIME_KEY, SEPERATOR, hisId));
		
		return lst.stream().filter(d -> d.getDivergenceTimeNo() == divNo).findFirst().orElse(null);
	}

	private CompanyDivergenceReferenceTime getComDivRefTime(int divNo, String hisId,
			MasterShareContainer<String> shareContainer) {
		List<CompanyDivergenceReferenceTime> lst = shareContainer.getShared(join(COM_DIV_REF_TIME_KEY, SEPERATOR, hisId));
		
		return lst.stream().filter(d -> d.getDivergenceTimeNo() == divNo).findFirst().orElse(null);
	}

	/** 乖離時間を判定する */
	private InternalCheckStatus evaluate(int divergenceTime, boolean isAlarm, boolean isUse, DivergenceReferenceTimeValue standard, 
			DivergenceTime divTimeEr, nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime divTime){
		if(!isUse || standard == null){
			return evaluateReasonOnNoError(divTime);
		}
		DivergenceReferenceTime sdTime = getReferTime(isAlarm, standard);
		if(sdTime != null && sdTime.v() > 0){
			if(divergenceTime >= sdTime.valueAsMinutes()) {
				// パラメータ「エラーの解除方法．乖離理由が選択された場合，エラーを解除する」をチェックする
				if(divTimeEr.getErrorCancelMedthod().isReasonSelected()) {
					
					if(isReasonSelected(divTime)) {
						return InternalCheckStatus.ERROR_CLEARED;
					}
				}
				if(divTimeEr.getErrorCancelMedthod().isReasonInputed()) {
					if(isReasonInputed(divTime)) {
						return InternalCheckStatus.ERROR_CLEARED;
					}
				}
				return InternalCheckStatus.ERROR;
			}
		}
		return evaluateReasonOnNoError(divTime);
	}
	
	private DivergenceReferenceTime getReferTime(boolean isAlarm, DivergenceReferenceTimeValue standard){
		if(isAlarm && standard.getAlarmTime().isPresent()){
			return standard.getAlarmTime().get();
		}
		return standard.getErrorTime().orElse(null);
	}

	private boolean isReasonSelected(nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime divTime) {
		return divTime.getDivResonCode() != null && divTime.getDivResonCode().isPresent() && !divTime.getDivResonCode().get().v().isEmpty();
	}

	private boolean isReasonInputed(nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime divTime) {
		return divTime.getDivReason() != null && divTime.getDivReason().isPresent() && !divTime.getDivReason().get().v().isEmpty();
	}
	
	private InternalCheckStatus evaluateReasonOnNoError(nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime divTime) {
		if(isReasonInputed(divTime) || isReasonSelected(divTime)){
			
			return InternalCheckStatus.NO_ERROR_WITH_REASON;
		}
		
		return InternalCheckStatus.NO_ERROR;
	}

	/** ドメインモデル「勤務種別ごとの乖離時間のエラーアラームメッセージ」を取得する */
	private String getMessage(boolean isByWt, boolean isWithBonusText, String comId, int divNo, 
			boolean isAlarm, BusinessTypeCode wtCode, MasterShareContainer<String> shareContainer) {
		ErrorAlarmMessage message = null;
		if(!isByWt) {
			List<DivergenceTimeErrorAlarmMessage> mesL = shareContainer.getShared(join(DIVERGENCE_MESSAGE_KEY, SEPERATOR, comId));
			DivergenceTimeErrorAlarmMessage mes = mesL.stream().filter(
					c -> c.getDivergenceTimeNo() == divNo).findFirst().orElse(null);
			if(mes != null) {
				message = isAlarm ? mes.getAlarmMessage().orElse(null) : mes.getErrorMessage().orElse(null);
			}
		} else {
			List<WorkTypeDivergenceTimeErrorAlarmMessage>  mesL = shareContainer.getShared(
					join(DIVERGENCE_MESSAGE_KEY, SEPERATOR, comId, SEPERATOR, wtCode.toString()));
			WorkTypeDivergenceTimeErrorAlarmMessage mes = mesL.stream().filter(
					c -> c.getDivergenceTimeNo() == divNo).findFirst().orElse(null);
			if(mes != null) {
				message = isAlarm ? mes.getAlarmMessage().orElse(null) : mes.getErrorMessage().orElse(null);
			}
		}
		if(message != null) {
			return !isWithBonusText ? message.v() : join(message.v(), 
					shareContainer.getShared(KDW003_108_KEY, () -> resources.localize(KDW003_108_KEY).orElse(EMPTY_STRING)));
		}
		return EMPTY_STRING;
	}
	
	private String join(String... values){
		return StringUtils.join(values);
	}

	private int getNo(int numberIn) {
		return (numberIn + 1) / 2;
	}
	
	private int getNumber(String code){
		String number = code.replace(code.replaceAll(PATTERN_1, EMPTY_STRING), EMPTY_STRING);
		if(number.isEmpty()){ throw new RuntimeException(RUNTIME_ERROR_1 + code); }
		return Integer.parseInt(number);
	}

	private EmployeeDailyPerError newError(String comId, String empId, GeneralDate tarD, DivergenceCheckResult erAl, String mes) {
		return new EmployeeDailyPerError(comId, empId, tarD, erAl.errorCode, 
										erAl.displayItem == null ? new ArrayList<>() : Arrays.asList(erAl.displayItem), 
										erAl.cancelable ? 1 : 0, mes);
	}
	
	@AllArgsConstructor
	private class DivergenceCheckResult {
		ErrorAlarmWorkRecordCode errorCode;
		Integer displayItem;
		Boolean cancelable;
		InternalCheckStatus status;
		boolean isAlarm;
		Integer no;
	}
	
	private enum InternalCheckStatus {
		
		/**　エラー*/
		ERROR,
		/**　正常　*/
		NO_ERROR,
		/**　正常（理由入力）*/
		NO_ERROR_WITH_REASON,
		/**　エラー（理由入力あり）*/
		ERROR_CLEARED;
	}
	
/** 日の本人確認を登録する */
//private void registryIdentity(String companyId, IdentityProcessUseSet identityPUS, SelfConfirmContentRegistry selfConfirm) {
//	if(!identityPUS.getYourSelfConfirmError().isPresent()) {
//		return;
//	}
//	if(identityPUS.getYourSelfConfirmError().get() != SelfConfirmError.CAN_CONFIRM_WHEN_ERROR) {
//		List<EmployeeDailyPerError> errors = new ArrayList<>();
//		errors.addAll(selfConfirm.content.stream().map(c -> errorRepo.find(selfConfirm.empId, c.ymd))
//				.flatMap(List::stream).collect(Collectors.toList()));
//		if(!errors.isEmpty()) {
//			List<ErrorAlarmWorkRecord> eaRecords = eaRecordRepo.getListErAlByListCodeError(companyId, 
//					errors.stream().map(c -> c.getErrorAlarmWorkRecordCode().v()).collect(Collectors.toList()));
//			if(!eaRecords.isEmpty()) {
//				// fix remove ドメインモデル「日の本人確認」を削除する Thanh
//				selfConfirm.content.stream().filter(c -> !c.confirmStatus).forEach(c -> {
//					identityRepo.remove(companyId, selfConfirm.empId, c.ymd);
//				});
//				GeneralDate today = GeneralDate.today();
//				selfConfirm.content.stream().filter(c -> c.confirmStatus).forEach(c -> {
//					identityRepo.insert(new Identification(companyId, selfConfirm.empId, c.ymd, today));
//				});
//				return;
//			}
//		}
//	}
//}
}
