package nts.uk.ctx.at.request.dom.applicationreflect.service;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectProcessRecord;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.ApprovalProcessingUseSettingAc;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.IdentityProcessUseSetAc;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.AppOvertimeSetting;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.AppOvertimeSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.UnitTime;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.WithdrawalAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.WithdrawalAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.withdrawalrequestset.WithDrawalReqSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.withdrawalrequestset.WorkUse;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.AppReflectionSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.ApplyTimeSchedulePriority;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.ClassifyScheAchieveAtr;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.PriorityTimeReflectAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSettingRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class InformationSettingOfAppForReflectImpl implements InformationSettingOfAppForReflect {
	@Inject
	private WithDrawalReqSetRepository furiSetting;
	@Inject
	private GoBackDirectlyCommonSettingRepository chokochoki;
	@Inject
	private AppOvertimeSettingRepository overtimeSetting;
	@Inject
	private RequestSettingRepository requestSetting;
	@Inject
	private WithdrawalAppSetRepository kyushutsuSetting;
	@Inject
	private AppReflectProcessRecord appRefRecord;
	@Override
	public InformationSettingOfEachApp getSettingOfEachApp() {
		String cid = AppContexts.user().companyId();
		Optional<WorkUse> recAbsSetting = furiSetting.getDeferredWorkTimeSelect(cid);
		//振休振出申請設定: 就業時間帯選択の利用
		WorkUse furikyuFurishutsu = WorkUse.USE;
		if(recAbsSetting.isPresent()) {
			furikyuFurishutsu = recAbsSetting.get();
		}
		Optional<UseAtr> optGobackSetting = chokochoki.getWorkChangeTimeAtr(cid);
		//直行直帰申請共通設定．勤務の変更申請時
		boolean chokochoki = true;
		if(optGobackSetting.isPresent()) {
			chokochoki = optGobackSetting.get() == UseAtr.USE ? true : false;
		}
		//残業申請設定．残業事後反映設定．勤務種類、就業時間帯
		boolean zangyouRecordReflect = true;
		//残業申請設定．残業事前反映設定．「勤務種類、就業時間帯、勤務時間」
		boolean zangyouScheReflect = true;
		//残業申請設定．残業事前反映設定．残業時間
		boolean zangyouWorktime = true;
		Optional<AppOvertimeSetting> optOvertimeSetting = overtimeSetting.getByCid(cid);
		if(optOvertimeSetting.isPresent()) {
			AppOvertimeSetting overSetting = optOvertimeSetting.get();
			zangyouRecordReflect = overSetting.getPostTypeSiftReflectFlg() == UseAtr.USE ? true : false;
			zangyouScheReflect = overSetting.getPreTypeSiftReflectFlg() == UseAtr.USE ? true : false;
			zangyouWorktime = overSetting.getPreOvertimeReflectFlg() == UseAtr.USE ? true : false;
		}
		
		Optional<AppReflectionSetting> optRequestSetting = requestSetting.getAppReflectionSetting(cid);
		//申請反映設定．予定時刻の反映時刻優先
		ApplyTimeSchedulePriority scheJikokuYusen = ApplyTimeSchedulePriority.PRIORITY_FIX_TIME_SCHEDULED_WORK;
		//申請反映設定．反映時刻優先
		PriorityTimeReflectAtr workJikokuYusen = PriorityTimeReflectAtr.APP_TIME;
		//申請反映設定．事前申請スケジュール反映
		boolean jizenScheYusen = true;
		//申請反映設定．予定と実績を同じに変更する区分
		ClassifyScheAchieveAtr scheAndWorkChange = ClassifyScheAchieveAtr.ALWAYS_CHANGE_AUTO;
		if(optRequestSetting.isPresent()) {
			AppReflectionSetting requestSetting = optRequestSetting.get();
			scheJikokuYusen = requestSetting.getReflecTimeofSche();
			workJikokuYusen = requestSetting.getPriorityTimeReflectFlag();
			jizenScheYusen = requestSetting.getScheReflectFlg();
			scheAndWorkChange = requestSetting.getClassScheAchi();
		}
		//休出申請設定．事前反映設定．休出時間
		boolean isHwScheReflect = true;
		boolean isHwRecordReflectTime = true;
		boolean isHwRecordReflectBreak = true;
		Optional<WithdrawalAppSet> optBreackOfWork = kyushutsuSetting.getByCid(cid);
		if(optBreackOfWork.isPresent()) {
			WithdrawalAppSet holidayWorkReflectSetting = optBreackOfWork.get();
			isHwScheReflect = holidayWorkReflectSetting.getRestTime() == UnitTime.ONEMIN ? false : true;
			isHwRecordReflectTime = holidayWorkReflectSetting.getWorkTime() == nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.UseAtr.USE ? true : false;
			isHwRecordReflectBreak = holidayWorkReflectSetting.getBreakTime() == nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.UseAtr.USE ? true : false;
		}
		Optional<IdentityProcessUseSetAc> getIdentityProcessUseSet = appRefRecord.getIdentityProcessUseSet(cid);
		Optional<ApprovalProcessingUseSettingAc> getApprovalProcessingUseSetting = appRefRecord.getApprovalProcessingUseSetting(cid);
		return new InformationSettingOfEachApp(furikyuFurishutsu,
				chokochoki,
				zangyouRecordReflect,
				zangyouScheReflect,
				zangyouWorktime,
				scheJikokuYusen,
				workJikokuYusen,
				jizenScheYusen,
				scheAndWorkChange,
				isHwScheReflect,
				isHwRecordReflectTime,
				isHwRecordReflectBreak,
				getIdentityProcessUseSet,
				getApprovalProcessingUseSetting);
	}

}
