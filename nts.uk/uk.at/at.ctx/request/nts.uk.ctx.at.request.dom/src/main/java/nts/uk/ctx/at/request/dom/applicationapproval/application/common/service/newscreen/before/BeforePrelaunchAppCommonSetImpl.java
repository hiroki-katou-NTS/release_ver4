package nts.uk.ctx.at.request.dom.applicationapproval.application.common.service.newscreen.before;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.management.RuntimeErrorException;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.applicationapproval.application.ApplicationType;
import nts.uk.ctx.at.request.dom.applicationapproval.application.common.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.at.request.dom.applicationapproval.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.request.application.common.BaseDateFlg;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.requestofeach.RequestOfEachCompany;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.requestofeach.RequestOfEachCompanyRepository;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.requestofeach.RequestOfEachWorkplace;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.requestofeach.RequestOfEachWorkplaceRepository;

@Stateless
public class BeforePrelaunchAppCommonSetImpl implements BeforePrelaunchAppCommonSet {
	
	private final String BASE_DATE_CACHE_KEY = "baseDate";
	
	@Inject
	private ApplicationSettingRepository appSettingRepository;
	
	@Inject
	private EmployeeAdapter employeeAdaptor;
	
	@Inject
	private RequestOfEachWorkplaceRepository requestOfEachWorkplaceRepository;
	
	@Inject
	private RequestOfEachCompanyRepository requestOfEachCompanyRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;
	
	public AppCommonSettingOutput prelaunchAppCommonSetService(String companyID, String employeeID, int rootAtr, ApplicationType targetApp, GeneralDate appDate){
		AppCommonSettingOutput appCommonSettingOutput = new AppCommonSettingOutput();
		GeneralDate baseDate = null;
		// ドメインモデル「申請承認設定」を取得する ( Acquire the domain model "application approval setting" )
		Optional<ApplicationSetting> applicationSettingOp = appSettingRepository.getApplicationSettingByComID(companyID);
		if(!applicationSettingOp.isPresent()){
			throw new RuntimeException("Not found ApplicationSetting in table KRQST_APPLICATION_SETTING, companyID =" + companyID);
		}
		ApplicationSetting applicationSetting = applicationSettingOp.get();
		
		appCommonSettingOutput.applicationSetting = applicationSetting;
		Optional<AppTypeDiscreteSetting> appTypeDiscreteSettingOp = appTypeDiscreteSettingRepository.getAppTypeDiscreteSettingByAppType(companyID, targetApp.value);
		if(appTypeDiscreteSettingOp.isPresent()) {
			AppTypeDiscreteSetting appTypeDiscreteSetting = appTypeDiscreteSettingOp.get();
			appCommonSettingOutput.appTypeDiscreteSettings.add(appTypeDiscreteSetting);
		}
		// ドメインモデル「申請設定」．承認ルートの基準日をチェックする ( Domain model "application setting". Check base date of approval route )
		if(applicationSetting.getBaseDateFlg().equals(BaseDateFlg.APP_DATE)){
			if(appDate==null){
			// 「申請設定」．承認ルートの基準日が申請対象日時点の場合 ( "Application setting". When the reference date of the approval route is the date of the application target date )
			// 申請対象日のパラメータがあるかチェックする ( Check if there is a parameter on the application target date )
				baseDate = GeneralDate.today();
			} else {
				baseDate = appDate;
			}
		} else {
			// 「申請設定」．承認ルートの基準日がシステム日付時点の場合 ( "Application setting". When the base date of the approval route is at the time of the system date )
			baseDate = GeneralDate.today();
		}
		appCommonSettingOutput.generalDate = baseDate;
		
		// 申請本人の所属職場を含める上位職場を取得する ( Acquire the upper workplace to include the workplace of the applicant himself / herself )
		List<String> workPlaceIDs = employeeAdaptor.findWpkIdsBySid(companyID, employeeID, baseDate);
		List<RequestOfEachWorkplace> loopResult = new ArrayList<>();
		for(String workPlaceID : workPlaceIDs) {
			// ドメインモデル「職場別申請承認設定」を取得する ( Acquire domain model "Application approval setting by workplace" )
			Optional<RequestOfEachWorkplace> requestOfEarchWorkplaceOp = requestOfEachWorkplaceRepository.getRequest(companyID, workPlaceID);
			if(requestOfEarchWorkplaceOp.isPresent()) {
				loopResult.add(requestOfEarchWorkplaceOp.get());
				break;
			}
		}
		// ドメインモデル「職場別申請承認設定」を取得できたかチェックする ( Check whether domain model "application approval setting by workplace" could be acquired )
		if(loopResult.size() == 0) {
			Optional<RequestOfEachCompany> rqOptional = requestOfEachCompanyRepository.getRequestByCompany(companyID);
			if(rqOptional.isPresent())
				appCommonSettingOutput.requestOfEachCommon = rqOptional.get();
		} else {
				// ドメインモデル「会社別申請承認設定」を取得する ( Acquire the domain model "application approval setting by company" )
				appCommonSettingOutput.requestOfEachCommon = loopResult.get(0);
		}
		
		// アルゴリズム「社員所属雇用履歴を取得」を実行する ( Execute the algorithm "Acquire employee affiliation employment history" )
		/*String employeeCD = employeeAdaptor.getEmploymentCode(companyID, employeeID, baseDate);
		if(employeeCD!=null) {
			throw new BusinessException("Msg_426");
		}*/
		// ドメインモデル「雇用別申請承認設定」を取得する ( Acquire the domain model "application approval setting by employment" )
		// ApplicationCommonSetting obj1 = ApplicationApprovalSettingByEmployment.find(companyID, employeeCD);
		// return obj1
		
		return appCommonSettingOutput;
	}
	
}
