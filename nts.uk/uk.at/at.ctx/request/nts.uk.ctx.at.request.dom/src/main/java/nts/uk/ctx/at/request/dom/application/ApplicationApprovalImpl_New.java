package nts.uk.ctx.at.request.dom.application;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.brkoffsupchangemng.BrkOffSupChangeMng;
import nts.uk.ctx.at.request.dom.application.holidayshipment.brkoffsupchangemng.BrkOffSupChangeMngRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWorkRepository;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarlyRepository;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampRepository;
import nts.uk.ctx.at.request.dom.application.workchange.IAppWorkChangeRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.BaseDateFlg;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
@Transactional
public class ApplicationApprovalImpl_New implements ApplicationApprovalService_New {

	@Inject
	private ApplicationRepository_New applicationRepository;

	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;

	@Inject
	private AppStampRepository appStampRepository;

	@Inject
	private OvertimeRepository overtimeRepository;

	@Inject
	private GoBackDirectlyRepository goBackDirectlyRepository;

	@Inject
	private IAppWorkChangeRepository workChangeRepository;

	@Inject
	private LateOrLeaveEarlyRepository lateOrLeaveEarlyRepository;
	
	@Inject
	private AppHolidayWorkRepository appHolidayWorkRepository;
	
	@Inject
	private AbsenceLeaveAppRepository absRepo;
	
	@Inject
	private RecruitmentAppRepository recRepo;
	
	@Inject
	private AppAbsenceRepository appAbsenceRepository;
	@Inject
	private BrkOffSupChangeMngRepository brkOffSupChangeMngRepository;
	
	@Inject
	private ApplicationSettingRepository applicationSettingRepository;

	@Override
	public void insert(Application_New application) {
		String companyID = AppContexts.user().companyId();
		applicationRepository.insert(application);
		BaseDateFlg baseDateFlg = applicationSettingRepository.getApplicationSettingByComID(companyID)
				.map(x -> x.getBaseDateFlg()).orElse(BaseDateFlg.SYSTEM_DATE);
		GeneralDate targetDate = baseDateFlg.equals(BaseDateFlg.SYSTEM_DATE) ? GeneralDate.today() : application.getAppDate();
		approvalRootStateAdapter.insertByAppType(application.getCompanyID(), application.getEmployeeID(),
				application.getAppType().value, application.getAppDate(), application.getAppID(), targetDate);
	}

	@Override
	public void delete(String companyID, String appID, Long version, ApplicationType appType) {
		switch (appType) {
		case STAMP_APPLICATION:
			appStampRepository.delete(companyID, appID);
			break;
		case OVER_TIME_APPLICATION:
			overtimeRepository.delete(companyID, appID);
			break;
		case GO_RETURN_DIRECTLY_APPLICATION:
			goBackDirectlyRepository.delete(companyID, appID);
			break;
		case WORK_CHANGE_APPLICATION:
			workChangeRepository.delete(companyID, appID);
			break;
		case EARLY_LEAVE_CANCEL_APPLICATION:
			lateOrLeaveEarlyRepository.remove(companyID, appID);
			break;
		case BREAK_TIME_APPLICATION:
			appHolidayWorkRepository.delete(companyID, appID);
			Optional<BrkOffSupChangeMng> brOptional = this.brkOffSupChangeMngRepository.findHolidayAppID(appID);
			if(brOptional.isPresent()){
				Optional<Application_New> optapplicationLeaveApp = this.applicationRepository.findByID(companyID, brOptional.get().getAbsenceLeaveAppID());
				if(optapplicationLeaveApp.isPresent()){
					Application_New applicationLeaveApp = optapplicationLeaveApp.get();
					applicationLeaveApp.setVersion(applicationLeaveApp.getVersion());
					applicationLeaveApp.getReflectionInformation().setStateReflectionReal(ReflectedState_New.NOTREFLECTED);
					applicationRepository.update(applicationLeaveApp);
				}
				this.brkOffSupChangeMngRepository.remove(appID, brOptional.get().getAbsenceLeaveAppID());
			}
			break;
		case COMPLEMENT_LEAVE_APPLICATION:
			absRepo.remove(appID);
			recRepo.remove(appID);
			break;
		case ABSENCE_APPLICATION:
			appAbsenceRepository.delete(companyID, appID);
			break;
		default:
			break;
		}
		applicationRepository.delete(companyID, appID);
		approvalRootStateAdapter.deleteApprovalRootState(appID);

	}

}
