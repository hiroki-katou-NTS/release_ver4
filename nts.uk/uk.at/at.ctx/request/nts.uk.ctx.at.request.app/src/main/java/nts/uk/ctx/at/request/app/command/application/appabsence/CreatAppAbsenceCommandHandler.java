package nts.uk.ctx.at.request.app.command.application.appabsence;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.SaveHolidayShipmentCommandHandler;
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
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
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
		Application_New appRoot = iFactoryApplication.buildApplication(appID, startDate,
				command.getPrePostAtr(), command.getApplicationReason(), command.getApplicationReason().replaceFirst(":", System.lineSeparator()),
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
		int countDay = 0;
		//申請開始日から申請終了日までループする-(loop từ applicationStartDate tới applicationEndDate)
		for(int i = 0; startDate.compareTo(endDate) + i <= 0; i++){
			GeneralDate appDate = startDate.addDays(i);
			countDay = countDay + 1;
			// 休暇・振替系申請存在チェック
			if(isInsert){//INPUT．モード=新規(INPUT.mode = New)
				//アルゴリズム「休暇・振替系申請存在チェック」を実施する-(thực hiện thuật toán 「Check tồn tại đơn xin nghỉ phép-nghỉ bù」)
				saveHolidayShipmentCommandHandler.vacationTransferCheck(command.getEmployeeID(), appDate, command.getPrePostAtr());
			}
			//その以外
			if(command.getHolidayAppType() == HolidayAppType.DIGESTION_TIME.value){
				// 11.時間消化登録時のエラーチェック :TODO
			}
		}
		SpecHolidayCommand specHd = command.getSpecHd();
		//選択する休暇種類をチェックする-(check holidayType đang chọn)
		if(command.getHolidayAppType() == HolidayAppType.SPECIAL_HOLIDAY.value){//選択する休暇種類が特別休暇の場合
			//hoatt - 2018.08.08 - doi ung specHd
			//INPUT．モードをチェックする-(Check INPUT.mode)
			if(isInsert){//INPUT．モード=新規(INPUT.mode = New)
				//指定する勤務種類が事象に応じた特別休暇かチェックする
				CheckWkTypeSpecHdEventOutput checkSpecHd = specHdEventAlg.checkWkTypeSpecHdForEvent(companyID, command.getWorkTypeCode());
				//取得した事象に応じた特休フラグをチェックする-(Check specialHolidayEventFlag đã lấy)
				if(checkSpecHd.isSpecHdForEventFlag()){//取得した事象に応じた特休フラグがtrue(specialHolidayEventFlag = true)
					SpecialHolidayEvent spHdEv = checkSpecHd.getSpecHdEvent().get();
					if(specHd == null || specHd.getRelationCD() == null){
						return;
					}
					//指定する特休枠の上限日数を取得する - (get MaxDay SpecHd)
					MaxDaySpecHdOutput maxDay = specHdEventAlg.getMaxDaySpecHd(companyID, checkSpecHd.getFrameNo().get(), spHdEv,
							Optional.of(specHd.getRelationCD()));
					//申請する日数(ノート1)：
					int appDay = 0;//申請する日数
					if(spHdEv.getIncludeHolidays().equals(UseAtr.USE)){//したメインモデル「事象に対する特別休暇」．休日を取得日に含めるがtrue：
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
					if(specHd.getMournerCheck() == true){//・画面上に喪主チェックボックスがあり 且つ 喪主チェックボックスにチェックあり：
						//上限日数=取得した上限日数 + 取得した喪主加算日数
						maxDaySpec = maxDay.getMaxDay() + maxDay.getDayOfRela();
					}else{//その以外：
						//上限日数=取得した上限日数
						maxDaySpec = maxDay.getMaxDay();
					}
					//申請する日数(※ノート1を参照)は上限日数(※ノート2を参照)と比較する-(So sánh appDay(tham khảo note 1) với grantDay(Tham khảo note2))
					if(appDay > maxDaySpec){//申請する日数 > 上限日数 がtrue(appDay > maxDaySpec)
						//エラーメッセージ(Msg_632)(error message)
						throw new BusinessException("Msg_632", Integer.toString(maxDaySpec));
					}
				}
			}
		}
		
	}

}
