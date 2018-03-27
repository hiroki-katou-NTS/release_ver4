package nts.uk.ctx.at.request.dom.application.overtime.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.frame.OvertimeInputCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.IErrorCheckBeforeRegister;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeCheckResult;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeInputRepository;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
@Stateless
public class OvertimeSixProcessImpl implements OvertimeSixProcess{
	final String DATE_FORMAT = "yyyy/MM/dd";
	@Inject
	private IErrorCheckBeforeRegister IErrorCheckBeforeRegister;
	@Inject
	private OvertimeRestAppCommonSetRepository overtimeRestAppCommonSetRepository;
	@Inject
	private ApplicationRepository_New applicationRepository;
	@Inject
	private OvertimeRepository overtimeRepository;
	@Inject
	private OvertimeInputRepository overtimeInputRepository;
	@Inject
	private RecordWorkInfoAdapter recordWorkInfoAdapter;
	@Inject
	private PredetemineTimeSettingRepository workTimeSetRepository;
	@Inject
	private IOvertimePreProcess overtimePreProcess;
	
	/* 
	 * 06-01_色表示チェック
	 * @see nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeSixProcess#checkDisplayColor(java.util.List, java.util.List, int, nts.arc.time.GeneralDateTime, nts.arc.time.GeneralDate, int, java.lang.String, java.lang.String, nts.uk.ctx.at.request.dom.setting.requestofeach.RequestAppDetailSetting)
	 */
	@Override
	public List<CaculationTime> checkDisplayColor(List<CaculationTime> overTimeInputs,
			List<OvertimeInputCaculation> overtimeInputCaculations,int prePostAtr,GeneralDateTime inputDate,GeneralDate appDate, int appType,String employeeID,String companyID,ApprovalFunctionSetting approvalFunctionSetting,String siftCD) {
		for(CaculationTime overtimeInput : overTimeInputs ){
			for(OvertimeInputCaculation overtimeInputCaculation : overtimeInputCaculations){
					if(overtimeInput.getFrameNo() == overtimeInputCaculation.getFrameNo()){
						if(overtimeInput.getApplicationTime() != null && overtimeInput.getApplicationTime() != overtimeInputCaculation.getResultCaculation()){
							overtimeInput.setErrorCode(3); // 色定義名：計算値
						}
						if(overtimeInputCaculation.getResultCaculation()!= null && overtimeInputCaculation.getResultCaculation() == 0){
							continue;
						}else if(overtimeInputCaculation.getResultCaculation()!= null && overtimeInputCaculation.getResultCaculation() > 0){
							// 03-01_事前申請超過チェック
							OvertimeCheckResult overtimeCheckResult = this.IErrorCheckBeforeRegister.preApplicationExceededCheck(overtimeInput.getCompanyID(),appDate, inputDate, EnumAdaptor.valueOf(prePostAtr, PrePostAtr.class), overtimeInputCaculation.getAttendanceID(), convert(overtimeInput));
							overtimeInput.setErrorCode(overtimeCheckResult.getErrorCode());
							// 06-04_計算実績超過チェック
							List<CaculationTime> caculations = new ArrayList<>();
							caculations.add(overtimeInput);
							List<OvertimeInputCaculation> overtimeCals = new ArrayList<>();
							overtimeCals.add(overtimeInputCaculation);
							overtimeInput.setErrorCode(checkCaculationActualExcess(prePostAtr,appType,employeeID,companyID,approvalFunctionSetting,appDate,caculations,siftCD,overtimeCals).getErrorCode());
						}
					}
			}
		}
		return overTimeInputs;
		
	}
	
