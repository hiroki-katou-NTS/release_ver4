package nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.service;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.uk.ctx.at.request.dom.application.common.ApplicationType;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.StartCheckErrorService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.BeforePrelaunchAppCommonSet;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReasonRepository;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSetting;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSettingRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class GoBackDirectCommonDefault implements GoBackDirectCommonService {

	@Inject
	GoBackDirectlyCommonSettingRepository goBackRepo;

	@Inject
	ApplicationReasonRepository appFormRepo;
	
	@Inject 
	EmployeeAdapter employeeAdapter;
	
	@Inject 
	BeforePrelaunchAppCommonSet beforePrelaunchAppCommonSet;
	@Inject 
	StartCheckErrorService startCheckErrorService;

	@Override
	public GoBackDirectBasicData getSettingData(String SID) {
		String companyID = AppContexts.user().companyId();
		//1-1.新規画面起動前申請共通設定を取得する
		AppCommonSettingOutput appCommonSetting = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(
				companyID, 
				SID, 
				1, 
				ApplicationType.GO_RETURN_DIRECTLY_APPLICATION, 
				null);
		//1-4
		
		//アルゴリズム「1-5.新規画面起動時のエラーチェック」を実行する
		startCheckErrorService.checkError(ApplicationType.GO_RETURN_DIRECTLY_APPLICATION.value);
		//アルゴリズム「直行直帰基本データ」を実行する 
		GoBackDirectBasicData dataSetting = new GoBackDirectBasicData();
		// ドメインモデル「直行直帰申請共通設定」より取得する
		dataSetting.setGoBackDirectSet(goBackRepo.findByCompanyID(companyID));
		// アルゴリズム「社員IDから社員を取得する」を実行する
		String employeeName = employeeAdapter.getEmployeeName(AppContexts.user().employeeId());
		dataSetting.setEmployeeName(employeeName);
		// ドメインモデル「申請定型理由」を取得
		List<ApplicationReason> listReason = appFormRepo.getReasonByAppType(companyID,
				ApplicationType.GO_RETURN_DIRECTLY_APPLICATION.value);
		dataSetting.setListAppReason(listReason);
		dataSetting.setAppCommonSettingOutput(appCommonSetting);
		//アルゴリズム「1-5.新規画面起動時のエラーチェック」を実行する
		startCheckErrorService.checkError(ApplicationType.GO_RETURN_DIRECTLY_APPLICATION.value);
		return dataSetting;
	}

}
