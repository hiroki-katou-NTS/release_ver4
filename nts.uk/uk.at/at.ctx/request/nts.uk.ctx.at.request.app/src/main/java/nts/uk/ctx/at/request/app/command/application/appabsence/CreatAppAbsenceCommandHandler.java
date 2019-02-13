package nts.uk.ctx.at.request.app.command.application.appabsence;

/*import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;
import nts.uk.ctx.at.request.dom.setting.company.displayname.HdAppDispNameRepository;*/
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.SaveHolidayShipmentCommandHandler;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.SettingNo65;
import nts.uk.ctx.at.request.app.find.setting.company.request.applicationsetting.apptypesetting.DisplayReasonDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.IFactoryApplication;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.HolidayAppType;
import nts.uk.ctx.at.request.dom.application.appabsence.appforspecleave.AppForSpecLeave;
import nts.uk.ctx.at.request.dom.application.appabsence.service.AbsenceServiceProcess;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.RegisterAtApproveReflectionInfoService_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.after.NewAfterRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.other.GetHdDayInPeriodService;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.PeriodCurrentMonth;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.AppliedDate;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.DisplayReasonRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.AppRemainCreateInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.EarchInterimRemainCheck;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainCheckInputParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngCheckRegister;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.PrePostAtr;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.SpecialHolidayEvent;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.service.CheckWkTypeSpecHdEventOutput;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.service.MaxDaySpecHdOutput;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.service.SpecialHolidayEventAlgorithm;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class CreatAppAbsenceCommandHandler extends CommandHandlerWithResult<CreatAppAbsenceCommand, ProcessResult>{
	
	final static String DATE_FORMAT = "yyyy/MM/dd";
	@Inject
	private IFactoryApplication iFactoryApplication;
	@Inject
	private NewBeforeRegister_New newBeforeRegister;
	@Inject
	private SaveHolidayShipmentCommandHandler saveHolidayShipmentCommandHandler;
	@Inject 
	private AbsenceServiceProcess absenceServiceProcess;
	@Inject
	private NewAfterRegister_New newAfterRegister;
	@Inject
	private RegisterAtApproveReflectionInfoService_New registerService;
	@Inject
	private SpecialHolidayEventAlgorithm specHdEventAlg;
	@Inject
	private GetHdDayInPeriodService getHdDayInPeriodSv;
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	@Inject
	private InterimRemainDataMngCheckRegister interimRemainCheckReg;
	/*@Inject
	private HdAppDispNameRepository repoHdAppDispName;*/
	@Inject
	private DisplayReasonRepository displayRep;
	@Inject
	ApplicationSettingRepository applicationSettingRepository;
	@Inject
	private HdAppSetRepository repoHdAppSet;
	@Inject
	private OtherCommonAlgorithm otherCommonAlg;	
	@Override
	protected ProcessResult handle(CommandHandlerContext<CreatAppAbsenceCommand> context) {
		CreatAppAbsenceCommand command = context.getCommand();
		// 会社ID
		String companyID = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();
		// Create Application
		GeneralDate startDate = command.getStartDate() == null ? null : GeneralDate.fromString(command.getStartDate(), DATE_FORMAT);
		GeneralDate endDate = command.getEndDate() == null ? null : GeneralDate.fromString(command.getEndDate(), DATE_FORMAT);
		List<DisplayReasonDto> displayReasonDtoLst = 
				displayRep.findDisplayReason(companyID).stream().map(x -> DisplayReasonDto.fromDomain(x)).collect(Collectors.toList());
		DisplayReasonDto displayReasonSet = displayReasonDtoLst.stream().filter(x -> x.getTypeOfLeaveApp() == command.getHolidayAppType())
				.findAny().orElse(null);
		String appReason = "";
		if(displayReasonSet!=null){
			boolean displayFixedReason = displayReasonSet.getDisplayFixedReason() == 1 ? true : false;
			boolean displayAppReason = displayReasonSet.getDisplayAppReason() == 1 ? true : false;
			String typicalReason = Strings.EMPTY;
			String displayReason = Strings.EMPTY;
			if(displayFixedReason){
				if(Strings.isBlank(command.getAppReasonID())){
					typicalReason += "";
				} else {
					typicalReason += command.getAppReasonID();
				}
			}
			if(displayAppReason){
				if(Strings.isNotBlank(typicalReason)){
					displayReason += System.lineSeparator();
				}
				if(Strings.isBlank(command.getApplicationReason())){
					displayReason += "";
				} else {
					displayReason += command.getApplicationReason();
				}
			}
			Optional<ApplicationSetting> applicationSettingOp = applicationSettingRepository
					.getApplicationSettingByComID(companyID);
			ApplicationSetting applicationSetting = applicationSettingOp.get();
			if(displayFixedReason||displayAppReason){
				if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
						&& Strings.isBlank(typicalReason+displayReason)) {
					throw new BusinessException("Msg_115");
				}
			}
			appReason = typicalReason + displayReason;
		}
		Application_New appRoot = iFactoryApplication.buildApplication(appID, startDate,
				command.getPrePostAtr(), appReason, appReason.replaceFirst(":", System.lineSeparator()),
				ApplicationType.ABSENCE_APPLICATION, startDate, endDate, command.getEmployeeID());
		AppForSpecLeave specHd = null;
		SpecHolidayCommand specHdCm = command.getSpecHd();
		if(command.getHolidayAppType() == HolidayAppType.SPECIAL_HOLIDAY.value && specHdCm != null){
			specHd = AppForSpecLeave.createFromJavaType(appID, specHdCm.getMournerCheck(), specHdCm.getRelationCD(), specHdCm.getRelaReason());
		}
		AppAbsence appAbsence = new AppAbsence(companyID,
				appID,
				command.getHolidayAppType(),
				command.getWorkTypeCode(),
				command.getWorkTimeCode(),
				command.isHalfDayFlg(), 
				command.isChangeWorkHour(),
				command.getAllDayHalfDayLeaveAtr(), 
				command.getStartTime1(),
				command.getEndTime1(),
				command.getStartTime2(),
				command.getEndTime2(),
				specHd);
		// 2-1.新規画面登録前の処理を実行する
		newBeforeRegister.processBeforeRegister(appRoot,0);
		// 7.登録時のエラーチェック
		this.checkBeforeRegister(command,startDate,endDate,true);
		//計画年休上限チェック(check giới han trên plan annual holiday)
		//hoatt-2018-07-04
		absenceServiceProcess.checkLimitAbsencePlan(companyID, command.getEmployeeID(), command.getWorkTypeCode(),
				startDate, endDate, EnumAdaptor.valueOf(command.getHolidayAppType(), HolidayAppType.class));
		// insert
		absenceServiceProcess.createAbsence(appAbsence, appRoot);
		// 2-2.新規画面登録時承認反映情報の整理
		registerService.newScreenRegisterAtApproveInfoReflect(appRoot.getEmployeeID(), appRoot);
		// 暫定データの登録
		GeneralDate cmdStartDate = GeneralDate.fromString(command.getStartDate(), DATE_FORMAT);
		GeneralDate cmdEndDate = GeneralDate.fromString(command.getEndDate(), DATE_FORMAT);
		List<GeneralDate> listDate = new ArrayList<>();
		List<GeneralDate> lstHoliday = otherCommonAlg.lstDateNotHoliday(companyID, command.getEmployeeID(), new DatePeriod(cmdStartDate, cmdEndDate));
		for(GeneralDate loopDate = cmdStartDate; loopDate.beforeOrEquals(cmdEndDate); loopDate = loopDate.addDays(1)){
			if(!lstHoliday.contains(loopDate)) {
				listDate.add(loopDate);	
			}			
		}
		if(!listDate.isEmpty()) {
			interimRemainDataMngRegisterDateChange.registerDateChange(
					companyID, 
					command.getEmployeeID(), 
					listDate);	
		}
		
		// 2-3.新規画面登録後の処理を実行
		return newAfterRegister.processAfterRegister(appRoot);

	}
	/**
	 * 7.登録時のエラーチェック
	 * @param command
	 * @param startDate
	 * @param endDate
	 * @param isInsert
	 */
	public void checkBeforeRegister(CreatAppAbsenceCommand command,GeneralDate startDate,GeneralDate endDate,boolean isInsert){
		String companyID = AppContexts.user().companyId();
		//hoatt 2019.02.11
		//EA修正履歴 No.3104
//		int countDay = 0;
		//申請開始日から申請終了日までループする-(loop từ applicationStartDate tới applicationEndDate)
//		for(int i = 0; startDate.compareTo(endDate) + i <= 0; i++){
//			GeneralDate appDate = startDate.addDays(i);
//			countDay = countDay + 1;
			// 休暇・振替系申請存在チェック
//			if(isInsert){//INPUT．モード=新規(INPUT.mode = New)
				//アルゴリズム「休暇・振替系申請存在チェック」を実施する-(thực hiện thuật toán 「Check tồn tại đơn xin nghỉ phép-nghỉ bù」)
//				saveHolidayShipmentCommandHandler.vacationTransferCheck(command.getEmployeeID(), appDate, command.getPrePostAtr());
//			}
			//その以外
//			if(command.getHolidayAppType() == HolidayAppType.DIGESTION_TIME.value){
				// 11.時間消化登録時のエラーチェック :TODO
//			}
//		}
		SpecHolidayCommand specHd = command.getSpecHd();
		//選択する休暇種類をチェックする-(check holidayType đang chọn)
		if(command.getHolidayAppType() == HolidayAppType.SPECIAL_HOLIDAY.value){//選択する休暇種類が特別休暇の場合
			//hoatt - 2018.08.08 - doi ung specHd
			//INPUT．モードをチェックする-(Check INPUT.mode)
			//2018.09.12 muto #100577 EA修正履歴 No.2684 更新モード（B画面）もチェックする
			//指定する勤務種類が事象に応じた特別休暇かチェックする
			CheckWkTypeSpecHdEventOutput checkSpecHd = specHdEventAlg.checkWkTypeSpecHdForEvent(companyID, command.getWorkTypeCode());
			//取得した事象に応じた特休フラグをチェックする-(Check specialHolidayEventFlag đã lấy)
			if(checkSpecHd.isSpecHdForEventFlag()){//取得した事象に応じた特休フラグがtrue(specialHolidayEventFlag = true)
				SpecialHolidayEvent spHdEv = checkSpecHd.getSpecHdEvent().get();
				//指定する特休枠の上限日数を取得する - (get MaxDay SpecHd)
				MaxDaySpecHdOutput maxDay = specHdEventAlg.getMaxDaySpecHd(companyID, checkSpecHd.getFrameNo().get(), spHdEv,
						specHd == null || specHd.getRelationCD() == null ? Optional.empty() :  Optional.of(specHd.getRelationCD()));
				//申請する日数(ノート1)：
				int appDay = 0;//申請する日数
				if(spHdEv.getIncludeHolidays().value == UseAtr.USE.value){//したメインモデル「事象に対する特別休暇」．休日を取得日に含めるがtrue：
					//申請する日数 = 申請する終了日 - 申請する開始日 + 1
					appDay = startDate.daysTo(endDate) + 1;
				}else{//したメインモデル「事象に対する特別休暇」．休日を取得日に含めるがfalse：
					//19.指定する期間での休日日数を取得する-(Lấy số ngày nghỉ phép trong khoảng thời gian chỉ định)
					int hdDaySys = getHdDayInPeriodSv.getHolidayDayInPeriod(command.getEmployeeID(), new DatePeriod(startDate, endDate));
					//申請する日数 = 申請する終了日 - 申請する開始日 + 1 - 休日日数
					appDay = startDate.daysTo(endDate) + 1 - hdDaySys;
				}
				//上限日数(ノート2)：
				int maxDaySpec = 0;//上限日数
				if(specHd == null || specHd.getRelationCD() == null){//TH fix
					maxDaySpec = maxDay.getMaxDay();
				}else{
					//・画面上に喪主チェックボックスがあり 且つ 喪主チェックボックスにチェックあり：
					//上限日数=取得した上限日数 + 取得した喪主加算日数
					//上限日数=取得した上限日数
					maxDaySpec = specHd.getMournerCheck() ? maxDay.getMaxDay() + maxDay.getDayOfRela() : maxDay.getMaxDay();
				}
				//申請する日数(※ノート1を参照)は上限日数(※ノート2を参照)と比較する-(So sánh appDay(tham khảo note 1) với grantDay(Tham khảo note2))
				if(appDay > maxDaySpec){//申請する日数 > 上限日数 がtrue(appDay > maxDaySpec)
					//エラーメッセージ(Msg_632)(error message)
					throw new BusinessException("Msg_632", Integer.toString(maxDaySpec));
				}
			}
		}
		//No.376
		//hoatt - QA#100286
		//ドメインモデル「休暇申請設定」を取得する(lấy domain 「休暇申請設定」)
		Optional<HdAppSet> hdAppSet = repoHdAppSet.getAll();
		/**	・代休チェック区分 - HolidayType: 1*/
		boolean chkSubHoliday = false;
		/**	・振休チェック区分  - HolidayType: 7*/
		boolean chkPause = false;
		/**	・年休チェック区分 - HolidayType: 0*/
		boolean chkAnnual = false;
		/**	・積休チェック区分 - HolidayType: 4*/
		boolean chkFundingAnnual = false;
		/**	・特休チェック区分 - HolidayType: 3*/
		boolean chkSpecial = true;
		/**	・公休チェック区分 */
		boolean chkPublicHoliday = false;
		/**	・超休チェック区分*/
		boolean chkSuperBreak = true;
		int holidayAppType = command.getHolidayAppType();
		if(hdAppSet.isPresent()){
			HdAppSet hdSet = hdAppSet.get();
			//Bug#100448
			//Bug#101701
			chkSubHoliday = hdSet.getRegisShortLostHd().value == 1 && holidayAppType == 1 ? true : false;//休暇申請設定．代休残数不足登録できる
			chkPause = hdSet.getRegisInsuff().value == 1 && holidayAppType == 7 ? true : false;//休暇申請設定．振休残数不足登録できる
			chkAnnual = hdSet.getRegisNumYear().value == 1 && holidayAppType == 0 ? true : false;//休暇申請設定．年休残数不足登録できる
			chkFundingAnnual = hdSet.getRegisShortReser().value == 1 && holidayAppType == 4 ? true : false;//休暇申請設定．積立年休残数不足登録できる
//			chkPublicHoliday = hdSet.getRegisLackPubHd().value == 1 && holidayAppType == 1 ? true : false;//休暇申請設定．公休残数不足登録できる
		}
		//社員の当月の期間を算出する - 4.社員の当月の期間を算出する
//		＜INPUT＞
//		・会社ID＝ログイン会社ID
//		・社員ID＝申請者社員ID
//		・基準日 = システム日付
		PeriodCurrentMonth cls = otherCommonAlg.employeePeriodCurrentMonthCalculate(companyID, command.getEmployeeID(), GeneralDate.today());
		//登録時の残数チェック
		/** ・登録対象一覧 :	申請(List) */
		//＜INPUT＞
//		・会社ID＝ログイン会社ID
//		・社員ID＝申請者社員ID
//		・集計開始日＝締め開始日
//		・集計終了日＝締め開始日＋１年先（締め開始日.AddYears(1).AddDays(-1)）
//		・モード＝その他モード
//		・基準日＝申請開始日
//		・登録期間の開始日＝申請開始日
//		・登録期間の終了日＝申請終了日
//		・登録対象区分＝申請
//		・登録対象一覧＝申請データ
//		・代休チェック区分＝（休暇申請設定．代休残数不足登録できる＝false）
//		・振休チェック区分＝（休暇申請設定．振休残数不足登録できる＝false）
//		・年休チェック区分＝（休暇申請設定．年休残数不足登録できる＝false）
//		・積休チェック区分＝（休暇申請設定．積立年休残数不足登録できる＝false）
//		・特休チェック区分＝true
//		・公休チェック区分＝（休暇申請設定．公休残数不足登録できる＝false）
//		・超休チェック区分＝true
		List<AppRemainCreateInfor> appData = new ArrayList<>();
		List<GeneralDate> lstDateNotHoliday = otherCommonAlg.lstDateNotHoliday(companyID, command.getEmployeeID(), new DatePeriod(startDate, endDate));
		appData.add(new AppRemainCreateInfor(command.getEmployeeID(), command.getAppID(), GeneralDateTime.now(), startDate, 
				EnumAdaptor.valueOf(command.getPrePostAtr(), PrePostAtr.class), 
				nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType.ABSENCE_APPLICATION, 
				command.getWorkTypeCode() == null ? Optional.empty() : Optional.of(command.getWorkTypeCode()), 
				command.getWorkTimeCode() == null ? Optional.empty() : Optional.of(command.getWorkTimeCode()), 
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(startDate), Optional.of(endDate), lstDateNotHoliday));
		//申請期間から休日以外の申請日を取得する
		
		InterimRemainCheckInputParam inputParam = new InterimRemainCheckInputParam(companyID, command.getEmployeeID(), 
				new DatePeriod(cls.getStartDate(), cls.getStartDate().addYears(1).addDays(-1)), false, startDate, new DatePeriod(startDate, endDate),
				true, new ArrayList<>(), new ArrayList<>(), appData, chkSubHoliday, chkPause, chkAnnual, chkFundingAnnual,
				chkSpecial, chkPublicHoliday, chkSuperBreak);
		EarchInterimRemainCheck checkResult = interimRemainCheckReg.checkRegister(inputParam);
		//EA.2577
		//代休不足区分 or 振休不足区分 or 年休不足区分 or 積休不足区分 or 特休不足区分 = true（残数不足）
		if(checkResult.isChkSubHoliday() || checkResult.isChkPause() || checkResult.isChkAnnual() 
				|| checkResult.isChkFundingAnnual() || checkResult.isChkSpecial()){
			//QA#100887
			String name = "";
			String nametmp = "";
			if(hdAppSet.isPresent()){
				HdAppSet hdApp = hdAppSet.get();
				if(checkResult.isChkSubHoliday()){
					//代表者名 - HdAppType.TEMP_HD
					nametmp = hdApp.getObstacleName() == null ? "" : hdApp.getObstacleName().v();
					name = name != "" && name != "" ? name + "," + nametmp : name + nametmp;
				}
				if(checkResult.isChkPause()){
					//振休名称 - HdAppType.SHIFT
					nametmp = hdApp.getFurikyuName() == null ? "" : hdApp.getFurikyuName().v();
					name = name != "" && name != "" ? name + "," + nametmp : name + nametmp;
				}
				if(checkResult.isChkAnnual()){
					//年休名称 - HdAppType.ANNUAL_HD
					nametmp = hdApp.getYearHdName() == null ? "" : hdApp.getYearHdName().v();
					name = name != "" && name != "" ? name + "," + nametmp : name + nametmp;
				}
				if(checkResult.isChkFundingAnnual()){
					//積休名称 - HdAppType.YEARLY_RESERVED
					nametmp = hdApp.getYearResig() == null ? "" : hdApp.getYearResig().v();
					name = name != "" && name != "" ? name + "," + nametmp : name + nametmp;
				}
				if(checkResult.isChkSpecial()){
					//特別休暇名称 - HdAppType.SPECIAL_VACATION
					nametmp = hdApp.getSpecialVaca() == null ? "" : hdApp.getSpecialVaca().v();
					name = name != "" && name != "" ? name + "," + nametmp : name + nametmp;
				}
			}
			//エラーメッセージ（Msg_1409）
			throw new BusinessException("Msg_1409", name);
		}
	}

	//return エラーメッセージ-確認メッセージ
	public void checkRegister(ParamCheckRegister param){
		SettingNo65 setNo65 = param.getSetNo65();
		//アルゴリズム「代休振休優先消化チェック」を実行する (Thực hiện thuật toán [check sử dụng độ ưu tiên nghi bù])
		absenceServiceProcess.checkDigestPriorityHd(EnumAdaptor.valueOf(setNo65.getPridigCheck(), AppliedDate.class),
				setNo65.isSubVacaManage(), setNo65.isSubVacaTypeUseFlg(), setNo65.isSubHdManage(), setNo65.isSubHdTypeUseFlg(),
				param.getNumberSubHd(), param.getNumberSubVaca());
	}
}
