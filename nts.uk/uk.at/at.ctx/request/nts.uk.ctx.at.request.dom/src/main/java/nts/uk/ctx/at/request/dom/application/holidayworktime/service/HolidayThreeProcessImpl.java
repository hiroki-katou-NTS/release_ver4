package nts.uk.ctx.at.request.dom.application.holidayworktime.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.frame.OvertimeInputCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInputRepository;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeCheckResult;
import nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime;
import nts.uk.ctx.at.request.dom.application.overtime.service.IOvertimePreProcess;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.CalcStampMiss;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.OverrideSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.WithdrawalAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.WithdrawalAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.AppDateContradictionAtr;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetting;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrameRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;

@Stateless
public class HolidayThreeProcessImpl implements HolidayThreeProcess {
	@Inject
	private HolidayWorkInputRepository holidayWorkInputRepository;
	@Inject
	private ApplicationRepository_New appRepository;
	@Inject
	private OvertimeRestAppCommonSetRepository overtimeRestAppCommonSetRepository;
	@Inject
	private WorkTypeRepository workTypeRep;
	@Inject
	private IOvertimePreProcess iOvertimePreProcess;
	@Inject
	private RecordWorkInfoAdapter recordWorkInfoAdapter;
	@Inject
	private PredetemineTimeSettingRepository workTimeSetRepository;
	@Inject
	private WithdrawalAppSetRepository withdrawalAppSetRepository;
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	@Inject
	private WorkdayoffFrameRepository breaktimeFrameRep;
	
