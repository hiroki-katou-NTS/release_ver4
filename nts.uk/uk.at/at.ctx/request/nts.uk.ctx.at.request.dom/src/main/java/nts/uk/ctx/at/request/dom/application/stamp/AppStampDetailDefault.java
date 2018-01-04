package nts.uk.ctx.at.request.dom.application.stamp;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.BeforePreBootMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class AppStampDetailDefault implements AppStampDetailDomainService {

	@Inject
	private AppStampRepository appStampRepository; 
	
	@Inject
	private BeforePreBootMode beforePreBootMode;
	
	@Inject
	private AppStampCommonDomainService appStampCommonDomainService;
	
	/*@Inject
	private PreLaunchScreenSetting preLaunchScreenSetting;*/
	
	@Inject
	private DetailBeforeUpdate detailBeforeProcessRegister;
	
	@Inject
	private DetailAfterUpdate afterProcessDetail;
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Override
	public void appStampPreProcess(AppStamp appStamp) {
//		beforePreBootMode.judgmentDetailScreenMode(appStamp, appStamp.getApplicationDate());
//		// this.preLaunchScreenSetting
//		appStampCommonDomainService.appStampSet(appStamp.getCompanyID());
//		// 13.実績を取得する
	}

	@Override
	public List<String> appStampUpdate(String applicationReason, AppStamp appStamp) {
		appStampCommonDomainService.appReasonCheck(applicationReason, appStamp);
		appStampCommonDomainService.validateReason(appStamp);
		return appStampUpdateProcess(appStamp);
	}
	
	private List<String> appStampUpdateProcess(AppStamp appStamp) {
		detailBeforeProcessRegister.processBeforeDetailScreenRegistration(
				appStamp.getApplication_New().getCompanyID(), 
				appStamp.getApplication_New().getEmployeeID(), 
				appStamp.getApplication_New().getAppDate(), 
				1, 
				appStamp.getApplication_New().getAppID(), 
				appStamp.getApplication_New().getPrePostAtr(), 
				appStamp.getVersion());
		appStampRepository.updateStamp(appStamp);
		applicationRepository.updateWithVersion(appStamp.getApplication_New());
		return afterProcessDetail.processAfterDetailScreenRegistration(appStamp.getApplication_New());
	}
}
