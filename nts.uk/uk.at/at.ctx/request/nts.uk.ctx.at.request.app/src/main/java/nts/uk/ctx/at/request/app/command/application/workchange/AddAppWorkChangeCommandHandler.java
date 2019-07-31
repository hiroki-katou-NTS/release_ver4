package nts.uk.ctx.at.request.app.command.application.workchange;

/*import java.util.ArrayList;
import nts.uk.ctx.at.shared.dom.worktype.algorithm.SpecHdFrameForWkTypeSetService;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.gul.text.StringUtil;
import nts.gul.collection.CollectionUtil;*/
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.request.app.command.application.common.CreateApplicationCommand;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.IFactoryApplication;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.IWorkChangeRegisterService;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@Transactional
public class AddAppWorkChangeCommandHandler extends CommandHandlerWithResult<AddAppWorkChangeCommand, ProcessResult> {
	
	@Inject
	private IWorkChangeRegisterService workChangeRegisterService;
	@Inject
	private IFactoryApplication IfacApp;
//	@Inject
//	private CollectAchievement collectAchievement;
//	@Inject
//	private SpecHdFrameForWkTypeSetService specHdWkpTypeSv;
	@Inject 
	private OtherCommonAlgorithm  otherCommonAlgorithm;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;
	
	@Inject
	private ApplicationSettingRepository applicationSettingRepository;
	
	@Override
	protected ProcessResult handle(CommandHandlerContext<AddAppWorkChangeCommand> context) {
		AddAppWorkChangeCommand addCommand = context.getCommand();

		// Application command
		CreateApplicationCommand appCommand = addCommand.getApplication();
		// Work change command
		AppWorkChangeCommand workChangeCommand = addCommand.getWorkChange();
		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();
		// 入力者 = 申請者
		// 申請者
		String applicantSID = addCommand.getEmployeeID()!=null?addCommand.getEmployeeID(): AppContexts.user().employeeId();
		
		AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(
				companyId, 
				ApplicationType.WORK_CHANGE_APPLICATION.value).get();
		String appReason = Strings.EMPTY;	
		String typicalReason = Strings.EMPTY;
		String displayReason = Strings.EMPTY;
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)){
			typicalReason += appCommand.getAppReasonID();
		}
		if(appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
			if(Strings.isNotBlank(typicalReason)){
				displayReason += System.lineSeparator();
			}
			displayReason += appCommand.getApplicationReason();
		}
		Optional<ApplicationSetting> applicationSettingOp = applicationSettingRepository
				.getApplicationSettingByComID(companyId);
		ApplicationSetting applicationSetting = applicationSettingOp.get();
		if(appTypeDiscreteSetting.getTypicalReasonDisplayFlg().equals(AppDisplayAtr.DISPLAY)
			||appTypeDiscreteSetting.getDisplayReasonFlg().equals(AppDisplayAtr.DISPLAY)){
			if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
					&& Strings.isBlank(typicalReason+displayReason)) {
				throw new BusinessException("Msg_115");
			}
		}
		appReason = typicalReason + displayReason;
		
		// 申請
		Application_New app = IfacApp.buildApplication(appID, appCommand.getStartDate(), appCommand.getPrePostAtr(), appReason, 
				appReason, ApplicationType.WORK_CHANGE_APPLICATION, appCommand.getStartDate(), appCommand.getEndDate(), applicantSID);
					
		// 勤務変更申請
		AppWorkChange workChangeDomain = AppWorkChange.createFromJavaType(
				companyId, 
				appID,
				workChangeCommand.getWorkTypeCd(), 
				workChangeCommand.getWorkTimeCd(),
				workChangeCommand.getExcludeHolidayAtr(), 
				workChangeCommand.getWorkChangeAtr(),
				workChangeCommand.getGoWorkAtr1(), 
				workChangeCommand.getBackHomeAtr1(),
				workChangeCommand.getBreakTimeStart1(), 
				workChangeCommand.getBreakTimeEnd1(),
				workChangeCommand.getWorkTimeStart1(), 
				workChangeCommand.getWorkTimeEnd1(),
				workChangeCommand.getWorkTimeStart2(), 
				workChangeCommand.getWorkTimeEnd2(),
				workChangeCommand.getGoWorkAtr2(), 
				workChangeCommand.getBackHomeAtr2());
		
		//1日休日のチェック
		checkHoliday(companyId,applicantSID,addCommand);
		//ドメインモデル「勤務変更申請設定」の新規登録をする
		return workChangeRegisterService.registerData(workChangeDomain, app, addCommand.isCheckOver1Year());
	}

	
	/**
	 * 1日休日のチェック
	 * @param applicantSID 
	 * @param SID
	 * @param AddAppWorkChangeCommand
	 */
	private void checkHoliday(String companyId, String applicantSID, AddAppWorkChangeCommand addCommand) {
		boolean isCheck = addCommand.getWorkChange().getExcludeHolidayAtr() == 1;
		// INPUT．休日除くチェック区分をチェックする
		if (isCheck) {
			//申請期間から休日の申請日を取得する
			GeneralDate startDate = addCommand.getApplication().getStartDate();

			GeneralDate endDate = addCommand.getApplication().getEndDate();
			
			List<GeneralDate> dateClears = otherCommonAlgorithm.lstDateIsHoliday(companyId, applicantSID,
					new DatePeriod(startDate, endDate));

			
			int totalDate = startDate.daysTo(endDate) + 1;

			if (dateClears.size() == totalDate) {
				//日付一覧(output)の件数 > 0
				String dateListString = "";

				for (int i = 0; i < dateClears.size(); i++) {
					if (dateListString != "") {
						dateListString += "、";
					}
					dateListString += dateClears.get(i).toString("yyyy/MM/dd");
				}
				throw new BusinessException("Msg_1459",dateListString);
			}
		}
	}
}