	// 03-02_実績超過チェック
	@Override
	public CaculationTime checkCaculationActualExcess(int prePostAtr, int appType, String employeeID,
			String companyID, GeneralDate appDate,
			CaculationTime breakTime, String siftCD,Integer calTime, boolean isCalculator) {
		if(!checkCodition(prePostAtr,companyID, isCalculator)){
			return breakTime;
		}
		String employeeName = employeeAdapter.getEmployeeName(employeeID);
		//Imported(申請承認)「勤務実績」を取得する
		RecordWorkInfoImport recordWorkInfoImport = recordWorkInfoAdapter.getRecordWorkInfo(employeeID,appDate);
		Optional<PredetemineTimeSetting> workTimeSetOtp = workTimeSetRepository.findByWorkTimeCode(companyID, siftCD);
		if (workTimeSetOtp.isPresent()) {
			PredetemineTimeSetting workTimeSet = workTimeSetOtp.get();
			
			if(iOvertimePreProcess.checkTimeDay(appDate.toString("yyyy/MM/dd"),workTimeSet)){
				// 03-02-3_当日の場合
				breakTime = checkHolidayWorkOnDay(companyID, employeeID, appDate.toString("yyyy/MM/dd"), siftCD, breakTime,recordWorkInfoImport,calTime, employeeName);
			}else{
				// 03-02-2_当日以外の場合
				breakTime = this.checkOutSideTimeTheDay(companyID, employeeID, appDate.toString("yyyy/MM/dd"), siftCD, breakTime,recordWorkInfoImport,calTime,employeeName);
			}
		}
		return breakTime;
	}
	// 03-02-2_当日以外の場合
	@Override
	public CaculationTime checkOutSideTimeTheDay(String companyID, String employeeID, String appDate,
			String siftCD, CaculationTime breakTime,
			RecordWorkInfoImport recordWorkInfoImport,Integer calTime,String employeeName) {
		Optional<WorkType> workType = workTypeRep.findByPK(companyID, recordWorkInfoImport.getWorkTypeCode());
		if(workType.isPresent()){
			if(workType.get().getDailyWork().isHolidayWork()){
				// 03-02-2-1_当日以外_休日出勤の場合
				this.checkOutSideTimeTheDayForHoliday(companyID, employeeID, appDate, siftCD, breakTime, recordWorkInfoImport, calTime,employeeName);
			}else{
				// 03-02-2-2_当日以外_休日の場合
				this.checkOutSideTimeTheDayNoForHoliday(companyID, employeeID, appDate, siftCD, breakTime, recordWorkInfoImport, calTime,employeeName);
			}
		}
		return breakTime;
	}
	/* (non-Javadoc)
	 * 03-02-2-1_当日以外_休日出勤の場合
	 * @see nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidayThreeProcess#checkOutSideTimeTheDayForHoliday(java.lang.String, java.lang.String, java.lang.String, nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting, java.lang.String, java.util.List, nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport)
	 */
	@Override
	public CaculationTime checkOutSideTimeTheDayForHoliday(String companyID, String employeeID, String appDate,
			String siftCD, CaculationTime breakTime,
			RecordWorkInfoImport recordWorkInfoImport,Integer calTime,String employeeName) {
		if(recordWorkInfoImport != null){
			//打刻漏れチェック
			if(recordWorkInfoImport.getAttendanceStampTimeFirst() != null && recordWorkInfoImport.getLeaveStampTimeFirst() != null){
				//就業時間帯チェック
				 if(siftCD.equals(recordWorkInfoImport.getWorkTimeCode())){
					 //Imported(申請承認)「実績内容」.就業時間帯コード=画面上の就業時間帯
					 checkTimeThanCalTimeReally(breakTime,recordWorkInfoImport,employeeName,appDate,companyID);
				 }else{
					 //Imported(申請承認)「実績内容」.就業時間帯コード != 画面上の就業時間帯
					 //画面上の申請時間＞Imported(申請承認)「計算休出時間」.休出時間
					if(breakTime.getApplicationTime() != null && breakTime.getApplicationTime() > calTime){
						Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
						throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "");
					}else{
						breakTime.setCaculationTime(calTime != null ? Integer.toString(calTime) : null);
					}
				 }
			}else{
				Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
				throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "");
			}
		}
		return breakTime;
	}
	
	/* (non-Javadoc)
	 * 03-02-2-2_当日以外_休日の場合
	 * @see nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidayThreeProcess#checkOutSideTimeTheDayNoForHoliday(java.lang.String, java.lang.String, java.lang.String, java.lang.String, nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime, nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport, java.lang.Integer)
	 */
	@Override
	public CaculationTime checkOutSideTimeTheDayNoForHoliday(String companyID, String employeeID, String appDate,
			String siftCD, CaculationTime breakTime, RecordWorkInfoImport recordWorkInfoImport, Integer calTime,String employeeName) {
		if(recordWorkInfoImport != null){
			//打刻漏れチェック
			if(recordWorkInfoImport.getAttendanceStampTimeFirst() != null && recordWorkInfoImport.getLeaveStampTimeFirst() != null){
				 //Imported(申請承認)「実績内容」.就業時間帯コード != 画面上の就業時間帯
				 //画面上の申請時間＞Imported(申請承認)「計算休出時間」.休出時間
				if(breakTime.getApplicationTime() != null && breakTime.getApplicationTime() > calTime){
					Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
					throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "",
							String.valueOf(breakTime.getFrameNo()), String.valueOf(breakTime.getErrorCode()));
				}else{
					breakTime.setCaculationTime(calTime != null ? Integer.toString(calTime) : null);
				}
			}else{
				Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
				throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "");
			}
		}
		return breakTime;
	}
	/* (non-Javadoc)
	 * 03-02-3_当日の場合
	 * @see nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidayThreeProcess#checkHolidayWorkOnDay(java.lang.String, java.lang.String, java.lang.String, nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting, java.lang.String, java.util.List, nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport)
	 */
	@Override
	public CaculationTime checkHolidayWorkOnDay(String companyID, String employeeID, String appDate,
			String siftCD, CaculationTime breakTime,
			RecordWorkInfoImport recordWorkInfoImport,Integer calTime,String employeeName) {
		Optional<WorkType> workType = workTypeRep.findByPK(companyID, recordWorkInfoImport.getWorkTypeCode());
		if(workType.isPresent()){
			if(workType.get().getDailyWork().isHolidayWork()){
				// 03-02-3-1_当日_休日出勤の場合 
				this.checkOnDayTheDayForHolidayWork(companyID, employeeID, appDate, siftCD, breakTime, recordWorkInfoImport, calTime,employeeName);
			}else{
				//03-02-3-2_当日_休日の場合
				this.checkOutSideTimeTheDayNoForHoliday(companyID, employeeID, appDate, siftCD, breakTime, recordWorkInfoImport, calTime,employeeName);
			}
		}
		return breakTime;
	}
	// 03-02-3-1_当日_休日出勤の場合
	@Override
	public void checkOnDayTheDayForHolidayWork(String companyID, String employeeID, String appDate, String siftCD,
			CaculationTime breakTime, RecordWorkInfoImport recordWorkInfoImport, Integer calTime,String employeeName) {
		// ドメインモデル「休出申請設定」を取得
		Optional<WithdrawalAppSet> withdrawalApp =	withdrawalAppSetRepository.getWithDraw();
		if(recordWorkInfoImport != null){
			//打刻漏れチェック
			if(recordWorkInfoImport.getAttendanceStampTimeFirst() != null && recordWorkInfoImport.getLeaveStampTimeFirst() != null){
				if(withdrawalApp.isPresent() && withdrawalApp.get().getOverrideSet().equals(OverrideSet.TIME_OUT_PRIORITY)){
					if(siftCD != null && siftCD.equals(recordWorkInfoImport.getWorkTimeCode())){
						this.checkTimeThanCalTimeReally(breakTime, recordWorkInfoImport,employeeName,appDate,companyID);
					}
				}
				// Imported(申請承認)「実績内容」.就業時間帯コード != 画面上の就業時間帯
				// 画面上の申請時間＞Imported(申請承認)「計算休出時間」.休出時間
				if (breakTime.getApplicationTime() != null && breakTime.getApplicationTime() > calTime) {
					Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
					throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "");
				} else {
					// 
					breakTime.setCaculationTime(calTime != null ? Integer.toString(calTime) : null);
				}
			}else{
				if(withdrawalApp.isPresent() && withdrawalApp.get().getCalStampMiss().equals(CalcStampMiss.CAN_NOT_REGIS)){
					Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
					throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "");
				}else{
					if (breakTime.getApplicationTime() != null && breakTime.getApplicationTime() > calTime) {
						//画面上の申請時間＞Imported(申請承認)「計算休出時間」.休出時間
						Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
						throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "");
					} else {
						//画面上の申請時間＜＝Imported(申請承認)「計算休出時間」.休出時間
						breakTime.setCaculationTime(calTime != null ? Integer.toString(calTime) : null);
					}
				}
			}
		}
		
	}
	// 03-02-3-2_当日_休日の場合
	@Override
	public void checkDayIsHoliday(String companyID, String employeeID, String appDate, String siftCD,
			CaculationTime breakTime, RecordWorkInfoImport recordWorkInfoImport, Integer calTime,String employeeName) {
		// ドメインモデル「休出申請設定」を取得
		Optional<WithdrawalAppSet> withdrawalApp =	withdrawalAppSetRepository.getWithDraw();
		if(recordWorkInfoImport.getAttendanceStampTimeFirst() != null && recordWorkInfoImport.getLeaveStampTimeFirst() != null){
			//出勤、退勤打刻あり
			if (breakTime.getApplicationTime() != null && breakTime.getApplicationTime() > calTime) {
				//画面上の申請時間＞Imported(申請承認)「計算休出時間」.休出時間
				Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
				throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "");
			} else {
				//画面上の申請時間＜＝Imported(申請承認)「計算休出時間」.休出時間
				breakTime.setCaculationTime(calTime != null ? Integer.toString(calTime) : null);
			}
			
		}else{
			if(withdrawalApp.isPresent() && withdrawalApp.get().getCalStampMiss().equals(CalcStampMiss.CAN_NOT_REGIS)){
				Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
				throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "");
			}else{
				if ( breakTime.getApplicationTime() != null &&  breakTime.getApplicationTime() > calTime) {
					//画面上の申請時間＞Imported(申請承認)「計算休出時間」.休出時間
					Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
					throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "");
				} else {
					//画面上の申請時間＜＝Imported(申請承認)「計算休出時間」.休出時間
					breakTime.setCaculationTime(calTime != null ? Integer.toString(calTime) : null);
				}
			}
		}
		
	}

	/* (non-Javadoc)
	 * 03-01_事前申請超過チェック
	 * @see nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidayThreeProcess#preApplicationExceededCheck(java.lang.String, nts.arc.time.GeneralDate, nts.arc.time.GeneralDateTime, nts.uk.ctx.at.request.dom.application.PrePostAtr, int, java.util.List)
	 */
	@Override
	public OvertimeCheckResult preApplicationExceededCheck(String companyId, GeneralDate appDate,
			GeneralDateTime inputDate, PrePostAtr prePostAtr, int attendanceId, List<HolidayWorkInput> holidayWorkInputs, String employeeID) {
		String employeeName = employeeAdapter.getEmployeeName(employeeID);
		OvertimeCheckResult result = new OvertimeCheckResult();
		result.setFrameNo(-1);
		// 社員ID
		// String EmployeeId = AppContexts.user().employeeId();
		// チェック条件を確認
		if (!this.confirmCheck(companyId, prePostAtr)) {
			result.setErrorCode(0);
			return result;
		}
		// ドメインモデル「申請」を取得
		// 事前申請漏れチェック
		List<Application_New> beforeApplication = appRepository.getBeforeApplication(companyId, employeeID, appDate, inputDate,
				ApplicationType.BREAK_TIME_APPLICATION.value, PrePostAtr.PREDICT.value);
		if (beforeApplication.isEmpty()) {
			throw new BusinessException("Msg_1508",employeeName);
		}
		// 事前申請否認チェック
		// 否認以外：
		// 反映情報.実績反映状態＝ 否認、差し戻し
		ReflectedState_New refPlan = beforeApplication.get(0).getReflectionInformation().getStateReflectionReal();
		if (refPlan.equals(ReflectedState_New.DENIAL) || refPlan.equals(ReflectedState_New.REMAND)) {
			// 背景色を設定する
			throw new BusinessException("Msg_1508",employeeName);
		}
		String beforeCid = beforeApplication.get(0).getCompanyID();
		String beforeAppId = beforeApplication.get(0).getAppID();

		// 事前申請の申請時間
		List<HolidayWorkInput> beforeOvertimeInputs = holidayWorkInputRepository.getHolidayWorkInputByAppID(beforeCid, beforeAppId)
				.stream()
				.filter(item -> item.getAttendanceType() == EnumAdaptor.valueOf(attendanceId, AttendanceType.class))
				.collect(Collectors.toList());
		if (beforeOvertimeInputs.isEmpty()) {
			result.setErrorCode(0);
			return result;
		}
		// 残業時間１～１０、加給時間
		// すべての残業枠をチェック
		for (int i = 0; i < holidayWorkInputs.size(); i++) {
			HolidayWorkInput afterTime = holidayWorkInputs.get(i);
			int frameNo = afterTime.getFrameNo();
			Optional<HolidayWorkInput> beforeTimeOpt = beforeOvertimeInputs.stream()
					.filter(item -> item.getFrameNo() == frameNo).findFirst();
			if (!beforeTimeOpt.isPresent()) {
				continue;
			}
			HolidayWorkInput beforeTime = beforeTimeOpt.get();
			if (null == beforeTime) {
				continue;
			}
			// 事前申請の申請時間＞事後申請の申請時間
			if (afterTime.getApplicationTime()!= null && beforeTime.getApplicationTime().v() < afterTime.getApplicationTime().v()) {
				// 背景色を設定する
				Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyId), frameNo);
				throw new BusinessException("Msg_424",employeeName, workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "",
						"", String.valueOf(frameNo), String.valueOf(1));
			}
		}
		result.setErrorCode(0);
		return result;
	}
	/**
	 * チェック条件
	 *  03-01-1_チェック条件
	 * @return True：チェックをする, False：チェックをしない
	 */
	private boolean confirmCheck(String companyId, PrePostAtr prePostAtr) {
		// 事前事後区分チェック
		if (prePostAtr.equals(PrePostAtr.PREDICT)) {
			return false;
		}
		// ドメインモデル「残業休出申請共通設定」を取得
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet = this.overtimeRestAppCommonSetRepository
				.getOvertimeRestAppCommonSetting(companyId, ApplicationType.BREAK_TIME_APPLICATION.value);
		if (overtimeRestAppCommonSet.isPresent()) {
			// 残業休出申請共通設定.事前表示区分＝表示する  
			if (overtimeRestAppCommonSet.get().getPreExcessDisplaySetting().equals(UseAtr.USE)) {
				// 表示する:Trueを返す
				return true;
			}
		}
		return false;
	}

	

	
	// 03-02-1_チェック条件
	@Override
	public boolean checkCodition(int prePostAtr, String companyID, boolean isCalculator) {
		if(prePostAtr == PrePostAtr.POSTERIOR.value){
			Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSetting = overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.BREAK_TIME_APPLICATION.value);
			if(overtimeRestAppCommonSetting.isPresent()){
				if(isCalculator){
					//ドメインモデル「残業休出申請共通設定」.実績表示区分チェック
					if(overtimeRestAppCommonSetting.get().getPerformanceDisplayAtr().value == UseAtr.USE.value){
						return true;
					}
				} else {
					//休出の事前申請よりも超過している場合確認メッセージを表示するかどうかの区分
					if((overtimeRestAppCommonSetting.get().getPerformanceExcessAtr() == AppDateContradictionAtr.CHECKNOTREGISTER)||
							(overtimeRestAppCommonSetting.get().getPerformanceExcessAtr() == AppDateContradictionAtr.CHECKREGISTER)){
						return true;
					}
				}
			}
		}
		return false;
	}
	private void checkTimeThanCalTimeReally(CaculationTime breakTime,
			RecordWorkInfoImport recordWorkInfoImport,String employeeName,String appDate,String companyID){
		for(OvertimeInputCaculation holidayCal : recordWorkInfoImport.getOvertimeHolidayCaculation()){
			 if(holidayCal.getFrameNo() == breakTime.getFrameNo()){
				 //画面上の申請時間＞Imported(申請承認)「実績内容」.休出時間
				 if(breakTime.getApplicationTime() != null &&  breakTime.getApplicationTime() > holidayCal.getResultCaculation()){
					 Optional<WorkdayoffFrame> workDayoffFrame = breaktimeFrameRep.findWorkdayoffFrame(new CompanyId(companyID), breakTime.getFrameNo());
						throw new BusinessException("Msg_423",employeeName,appDate,workDayoffFrame.isPresent() ? workDayoffFrame.get().getWorkdayoffFrName().toString() : "");
				 }else{
					 breakTime.setCaculationTime(holidayCal.getResultCaculation() != null ? Integer.toString(holidayCal.getResultCaculation()) : null);
				 }
			 }
		 }
	}
	@Override
	public CaculationTime checkCaculationActualExcessForApprover(int prePostAtr, int appType, String employeeID,
			String companyID, GeneralDate appDate, CaculationTime breakTimeInput, String siftCD, Integer calTime, boolean isCalculator) {
		// 03-02-1_チェック条件
		if(!this.checkCodition(prePostAtr, companyID, isCalculator)){
			return breakTimeInput;
		}
		//Imported(申請承認)「勤務実績」を取得する
		//RecordWorkInfoImport recordWorkInfoImport = recordWorkInfoAdapter.getRecordWorkInfo(employeeID,appDate);
		if(breakTimeInput.getApplicationTime() != null && breakTimeInput.getApplicationTime() > 0){
			breakTimeInput.setErrorCode(2);
		}
		breakTimeInput.setCaculationTime(calTime != null ? Integer.toString(calTime) : null);
		return breakTimeInput;
	}
	
	
}
