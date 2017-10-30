package nts.uk.ctx.at.request.dom.application.lateorleaveearly.service;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarly;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarlyRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.ApplicationDeadline;
import nts.uk.ctx.at.request.dom.setting.request.application.ApplicationDeadlineRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.Deadline;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class LateOrLeaveEarlyServiceDefault implements LateOrLeaveEarlyService {

	@Inject
	LateOrLeaveEarlyRepository lateOrLeaveEarlyRepository;

	@Inject
	ApplicationRepository applicationRepository;

	@Inject
	ApplicationSettingRepository applicationSettingRepository;

	@Inject
	ApplicationSettingRepository applicationSetting;
	
	@Inject 
	EmployeeAdapter employeeAdapter;
	
	@Inject
	ApplicationDeadlineRepository deadlineRepository;	
	
	
	@Override
	public boolean isExist(String companyID, String appID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createLateOrLeaveEarly(LateOrLeaveEarly lateOrLeaveEarly) {

		/** 申請理由が必須  */

		Optional<ApplicationSetting> applicationSettingOp = applicationSettingRepository
				.getApplicationSettingByComID(lateOrLeaveEarly.getCompanyID());
		ApplicationSetting applicationSetting = applicationSettingOp.get();
		int prePost = lateOrLeaveEarly.getPrePostAtr().value;
		Integer lateTime1 = lateOrLeaveEarly.getLateTime1().valueAsMinutes();
		Integer earlyTime1 = lateOrLeaveEarly.getEarlyTime1().valueAsMinutes();
		Integer lateTime2 = lateOrLeaveEarly.getLateTime2().valueAsMinutes();
		Integer earlyTime2 = lateOrLeaveEarly.getEarlyTime2().valueAsMinutes();
		int late1 = lateOrLeaveEarly.getLate1().value;
		int late2 = lateOrLeaveEarly.getLate2().value;
		int early1 = lateOrLeaveEarly.getEarly1().value;
		int early2 = lateOrLeaveEarly.getEarly2().value;

		String applicationReason = lateOrLeaveEarly.getApplicationReason().v();
		String appReasonID = "";
		String appReason = "";
		if (applicationReason.indexOf(":") != -1) {
			if(!applicationReason.equals(":")) {
				appReasonID = applicationReason.split(":")[0];
				appReason = applicationReason.substring(appReasonID.length() + ":".length());
			}
		}
		
		if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
				&& Strings.isEmpty(appReason)) {
			throw new BusinessException("Msg_115");
		}

		// [画面Bのみ]遅刻時刻早退時刻がともに設定されているとき、遅刻時刻≧早退時刻 (#Msg_381#)
		if (((late1 == 1 && early1 == 1 && lateTime1 >= earlyTime1) || (late2 == 1 && early2 == 1 && lateTime2 >= earlyTime2)) 
			&& prePost == 0) {
			throw new BusinessException("Msg_381");
		}
		
		// 遅刻、早退、遅刻2、早退2のいずれか１つはチェック必須(#Msg_382#))
		int checkSelect = late1 + late2 + early1 + early2;
		if (checkSelect == 0) {
			throw new BusinessException("Msg_382");
		}

		// [画面Bのみ]遅刻、早退、遅刻2、早退2のチェックがある遅刻時刻、早退時刻は入力必須(#Msg_470#)
		if (prePost == 0 && 
			( 	(late1 == 1 && lateTime1 == null) || 
				(late2 == 1 && lateTime2 == null) ||
				(early1 == 1 && earlyTime1 == null)||
				(early2 == 1 && earlyTime2 == null) 
			)
		) {
			throw new BusinessException("Msg_470");
		}
		// Add LateOrLeaveEarly
		lateOrLeaveEarlyRepository.add(lateOrLeaveEarly);
	}

	@Override
	public void updateLateOrLeaveEarly(LateOrLeaveEarly lateOrLeaveEarly) {
		
		Optional<ApplicationSetting> applicationSettingOp = applicationSettingRepository
				.getApplicationSettingByComID(lateOrLeaveEarly.getCompanyID());
		ApplicationSetting applicationSetting = applicationSettingOp.get();

		int late1 = lateOrLeaveEarly.getLate1().value;
		int late2 = lateOrLeaveEarly.getLate2().value;
		int early1 = lateOrLeaveEarly.getEarly1().value;
		int early2 = lateOrLeaveEarly.getEarly2().value;
		
		// 遅刻、早退、遅刻2、早退2のいずれか１つはチェック必須(#Msg_382#)
		int checkSelect = late1 + late2 + early1 + early2;
		if (checkSelect == 0) {
			throw new BusinessException("Msg_382");
		}
		//申請承認設定->申請設定->申請制限設定.申請理由が必須＝trueのとき、申請理由が未入力 (#Msg_115#)
		if (applicationSetting.getRequireAppReasonFlg().equals(RequiredFlg.REQUIRED)
				&& Strings.isEmpty(lateOrLeaveEarly.getApplicationReason().v())) {
			throw new BusinessException("Msg_115");
		}
		
		lateOrLeaveEarlyRepository.update(lateOrLeaveEarly);
	}

	@Override
	public void deleteLateOrLeaveEarly(String companyID, String appID) {
		// 5-2.詳細画面削除後の処理
		//TODO

	}

	@Override
	public void registerLateOrLeaveEarly(String companyID) {
	
	}

	@Override
	public void changeApplication(String companyID, String appID, GeneralDate applicationDate, int actualCancelAtr,
			int early1, int earlyTime1, int late1, int lateTime1, int early2, int earlyTime2, int late2, int lateTime2,
			String reasonTemp, String appReason) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getApplicantName(String employeeID) {
		return employeeAdapter.getEmployeeName(employeeID);
	}

}