	/* 
	 *  06-04_計算実績超過チェック
	 * @see nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeSixProcess#checkCaculationActualExcess(int, int, java.lang.String, java.lang.String, nts.uk.ctx.at.request.dom.setting.requestofeach.RequestAppDetailSetting)
	 */
	@Override
	public CaculationTime checkCaculationActualExcess(int prePostAtr,int appType,String employeeID,String companyID,ApprovalFunctionSetting approvalFunctionSetting,GeneralDate appDate, List<CaculationTime> overTimeInputs,String siftCD,List<OvertimeInputCaculation> overtimeInputCaculations) {
		boolean condition = checkCondition(prePostAtr,appType,companyID);
		if(condition){
			//Imported(申請承認)「勤務実績」を取得する
			RecordWorkInfoImport recordWorkInfoImport = recordWorkInfoAdapter.getRecordWorkInfo(employeeID,appDate);
			Optional<PredetemineTimeSetting> workTimeSetOtp = workTimeSetRepository.findByWorkTimeCode(companyID, siftCD);
			if (workTimeSetOtp.isPresent()) {
				PredetemineTimeSetting workTimeSet = workTimeSetOtp.get();
				
				if(overtimePreProcess.checkTimeDay(appDate.toString("yyyy/MM/dd"),workTimeSet)){
					// 06-04-3_当日の場合
					overTimeInputs = checkDuringTheDay(companyID, employeeID, appDate.toString("yyyy/MM/dd"), approvalFunctionSetting, siftCD, overTimeInputs,recordWorkInfoImport,overtimeInputCaculations);
				}else{
					// 06-04-2_当日以外の場合
					overTimeInputs = this.checkOutSideTimeTheDay(companyID, employeeID, appDate.toString("yyyy/MM/dd"), approvalFunctionSetting, siftCD, overTimeInputs,recordWorkInfoImport,overtimeInputCaculations);
				}
			}
		}
		return new CaculationTime(overTimeInputs.get(0).getCompanyID(),
				overTimeInputs.get(0).getAppID(),
				overTimeInputs.get(0).getAttendanceID(),
				overTimeInputs.get(0).getFrameNo(),
				overTimeInputs.get(0).getTimeItemTypeAtr(),
				overTimeInputs.get(0).getFrameName(), 
				overTimeInputs.get(0).getApplicationTime(),
				overTimeInputs.get(0).getPreAppTime(),
				overTimeInputs.get(0).getCaculationTime(),
				overTimeInputs.get(0).getErrorCode(),true);
	}
	/* 
	 * 06-04-1_チェック条件
	 * @see nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeSixProcess#checkCondition(int, int, java.lang.String)
	 */
	@Override
	public boolean checkCondition(int prePostAtr,int appType,String companyID) {
		if(prePostAtr == PrePostAtr.POSTERIOR.value){
			Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSetting = overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, appType);
			if(overtimeRestAppCommonSetting.isPresent()){
				//ドメインモデル「残業休出申請共通設定」.実績表示区分チェック
				if(overtimeRestAppCommonSetting.get().getPerformanceDisplayAtr().value == UseAtr.USE.value){
					return true;
				}
			}
		}
		return false;
	}

	/* 
	 * 06-02_残業時間を取得
	 * @see nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeSixProcess#getCaculationOvertimeHours(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<CaculationTime> getCaculationOvertimeHours(String companyID, String employeeId, String appDate,
			int appType,List<CaculationTime> overtimeHours,List<OvertimeInputCaculation> overtimeInputCaculations) {
		
		/* 01-10_0時跨ぎチェック
		* TODO
		*/
		// 06-02-1_事前申請を取得
		overtimeHours = getAppOvertimeHoursPre(companyID,employeeId,appDate,appType,overtimeHours);
		
		/* 06-02-2_申請時間を取得
		* TODO
		*/
		getAppOvertimeCaculation(overtimeHours,overtimeInputCaculations);
		return overtimeHours;
	}
	/* 
	 * 06-02-1_事前申請を取得
	 * @see nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeSixProcess#getAppOvertimeHoursPre(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<CaculationTime> getAppOvertimeHoursPre(String companyID,String employeeId, String appDate,int appType,List<CaculationTime> overtimeHours) {
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSetting = overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, appType);
		if(overtimeRestAppCommonSetting.isPresent()){
			if(overtimeRestAppCommonSetting.get().getPreDisplayAtr().value == UseAtr.USE.value){
				List<Application_New> application = this.applicationRepository.getApp(employeeId,  GeneralDate.fromString(appDate, DATE_FORMAT), PrePostAtr.PREDICT.value, appType);
				if(application.size() > 0){
					Optional<AppOverTime> appOvertime = this.overtimeRepository.getAppOvertime(application.get(0).getCompanyID(), application.get(0).getAppID());
					if(appOvertime.isPresent()){
						List<OverTimeInput> overtimeInputs = overtimeInputRepository.getOvertimeInputByAttendanceId(appOvertime.get().getCompanyID(), appOvertime.get().getAppID(),AttendanceType.NORMALOVERTIME.value);
						overtimeHours = convertCaculation(overtimeInputs,overtimeHours);
					}
				}
			}
		}
		return overtimeHours;
	}
	/* 06-02-2_申請時間を取得 */
	@Override
	public List<CaculationTime> getAppOvertimeCaculation(List<CaculationTime> caculationTimes,List<OvertimeInputCaculation> overtimeInputCaculations) {
	
		for(CaculationTime caculationTime : caculationTimes){
			for(OvertimeInputCaculation caculation :overtimeInputCaculations){
				if(caculationTime.getFrameNo() == caculation.getFrameNo()){
					caculationTime.setApplicationTime(caculation.getResultCaculation());
				}
			}
		}
		return caculationTimes;
	}

	/* 
	 * 06-03_加給時間を取得
	 * @see nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeSixProcess#getCaculationBonustime(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<CaculationTime> getCaculationBonustime(String companyID, String employeeId, String appDate, int appType,List<CaculationTime> caculationTimes) {
		
		// 06-03-1_加給事前申請を取得
		List<CaculationTime> overtimeInputs = getAppBonustimePre(companyID,employeeId,appDate, appType,caculationTimes);
		return overtimeInputs;
		// 06-03-2_加給計算時間を取得
		// TODO
	}
	/* 
	 * 06-03-1_加給事前申請を取得
	 * @see nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeSixProcess#getAppBonustimePre(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<CaculationTime> getAppBonustimePre(String companyID, String employeeId, String appDate, int appType,List<CaculationTime> caculationTimes) {
		
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSetting = overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, appType);
		if(overtimeRestAppCommonSetting.isPresent()){
			if(overtimeRestAppCommonSetting.get().getBonusTimeDisplayAtr().value == UseAtr.USE.value){
				List<Application_New> application = this.applicationRepository.getApp(employeeId,  GeneralDate.fromString(appDate, DATE_FORMAT), PrePostAtr.PREDICT.value, appType);
				if(application.size() > 0){
					Optional<AppOverTime> appOvertime = this.overtimeRepository.getAppOvertime(application.get(0).getCompanyID(), application.get(0).getAppID());
					if(appOvertime.isPresent()){
						List<OverTimeInput> overtimeInputs = overtimeInputRepository.getOvertimeInputByAttendanceId(appOvertime.get().getCompanyID(), appOvertime.get().getAppID(),AttendanceType.BONUSPAYTIME.value);
						caculationTimes = convertCaculation(overtimeInputs,caculationTimes);
					}
				}
			}
		}
		return caculationTimes;
	}
	
	/* 06-04-2_当日以外の場合 */
	@Override
	public List<CaculationTime> checkOutSideTimeTheDay(String companyID, String employeeID, String appDate,
			ApprovalFunctionSetting approvalFunctionSetting, String siftCD,List<CaculationTime> overtimeHours,RecordWorkInfoImport recordWorkInfoImport,List<OvertimeInputCaculation> overtimeInputCaculations) {
		
		
		List<OvertimeInputCaculation> overtimeCal = new ArrayList<>();
		if(recordWorkInfoImport.getAttendanceStampTimeFirst() != null && recordWorkInfoImport.getLeaveStampTimeFirst() != null){
			// 打刻あり
			if(siftCD != recordWorkInfoImport.getWorkTimeCode()){
				overtimeCal = overtimeInputCaculations;
				// Imported(申請承認)「実績内容」.就業時間帯コード != 画面上の就業時間帯
				overtimeHours = printColor(overtimeHours,overtimeCal);
			}else{
				// Imported(申請承認)「実績内容」.就業時間帯コード = 画面上の就業時間帯
				overtimeCal = recordWorkInfoImport.getOvertimeCaculation();
				OvertimeInputCaculation overtimeInputCaculationShiftNight = new OvertimeInputCaculation(1, 11, recordWorkInfoImport.getShiftNightCaculation());
				OvertimeInputCaculation overtimeInputCaculationFlex = new OvertimeInputCaculation(1, 12, recordWorkInfoImport.getFlexCaculation());
				overtimeCal.add(overtimeInputCaculationShiftNight);
				overtimeCal.add(overtimeInputCaculationFlex);
				overtimeHours = printColor(overtimeHours,overtimeCal);
			}
		}else{
			// 出勤または退勤打刻なし
			for(CaculationTime caculationTime : overtimeHours){
				if(caculationTime.getApplicationTime()!= null && caculationTime.getApplicationTime() >= 0){
					caculationTime.setErrorCode(2);
				}
			}
		}
		return overtimeHours;
	}
	/* 06-04-3_当日の場合 */
	@Override
	public List<CaculationTime> checkDuringTheDay(String companyID, String employeeID, String appDate,
			ApprovalFunctionSetting approvalFunctionSetting, String siftCD, List<CaculationTime> overtimeHours,RecordWorkInfoImport recordWorkInfoImport,List<OvertimeInputCaculation> overtimeInputCaculations) {
		
		if(recordWorkInfoImport.getLeaveStampTimeFirst() == null){
			// 退勤打刻なし
			overtimeHours = printColor(overtimeHours, overtimeInputCaculations);
		}else{
			// 打刻あり
			if(siftCD != recordWorkInfoImport.getWorkTimeCode()){
				overtimeHours = printColor(overtimeHours, overtimeInputCaculations);
			}else{
				List<OvertimeInputCaculation> cals = recordWorkInfoImport.getOvertimeCaculation();
				OvertimeInputCaculation overtimeInputCaculationShiftNight = new OvertimeInputCaculation(1, 11, recordWorkInfoImport.getShiftNightCaculation());
				OvertimeInputCaculation overtimeInputCaculationFlex = new OvertimeInputCaculation(1, 12, recordWorkInfoImport.getFlexCaculation());
				cals.add(overtimeInputCaculationShiftNight);
				cals.add(overtimeInputCaculationFlex);
				overtimeHours = printColor(overtimeHours, cals);
			}
			
		}
		return overtimeHours;
	}
	
	/**
	 * @param overtimeInputs
	 * @return
	 */
	private List<CaculationTime> convertCaculation(List<OverTimeInput> overtimeInputs,List<CaculationTime> caculations){
		for(CaculationTime caTime : caculations ){
			for(OverTimeInput overtimeInput : overtimeInputs){
				if(caTime.getFrameNo() == overtimeInput.getFrameNo()){
					caTime.setPreAppTime(overtimeInput.getApplicationTime().toString());
				}
			}
		}
		return caculations;
	}
	
	/**
	 * @return
	 */
	private List<OvertimeInputCaculation> initOvertimeInputCaculation(){
		List<OvertimeInputCaculation> overtimeInputCaculations = new ArrayList<>();
		for(int i = 0; i < 13 ;i++){
			OvertimeInputCaculation overtimeInputCaculation = new  OvertimeInputCaculation(1,i, 0);
			overtimeInputCaculations.add(overtimeInputCaculation);
		}
		
		return overtimeInputCaculations;
	}
	private List<CaculationTime> printColor(List<CaculationTime> overtimeHours,List<OvertimeInputCaculation> overtimeInputCaculations){
		
		for(CaculationTime caculationTime : overtimeHours){
			for(OvertimeInputCaculation overtimeInput :overtimeInputCaculations){
				if(caculationTime.getFrameNo() == overtimeInput.getFrameNo()){
					if(caculationTime.getApplicationTime()!= null && caculationTime.getApplicationTime() > overtimeInput.getResultCaculation()){
						caculationTime.setFrameNo(caculationTime.getFrameNo());
						caculationTime.setErrorCode(2);
						caculationTime.setCaculationTime(overtimeInput.getResultCaculation() == null ? null : Integer.toString(overtimeInput.getResultCaculation()));
					}else{
						caculationTime.setFrameNo(caculationTime.getFrameNo());
						caculationTime.setCaculationTime(overtimeInput.getResultCaculation() == null ? null : Integer.toString(overtimeInput.getResultCaculation()));
					}
				}
			}
		}
		return overtimeHours;
	}
	private List<OverTimeInput> convert(CaculationTime caculationTime){
	List<OverTimeInput> overTimeInputs = new ArrayList<>();
		if(caculationTime .getApplicationTime() != null){
			OverTimeInput overTimeInput = OverTimeInput.createSimpleFromJavaType(caculationTime.getCompanyID(),
					caculationTime.getAppID(),
					caculationTime.getAttendanceID(), 
					caculationTime.getFrameNo(),
					-1,
					-1,
					caculationTime.getApplicationTime(),
					caculationTime.getTimeItemTypeAtr());
			overTimeInputs.add(overTimeInput);
		}
	
	return overTimeInputs;
}
	
}
